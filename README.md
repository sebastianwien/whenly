# whenly

> Working title — easy to rename later. See `docs/OPEN_QUESTIONS.md`.

A privacy-friendly, open-source alternative to Doodle and Nuudel. Create a poll, share a link, let the app pick the best slot. No accounts required, no trackers, no ads, no dark patterns. Data auto-deletes after the retention window.

## What's in the box

- **Three-state voting** — yes, maybe, no (Doodle parity, Nuudel gap).
- **Smart suggestion** — the best slot is picked for you, with a reason.
- **ICS + QR + per-option comments** — sharing and discussion without a chat app.
- **Auto-delete** — default 30 days after the last option, max 365, configurable.
- **No login wall** — capability URLs (`/p/{shareToken}` + `/admin/{adminToken}`).
- **Mobile-first PWA** — installable, offline shell, 60 px touch targets.
- **German + English** — full i18n on day one.
- **Self-hostable** — single `docker compose up`.

More detail in `docs/FEATURES.md`.

## Stack

- Backend: **Java 21 · Spring Boot 4.0.0 · PostgreSQL 17 · Flyway · Gradle Kotlin DSL**
- Frontend: **Vue 3 · TypeScript · Vite · Tailwind v4 · Pinia · Vue i18n**
- Infra: **Docker Compose · nginx**
- Licence: **AGPL-3.0**

Architecture diagram, module map, and threading notes in `docs/ARCHITECTURE.md`.

## Run it locally

```bash
git clone git@github.com:sebastianwien/whenly.git
cd whenly
cp .env.example .env       # tweak DB password etc.
docker compose up --build
```

Open http://localhost:8080.

Hybrid mode (Postgres in Docker, app on host) and reset commands are in `docs/local-development.md`.

## API

REST, JSON, capability tokens. See `docs/API.md`.

## Security

Threat model, capability-token table, OWASP top-10 walk-through: `docs/SECURITY.md`.

## Status

This is a v0.1 MVP. Reviewed (architect + UX), tests pass (9 backend unit tests, TypeScript clean, frontend builds, PWA generated), but **end-to-end integration testing in Docker has not been done in this session** — the first `docker compose up --build` should be considered a smoke test.

Open decisions that need owner input live in `docs/OPEN_QUESTIONS.md`. Among them: final product name, hosting domain, SMTP provider, rate-limit thresholds, and whether to add a paid tier.

## Layout

```
whenly/
├── backend/              Java + Spring Boot 4
├── frontend/             Vue 3 + Vite + Tailwind v4
├── nginx/                Edge reverse proxy
├── docs/                 Architecture, API, security, open questions
├── docker-compose.yml
├── .env.example
└── LICENSE               AGPL-3.0
```

## Contributing

Issues + PRs welcome once a v0.1 tag exists. Until then, the API and schema are unstable.
