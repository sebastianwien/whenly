# whenly

> Working title — easy to rename later. Open-source group scheduling without the dark patterns.

A privacy-friendly alternative to Doodle and Nuudel. Create polls, vote on dates or options, find the best slot. No ads, no tracking, no account required.

## Status

Early development. See `docs/` for architecture and open questions.

## Stack

- **Backend**: Java 21, Spring Boot 4, PostgreSQL, Flyway
- **Frontend**: Vue 3, TypeScript, Vite, Pinia, Tailwind CSS
- **Infra**: Docker, nginx
- **License**: AGPL-3.0

## Quick start

```bash
docker compose up
```

Then open http://localhost:8080.

## Development

See `docs/local-development.md`.

## Why another scheduling tool?

- **Privacy-first**: GDPR-compliant retention, automatic deletion, no third-party trackers.
- **No login wall**: Create and vote on polls without an account.
- **Smart suggestions**: The app picks the best slot for you — no manual counting.
- **If-need-be voting**: Three-state voting (yes / maybe / no), unlike Nuudel.
- **Mobile-first**: Built for thumbs, not just desktops.
- **Self-hostable**: Docker compose, your data on your server.

## Contributing

Issues and PRs welcome once we hit v0.1.
