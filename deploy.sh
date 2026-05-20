#!/bin/bash
# whenly deployment script
# Validates environment, then builds and starts the stack.

set -euo pipefail

cd "$(dirname "$0")"

echo "whenly Deployment"
echo "================="

if [ ! -f .env ]; then
  echo "ERROR: .env file missing — copy from .env.example and fill in secrets"
  exit 1
fi

set -a
source .env
set +a

ERRORS=0

check() {
  local var="$1"
  local val="${!var:-}"
  if [ -z "$val" ]; then
    echo "ERROR: $var is missing"
    ERRORS=$((ERRORS + 1))
  fi
}

check WHENLY_DB_PASSWORD
check WHENLY_BASE_URL

if [ "$ERRORS" -gt 0 ]; then
  echo "$ERRORS required variable(s) missing — aborting"
  exit 1
fi

echo "Building and starting containers..."
docker compose -f docker-compose.prod.yml pull --quiet 2>/dev/null || true
docker compose -f docker-compose.prod.yml up -d --build --remove-orphans

echo "Waiting for backend health check..."
for i in $(seq 1 18); do
  if curl -sf http://127.0.0.1:8091/actuator/health | grep -q '"status":"UP"'; then
    echo "Backend healthy after $((i * 10))s"
    break
  fi
  if [ "$i" -eq 18 ]; then
    echo "Health check timed out — dumping logs:"
    docker compose -f docker-compose.prod.yml logs backend --tail=50
    exit 1
  fi
  sleep 10
done

echo "Deployment complete at $(date)"
