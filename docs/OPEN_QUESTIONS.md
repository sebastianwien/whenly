# Open Questions

Decisions deferred to the project owner. Each entry has a default the code assumes today; flip it when you decide.

## Naming

- **Final product name** — working title `whenly`. Anything from `meetly`, `slotvote`, `wann.io` is on the table. Rename touches: GitHub repo, `package.json`, `settings.gradle`, README, Docker image names, nginx server_name, manifest.json, domain config.

## Hosting / Domain

- **Production domain** — pick before launch. Affects: nginx `server_name`, CORS allow-list, OG metadata, manifest `start_url`.
- **Hosting provider** — Hetzner Cloud (like ev-monitor) is the default. Could also be Fly.io, Render, or a Pi at home for self-host demo.

## Email

- **Transactional sender** — SMTP credentials TBD. Used for: optional vote-confirmation links, admin reminders, "your poll closes in 24h". Default behavior with no SMTP configured: emails are silently skipped (logged).
- **Sender domain + DKIM/SPF/DMARC** — required if we send notifications.

## Premium / monetisation

- Owner said "primarily free". Open: is there ever a paid tier (custom branding for orgs, longer retention, analytics)? Default assumption: 100% free, donation link in footer.

## Retention defaults

- Default auto-delete window for polls = **30 days after the last poll date** (or 30 days after creation if no dates). Confirm or adjust.
- Maximum allowed user-chosen retention = **365 days**. Confirm.

## i18n scope

- MVP ships **de + en**. Confirm whether `nb`/`sv` (ev-monitor locales) or `fr`/`es` should follow.

## Data export

- GDPR right-to-export: should creators be able to download their poll + all votes as JSON/CSV? Default: yes, link on admin page. Confirm.

## Smart-suggest tie-breakers

- When multiple slots tie on yes-votes, current rule is: prefer (1) fewer "no" votes, then (2) earlier date, then (3) creation order. Confirm.
- Should "maybe" count as 0.5? Currently yes — confirm.

## Auth (later)

- Optional accounts use email + magic link by default. Open whether to add OAuth (Google/GitHub/Apple) later. Not in MVP.

## Anti-abuse

- Rate-limit defaults: 10 poll creates per IP per hour, 60 votes per IP per minute. Confirm thresholds.
- Captcha (hCaptcha/Turnstile): off by default, env-flag to enable. Pick provider if turned on.
