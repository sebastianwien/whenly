-- whenly: initial schema
-- Naming: snake_case. All timestamps in UTC. Cascading deletes from poll down.

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE app_user (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    display_name    VARCHAR(120),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_login_at   TIMESTAMPTZ
);

-- Magic-link login tokens. One-shot, expire fast.
CREATE TABLE login_token (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    token_hash      VARCHAR(128) NOT NULL UNIQUE,
    expires_at      TIMESTAMPTZ NOT NULL,
    used_at         TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_login_token_user ON login_token(user_id);

-- A poll: anonymous (no owner_user_id) or owned by an account.
-- public_id is in the share URL. admin_token is the secret edit URL.
CREATE TABLE poll (
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    public_id                   VARCHAR(64) NOT NULL UNIQUE,
    admin_token                 VARCHAR(64) NOT NULL UNIQUE,
    title                       VARCHAR(200) NOT NULL,
    description                 TEXT,
    location                    VARCHAR(255),
    poll_type                   VARCHAR(20) NOT NULL,
    timezone                    VARCHAR(64) NOT NULL DEFAULT 'UTC',
    allow_maybe                 BOOLEAN NOT NULL DEFAULT TRUE,
    allow_multiple              BOOLEAN NOT NULL DEFAULT TRUE,
    hide_results_until_voted    BOOLEAN NOT NULL DEFAULT FALSE,
    require_participant_name    BOOLEAN NOT NULL DEFAULT TRUE,
    finalized_option_id         UUID,
    finalized_at                TIMESTAMPTZ,
    closed_at                   TIMESTAMPTZ,
    owner_user_id               UUID REFERENCES app_user(id) ON DELETE SET NULL,
    owner_email                 VARCHAR(255),
    retention_until             TIMESTAMPTZ NOT NULL,
    created_at                  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at                  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_poll_type CHECK (poll_type IN ('DATE_TIME','DATE_ONLY','GENERIC'))
);
CREATE INDEX idx_poll_owner ON poll(owner_user_id);
CREATE INDEX idx_poll_retention ON poll(retention_until);

CREATE TABLE poll_option (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    poll_id         UUID NOT NULL REFERENCES poll(id) ON DELETE CASCADE,
    position        INTEGER NOT NULL,
    start_at        TIMESTAMPTZ,
    end_at          TIMESTAMPTZ,
    label           VARCHAR(200),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_option_poll ON poll_option(poll_id, position);

ALTER TABLE poll
    ADD CONSTRAINT fk_poll_finalized_option
    FOREIGN KEY (finalized_option_id) REFERENCES poll_option(id) ON DELETE SET NULL;

CREATE TABLE participant (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    poll_id             UUID NOT NULL REFERENCES poll(id) ON DELETE CASCADE,
    participant_token   VARCHAR(64) NOT NULL UNIQUE,
    name                VARCHAR(120) NOT NULL,
    user_id             UUID REFERENCES app_user(id) ON DELETE SET NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_participant_poll ON participant(poll_id);
CREATE INDEX idx_participant_user ON participant(user_id);

CREATE TABLE vote (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    participant_id  UUID NOT NULL REFERENCES participant(id) ON DELETE CASCADE,
    option_id       UUID NOT NULL REFERENCES poll_option(id) ON DELETE CASCADE,
    value           VARCHAR(8) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_vote UNIQUE (participant_id, option_id),
    CONSTRAINT chk_vote_value CHECK (value IN ('YES','MAYBE','NO'))
);
CREATE INDEX idx_vote_option ON vote(option_id);

CREATE TABLE comment (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    poll_id         UUID NOT NULL REFERENCES poll(id) ON DELETE CASCADE,
    option_id       UUID REFERENCES poll_option(id) ON DELETE CASCADE,
    participant_id  UUID REFERENCES participant(id) ON DELETE SET NULL,
    author_name     VARCHAR(120) NOT NULL,
    body            VARCHAR(2000) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_comment_poll ON comment(poll_id, created_at);
CREATE INDEX idx_comment_option ON comment(option_id);

-- Simple IP-based rate limit bucket. Pruned by scheduled job.
CREATE TABLE rate_limit_event (
    id          BIGSERIAL PRIMARY KEY,
    bucket      VARCHAR(40) NOT NULL,
    ip_hash     VARCHAR(64) NOT NULL,
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_rate_limit_lookup ON rate_limit_event(bucket, ip_hash, occurred_at);
