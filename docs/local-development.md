# Local development

Pick one of two paths: **everything in Docker** (no JDK/Node needed locally) or **hybrid** (Docker for Postgres, native for backend + frontend).

## Path A — full Docker

```bash
cp .env.example .env
# edit .env if you like
docker compose up --build
```

Open http://localhost:8080.

The stack:
- `nginx` on host port 8080 → reverse-proxies `/api/*` to the backend and `/*` to the frontend
- `frontend` (nginx + static Vue build)
- `backend` (Spring Boot 4 on JRE 21)
- `postgres` 17

First boot runs Flyway migrations and exposes the schema.

## Path B — hybrid

Postgres in Docker, app code on host. Faster iteration.

```bash
docker compose up postgres -d

# Backend
cd backend
./gradlew bootRun     # default profile, dev DB at localhost:5432

# Frontend (separate terminal)
cd frontend
npm install
npm run dev           # Vite on :5173, proxies /api → :8080
```

Browse http://localhost:5173.

## Tests

```bash
# Backend
cd backend && ./gradlew test

# Frontend type-check
cd frontend && npm run typecheck
```

## Anatomy

```
whenly/
├── backend/   Spring Boot 4 · Java 21 · Gradle Kotlin DSL
├── frontend/  Vue 3 · TypeScript · Vite · Tailwind v4 · vue-i18n
├── nginx/     Edge reverse proxy with security headers
├── docs/      Architecture, security, open questions
└── docker-compose.yml
```

## Resetting the database

```bash
docker compose down -v   # WARNING: drops the postgres volume
docker compose up --build
```
