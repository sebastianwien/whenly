# Security model

## Threat model

whenly is a low-friction polling app: a creator shares a URL, participants vote, the creator picks a slot. There are no high-value assets (no payments, no PII beyond names + optional emails), but two classes of misuse matter:

1. **Unauthorised reads** — outsiders peeking at private polls.
2. **Unauthorised writes** — outsiders mutating a poll, votes, or comments belonging to someone else.

The defence is capability URLs: long random tokens act as bearer credentials. There are no usernames/passwords for MVP.

## Capability tokens

| Token | Bytes | Purpose | Disclosure risk |
|-------|-------|---------|-----------------|
| `publicId`         | 24 (192 bit) | Share URL `/p/{publicId}` | Anyone who has it can view + vote |
| `adminToken`       | 24 (192 bit) | Admin URL `/admin/{adminToken}` | Anyone who has it can edit, finalise, delete |
| `participantToken` | 24 (192 bit) | Header `X-Participant-Token` | Anyone who has it can edit that participant's votes |

192 bits of CSPRNG entropy makes online brute force infeasible. URL-safe base64 keeps URLs tidy.

## Access control

Every admin-scoped controller method resolves the poll **by adminToken** — never by publicId + a separate auth check, which would risk a confused-deputy. Likewise, every vote/comment mutation that takes a participant token re-checks that the participant belongs to the poll in question.

`hideResultsUntilVoted` hides the participant list and votes from non-admin viewers until they themselves have voted (admin always sees them).

## OWASP top 10 checklist

- **A01 Broken Access Control** — capability-token model; explicit poll-ownership checks on every mutation.
- **A02 Cryptographic Failures** — only secrets handled are tokens (random) and (future) login tokens (stored as hash, never plain). No passwords.
- **A03 Injection** — JPA parameterised queries throughout, no raw JDBC, no shell exec, no v-html on user content.
- **A04 Insecure Design** — capability-URL model documented; admin/share URLs visually distinct in UI to discourage mis-sharing.
- **A05 Security Misconfiguration** — CORS allow-list env-driven; Actuator exposes only `health`/`info`; error responses scrub stack traces.
- **A06 Vulnerable Components** — Spring Boot 4.0.0 GA, Flyway, biweekly, ZXing all current; renovate/dependabot to be wired (see OPEN_QUESTIONS).
- **A07 Identification & Authentication Failures** — N/A for MVP (no accounts). When magic-link auth ships, tokens are one-shot + 15 min TTL.
- **A08 Software & Data Integrity Failures** — N/A.
- **A09 Security Logging & Monitoring Failures** — DEBUG logs in dev profile; INFO in prod. Audit logging for admin finalize/delete to be added (tracked in OPEN_QUESTIONS).
- **A10 SSRF** — no outbound calls driven by user input.

## GDPR posture

- No third-party trackers, no analytics SDK.
- Optional fields: `ownerEmail`, participant `name`.
- Comments are user content — escaping happens at render (Vue `{{ }}`), never via `v-html`.
- Polls auto-delete after `retentionUntil` (default = last option + 30 days, max 365). `CleanupScheduler` runs daily.
- Manual deletion via admin URL is immediate and cascades to options, participants, votes, and comments.
- No IP addresses are stored; the rate-limit bucket (when activated) uses an HMAC of the IP, not the IP itself.

## Rate limiting

`rate_limit_event` table exists; the enforcing filter is **not** yet wired (see OPEN_QUESTIONS — thresholds need owner decision before enabling).

## Reporting issues

Email security@whenly.example (placeholder — replace once domain is decided).
