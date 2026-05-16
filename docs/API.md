# HTTP API

Base URL in dev: `http://localhost:8080`. In prod, behind nginx at `/api/`.

All endpoints accept and return JSON unless noted. Errors use:

```json
{
  "code": "snake_case_code",
  "message": "Human-readable",
  "errors": [{ "field": "title", "message": "must not be blank" }]
}
```

Authentication uses capability tokens (see SECURITY.md). Submit the participant token in the `X-Participant-Token` header.

## Polls

### `POST /api/polls`

Create a poll. **No auth required.**

```json
{
  "title": "Sunday brunch",
  "description": "Bringing the dog. Vegan-friendly please.",
  "location": "Café Klatsch · Berlin",
  "type": "DATE_TIME",
  "timezone": "Europe/Berlin",
  "options": [
    { "startAt": "2026-06-01T09:00:00Z", "endAt": "2026-06-01T11:00:00Z" },
    { "startAt": "2026-06-01T11:00:00Z", "endAt": "2026-06-01T13:00:00Z" }
  ],
  "settings": {
    "allowMaybe": true,
    "allowMultiple": true,
    "hideResultsUntilVoted": false,
    "requireParticipantName": true
  },
  "ownerEmail": null,
  "retentionDays": 30
}
```

Returns `201` with:

```json
{
  "publicId": "Ab9_…",
  "adminToken": "Xz7-…",
  "shareUrl": "https://whenly.example/p/Ab9_…",
  "adminUrl": "https://whenly.example/admin/Xz7-…",
  "retentionUntil": "2026-07-01T11:00:00Z"
}
```

### `GET /api/polls/{publicId}`

Public poll view. Optional `X-Participant-Token` header pre-fills the viewer's previous votes via `viewerParticipantId` in the response. When `hideResultsUntilVoted` is true and the viewer has no vote, the `participants` array is empty and `resultsHidden` is `true` (the count is still returned as `participantCount`).

### `GET /api/polls/{publicId}/suggest`

Ranked options + best slot. Always available; safe to poll.

### `GET /api/polls/admin/{adminToken}`

Admin view. Includes the `admin.adminToken` and `admin.ownerEmail` fields.

### `POST /api/polls/admin/{adminToken}/finalize`

Body `{ "optionId": "…" }`. Picks the winner, closes the poll, makes `/ics` downloadable.

### `POST /api/polls/admin/{adminToken}/reopen`

Reverses the above.

### `DELETE /api/polls/admin/{adminToken}`

Immediate cascade delete. Returns `204`.

## Votes

### `POST /api/polls/{publicId}/votes`

Cast or update votes. If `X-Participant-Token` is present and valid for this poll, the existing participant is updated. Otherwise a new one is created.

```json
{
  "name": "Anna",
  "votes": [
    { "optionId": "uuid-a", "value": "YES" },
    { "optionId": "uuid-b", "value": "MAYBE" }
  ]
}
```

Returns:

```json
{ "participantToken": "qP4_…", "name": "Anna" }
```

Save that token in `localStorage` — it is your only way back into your votes.

### `DELETE /api/polls/{publicId}/votes/{participantToken}`

Withdraw participant + all their votes. Returns `204`.

## Comments

### `POST /api/polls/{publicId}/comments`

```json
{
  "authorName": "Anna",
  "body": "11:00 only if we relocate to Friedrichshain.",
  "optionId": "uuid-a",
  "participantToken": "qP4_…"
}
```

`optionId` is optional — `null` means a poll-level comment.

### `DELETE /api/polls/admin/{adminToken}/comments/{commentId}`

Admin-only. Returns `204`.

## Export

### `GET /api/polls/{publicId}/ics`

Returns `text/calendar`. `409` if the poll is not yet finalised.

### `GET /api/polls/{publicId}/qr`

Returns `image/png`, 512×512.

## Health

### `GET /actuator/health`

`{"status":"UP"}` on success. Used by the Docker healthcheck.

## Status codes

- `400` validation error (`code: validation_failed` with `errors[]`)
- `403` token mismatch or wrong owner
- `404` unknown poll / participant / comment
- `409` poll closed or not finalised
- `500` unhandled error (no stack trace leaked)
