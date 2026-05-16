# Features

## Today (MVP, v0.1)

### Poll creation

- **No account required** — anyone can create a poll. Optional `ownerEmail` for a future "your polls" page.
- **Three poll types**
  - `DATE_TIME` — date + start time + optional end time.
  - `DATE_ONLY` — whole-day options.
  - `GENERIC` — free-text options (e.g. "pizza", "sushi").
- **2 to 100 options**, server-validated.
- **Configurable retention** — default 30 days after the last option, max 365.
- **Per-poll settings**
  - `allowMaybe` — three-state voting (Doodle parity, fixes a Nuudel gap).
  - `allowMultiple` — when off, only one "yes" per participant.
  - `hideResultsUntilVoted` — voters see other votes only after they cast their own.
  - `requireParticipantName` — when off, anonymous votes are allowed.
- **Timezone-aware** — voter's browser-detected timezone is used for display.

### Voting

- Three-state pills (✓ / ~ / ✗) sized for thumbs (40 px).
- One click per cell, no drag, no double-tap.
- Smart auto-edit: clicking any pill enters edit mode; "Save" commits.
- Withdraw vote with one click + confirm.
- Identification by participant token stored in `localStorage`; reload returns your previous votes pre-filled.

### Per-option comments

- Comments can target the poll or a specific option.
- Helpful for "I could do 11:00 if we move it to Friday" without polluting the global thread.

### Smart suggestion

- Banner showing the best slot, with reason codes (everyone-yes, no-conflicts, tie-broken, highest-score).
- Updates every 10 s while the poll is open.

### Admin actions

- Secret `/admin/{token}` URL — only the creator should hold it.
- Finalise a winner (cascades to closing the poll, generates ICS).
- Reopen / change winner.
- Delete the poll and all votes immediately.

### Share + export

- Public share URL.
- 512×512 QR code (PNG) embedded on the poll page and the post-creation screen.
- `.ics` download once the poll is finalised.

### Privacy + GDPR

- No third-party trackers, no analytics SDKs.
- Auto-delete after the retention window.
- Manual deletion via admin token.
- No IP addresses stored; the rate-limit table (when wired) uses HMAC-of-IP.

### Internationalisation

- German + English from day one. Locale persists in `localStorage`.

### Progressive Web App

- Installable, offline-capable shell via `vite-plugin-pwa`.
- Service worker auto-updates without prompting.

### Dark mode

- `prefers-color-scheme` driven. No toggle yet (kept the UI lean).

### Design language

- Warm sand / deep ink / terracotta / sage palette — intentionally not the generic Tailwind look.
- Fraunces serif for headings + Inter for body.
- Bold 2 px borders, skeuomorphic press shadows, subtle paper noise on desktop.

## Differentiators vs. Doodle and Nuudel

| Feature | Doodle | Nuudel | whenly |
|---------|--------|--------|--------|
| No account required | optional | yes | **yes** |
| Maybe vote | yes | no | **yes** |
| Smart suggestion | premium | no | **free** |
| Auto-delete | premium | yes (60 d) | **configurable, 30 d default** |
| Per-option comments | no | no | **yes** |
| ICS + QR | yes | partial | **yes, free** |
| Open source | no | yes (AGPL) | **yes (AGPL)** |
| Self-hostable | no | yes | **yes** |
| German + English UI | yes | yes | **yes** |
| Trackers / ads | yes | no | **none** |

## Next on the list (deliberately out of MVP)

| Feature | State | Notes |
|---------|-------|-------|
| Magic-link login | DB ready (`app_user`, `login_token`) | Decide SMTP provider (`OPEN_QUESTIONS.md`). |
| "Your polls" dashboard | depends on auth | Needs account first. |
| Rate limiting filter | table exists | Pick thresholds; default in app.yml is 10 polls / IP / hour. |
| Audit log for admin actions | not started | Plain table + INFO logs; OPEN_QUESTIONS. |
| Email reminders ("poll closes in 24 h") | not started | Needs SMTP. |
| OAuth (Google / GitHub / Apple) | not started | Likely never — magic links suffice. |
| Captcha (Turnstile / hCaptcha) | env flag scaffold | Decide provider. |
| `nb` / `sv` / `fr` locales | not started | i18n machinery in place. |
| GDPR JSON export of a poll | not started | Easy add: serialise admin response. |
| WebPush notifications | not started | PWA infra already there. |

## Known limitations

- `allowMultiple` toggle is plumbed end-to-end on the server but not yet exposed in the create-poll UI (defaults to true). One commit to add.
- Comments are plain text. Markdown / mentions are intentionally out of scope.
- No bulk import of dates (CSV / `.ics`). Could be a useful add.
- `Poll.timezone` is captured but not exposed as a creator-side setting — defaults to the browser timezone. Same: one commit to add the field.
