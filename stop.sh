#!/usr/bin/env bash
# whenly: stop and remove the local stack. Postgres volume is kept.
# Pass --purge to also drop the volume (all polls + votes gone).

set -euo pipefail
cd "$(dirname "$0")"

if [[ "${1:-}" == "--purge" ]]; then
  echo "⚠ also dropping the postgres volume"
  docker compose down -v
else
  docker compose down
fi
