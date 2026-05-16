# Architecture

## Stack

| Layer | Choice | Why |
|-------|--------|-----|
| Backend | Java 21, Spring Boot 4.0.0, Gradle Kotlin DSL | LTS Java, latest Boot GA (Nov 2025), records + virtual threads available. |
| Persistence | PostgreSQL 17, Flyway migrations, Spring Data JPA | Operationally boring + easy to host. Flyway keeps schema in version control. |
| Frontend | Vue 3.5, TypeScript, Vite 6, Pinia, Vue Router, Vue i18n | Vue is small, ergonomic, and TS-friendly. No SPA framework lock-in. |
| Styling | Tailwind v4 with `@theme` (CSS-based config) | Skips JS config; theme tokens live in `src/styles/main.css` next to component-level classes. |
| Icons | Heroicons (outline) | Matches the bold, 2-px-stroke design language. |
| PWA | `vite-plugin-pwa` with autoUpdate service worker | Offline shell + installable. |
| Reverse proxy | nginx 1.27 (alpine) | Edge gzip, security headers, proxies `/api/*` and serves the SPA. |
| Container runtime | Docker Compose | Single `docker compose up` for the whole stack. |
| Licence | AGPL-3.0 | Strong copyleft for a complete SaaS-style app. |

## Module map

```
whenly/
├── backend/
│   └── src/main/java/net/whenly/
│       ├── WhenlyApplication.java           main + @EnableScheduling
│       ├── config/                          properties + CORS + bean wiring
│       ├── common/                          TokenGenerator, ApiException,
│       │                                    GlobalExceptionHandler
│       ├── domain/                          JPA entities (AppUser, Poll,
│       │                                    PollOption, Participant, Vote,
│       │                                    Comment, LoginToken, enums)
│       ├── repo/                            Spring Data JPA repositories
│       └── poll/                            services + controllers + DTOs
│           ├── PollService                  CRUD, finalise, retention calc
│           ├── VoteService                  cast/edit/withdraw votes
│           ├── CommentService               per-poll / per-option comments
│           ├── SmartSuggestService          ranked options + best slot
│           ├── PollResponseMapper           entity → JSON DTO
│           ├── CleanupScheduler             daily retention purge
│           ├── ExportController             ICS + QR
│           ├── PollController, VoteController, CommentController
│           └── dto/
│       └── resources/
│           ├── application.yml              dev + prod profiles
│           └── db/migration/V1__init.sql    Flyway baseline schema
│
├── frontend/
│   └── src/
│       ├── main.ts, App.vue
│       ├── router/index.ts                  routes + scroll + title
│       ├── i18n/                            de + en
│       ├── styles/main.css                  Tailwind v4 @theme tokens +
│       │                                    component classes
│       ├── api/
│       │   ├── client.ts                    fetch wrapper + ApiError
│       │   └── polls.ts                     typed API surface
│       ├── stores/participantToken.ts       Pinia store, persists tokens
│       │                                    in localStorage
│       ├── lib/format.ts                    Intl date formatting helpers
│       ├── components/
│       │   ├── AppHeader, AppFooter
│       │   └── VoteGrid                     mobile-first three-button vote
│       └── views/
│           ├── LandingView, AboutView, NotFoundView
│           ├── CreatePollView, PollCreatedView
│           ├── PollView, AdminPollView
│
├── nginx/                                   edge reverse proxy
├── docs/                                    architecture + security + open Qs
└── docker-compose.yml
```

## Domain model (high level)

```
AppUser  (optional)
   │
   │ owns 0..* (nullable owner_user_id)
   ▼
Poll  ── retention_until  ── finalized_option_id ──┐
   │                                                │
   │ 1..*                                           │
   ▼                                                │
PollOption  ◀───────────────────────────────────────┘
   ▲
   │ 0..*  Vote (yes/maybe/no) ─── belongs to ───▶ Participant ─── 1 ─── Poll
   │
   │ 0..*  Comment ──────────────── belongs to ───▶ Poll (+ optional Option)
```

Tokens (192 bits, base64url, no padding) are the only authentication primitive in MVP:

- `publicId` — anyone with the share URL.
- `adminToken` — only the creator.
- `participantToken` — the voter, sent in the `X-Participant-Token` header so URLs stay shareable.

## Request flow

```
   ┌────────────┐    https     ┌──────────┐
   │  browser   │ ────────────▶│  nginx   │ ── /api/* ──▶ Spring Boot ── JPA ──▶ Postgres
   │ Vue + PWA  │              │  edge    │
   │            │ ◀──────────  │  proxy   │ ── /*     ──▶ static SPA (nginx)
   └────────────┘              └──────────┘
```

CORS in dev (`localhost:5173` → `localhost:8080`) is allow-listed; in prod the SPA and API share the same origin so CORS is a no-op.

## Smart-suggest scoring

Defined in `SmartSuggestService`:

1. Score per option = `YES count + 0.5 × MAYBE count`.
2. Sort by score descending.
3. Tie-break by fewer NO votes.
4. Then by earlier `startAt`.
5. Then by lower position (creator order).

Reason codes returned alongside the suggestion:

- `everyone_yes` — best option had a YES from every participant.
- `no_conflicts` — best option had zero NO votes.
- `tie_broken` — at least two options tied on score; the rule above picked one.
- `highest_score` — straightforward winner.
- `no_votes_yet` — the poll has participants but no votes.
- `no_options` — defensive; should never fire for a saved poll.

## Retention + GDPR

Polls carry a `retention_until` timestamp computed at creation:

```
retention_until = max(option end_at or start_at) + retentionDays
                  (defaults: retentionDays = 30, cap = 365)
```

`CleanupScheduler` runs daily (default `0 30 3 * * *`) and deletes polls whose `retention_until` is in the past. Cascade deletes wipe options, participants, votes, and comments.

The frontend shows the relative retention date on the poll page so voters know when the data goes away.

## Threading + transactions

- Spring Boot 4 picks up virtual threads automatically on Java 21.
- Read endpoints (`@Transactional(readOnly = true)`) use the `PollResponseMapper`, which fetch-joins `Participant → Vote → PollOption` to avoid N+1 (`ParticipantRepository.findByPollWithVotes`).
- Write endpoints are short, single-transaction; cleanup runs in its own transactional method.

## Observability

- `/actuator/health` and `/actuator/info` are exposed publicly (no secrets).
- Application logs at `INFO` in prod, `DEBUG` in dev.
- Audit logging for admin actions (`finalize`, `delete`) is in `OPEN_QUESTIONS.md`.

## Deployment notes

- The backend Docker image runs as a non-root user, with a `wget` healthcheck against `/actuator/health`.
- `JAVA_OPTS` defaults to `-XX:+UseG1GC -XX:MaxRAMPercentage=75.0`, suitable for a 512 MiB container; tune via env var.
- The SPA build is fingerprinted; the inner nginx layer caches hashed assets for a year and bypasses cache for `index.html`, `sw.js`, and `manifest.webmanifest`.
- The edge nginx applies CSP, X-Frame-Options, Referrer-Policy, and Permissions-Policy headers.
