#!/usr/bin/env bash
cd "$(dirname "$0")"
echo "Stopping whenly development environment..."

if [ -f .backend.pid ]; then
  kill "$(cat .backend.pid)" 2>/dev/null && echo "Backend stopped" || true
  rm .backend.pid
fi

if [ -f .frontend.pid ]; then
  kill "$(cat .frontend.pid)" 2>/dev/null && echo "Frontend stopped" || true
  rm .frontend.pid
fi

docker compose -f docker-compose.dev.yml down
echo "All services stopped."
