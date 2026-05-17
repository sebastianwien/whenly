#!/usr/bin/env bash
# whenly local dev: build + start the full stack, stream logs, clean shutdown.
#
# Usage:
#   ./dev.sh           build + start everything, follow logs
#   ./dev.sh --reset   nuke the postgres volume first (fresh DB)
#   ./dev.sh --logs    skip build, attach to logs of a running stack
#   ./dev.sh --rebuild rebuild a single service, e.g. ./dev.sh --rebuild backend

set -euo pipefail

cd "$(dirname "$0")"

# Pre-flight: docker daemon reachable?
if ! docker info >/dev/null 2>&1; then
  echo "✗ Docker daemon not reachable. Start OrbStack / Docker Desktop first." >&2
  exit 1
fi

# Pre-flight: .env exists?
if [[ ! -f .env ]]; then
  echo "ℹ no .env found, copying from .env.example"
  cp .env.example .env
fi

case "${1:-}" in
  --reset)
    echo "⚠ wiping postgres volume (all polls + votes will be gone)"
    docker compose down -v
    docker compose up --build -d
    ;;
  --logs)
    docker compose logs -f
    exit 0
    ;;
  --rebuild)
    service="${2:?usage: ./dev.sh --rebuild <service>}"
    docker compose up -d --build "$service"
    docker compose logs -f "$service"
    exit 0
    ;;
  "")
    docker compose up --build -d
    ;;
  *)
    echo "unknown option: $1" >&2
    sed -n '3,9p' "$0" >&2
    exit 2
    ;;
esac

# Wait for backend health before printing the URL
echo -n "waiting for backend to come up "
for i in {1..60}; do
  if curl -fsS http://localhost:8080/actuator/health 2>/dev/null | grep -q '"status":"UP"'; then
    echo " ✓"
    break
  fi
  echo -n "."
  sleep 2
  if [[ "$i" == 60 ]]; then
    echo
    echo "✗ backend still not healthy after 2 minutes — showing logs:" >&2
    docker compose logs --tail 80 backend
    exit 1
  fi
done

port="$(grep -E '^WHENLY_HOST_PORT=' .env | cut -d= -f2)"
port="${port:-8080}"

cat <<EOF

  whenly is up
  ─────────────────────────────────────────
   App           http://localhost:${port}
   Health        http://localhost:${port}/actuator/health
   API base      http://localhost:${port}/api

  Useful next steps
   ./dev.sh --logs         stream logs
   ./stop.sh               stop the stack
   ./dev.sh --reset        wipe DB and restart fresh
   ./dev.sh --rebuild X    rebuild + restart one service (backend|frontend|nginx)
EOF
