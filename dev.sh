#!/usr/bin/env bash
# whenly local dev: start PostgreSQL, backend, and frontend with HMR.
#
# Usage:
#   ./dev.sh        start everything (logs stream live; Ctrl+C stops all)
#
# Ports:
#   Frontend (Vite)  http://localhost:5173
#   Backend          http://localhost:8080
#   Database         localhost:5432  (user/pass/db: whenly)

set -euo pipefail

cd "$(dirname "$0")"

GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}Starting whenly development environment...${NC}"
echo ""

# Pre-flight: docker daemon reachable?
if ! docker info >/dev/null 2>&1; then
  echo -e "${RED}Docker daemon not reachable. Start OrbStack / Docker Desktop first.${NC}" >&2
  exit 1
fi

mkdir -p logs

# Step 1: PostgreSQL
echo -e "${BLUE}Step 1/3: Starting PostgreSQL...${NC}"
docker compose -f docker-compose.dev.yml up -d

echo -e "${YELLOW}Waiting for PostgreSQL...${NC}"
max_attempts=30
attempt=0
until docker compose -f docker-compose.dev.yml exec -T postgres pg_isready -U whenly -d whenly >/dev/null 2>&1; do
  attempt=$((attempt + 1))
  if [ $attempt -ge $max_attempts ]; then
    echo -e "${RED}PostgreSQL failed to start after ${max_attempts}s${NC}"
    exit 1
  fi
  echo -n "."
  sleep 1
done
echo ""
echo -e "${GREEN}PostgreSQL ready${NC}"
echo ""

# Step 2: Backend
echo -e "${BLUE}Step 2/3: Starting Backend...${NC}"
echo -e "${YELLOW}Backend -> http://localhost:8080${NC}"
echo ""

cd backend
./gradlew bootRun >../logs/backend.log 2>&1 &
BACKEND_PID=$!
cd ..
echo $BACKEND_PID >.backend.pid

echo -e "${YELLOW}Waiting for Backend...${NC}"
max_attempts=60
attempt=0
until curl -fsS http://localhost:8080/actuator/health >/dev/null 2>&1; do
  attempt=$((attempt + 1))
  if [ $attempt -ge $max_attempts ]; then
    echo -e "${RED}Backend failed to start after ${max_attempts}s — check logs/backend.log${NC}"
    kill $BACKEND_PID 2>/dev/null || true
    exit 1
  fi
  if ! kill -0 $BACKEND_PID 2>/dev/null; then
    echo -e "${RED}Backend process died — check logs/backend.log${NC}"
    exit 1
  fi
  echo -n "."
  sleep 1
done
echo ""
echo -e "${GREEN}Backend ready${NC}"
echo ""

# Step 3: Frontend
echo -e "${BLUE}Step 3/3: Starting Frontend (Vite HMR)...${NC}"
echo -e "${YELLOW}Frontend -> http://localhost:5173${NC}"
echo ""

cd frontend
npm run dev >../logs/frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..
echo $FRONTEND_PID >.frontend.pid

sleep 3

echo ""
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${GREEN}whenly is running!${NC}"
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
echo -e "${BLUE}Frontend:${NC}   http://localhost:5173"
echo -e "${BLUE}Backend:${NC}    http://localhost:8080"
echo -e "${BLUE}Database:${NC}   localhost:5432  (user/pass/db: whenly)"
echo ""
echo -e "${RED}Stop: ./stop.sh or Ctrl+C${NC}"
echo ""

cat >stop.sh <<'STOP_SCRIPT'
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
STOP_SCRIPT
chmod +x stop.sh

trap './stop.sh; exit' INT TERM

echo -e "${BLUE}Tailing logs (Ctrl+C to stop all services)...${NC}"
echo ""
tail -f logs/backend.log logs/frontend.log
