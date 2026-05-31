-- Add OPEN poll type: participants can suggest options during voting

ALTER TABLE poll DROP CONSTRAINT chk_poll_type;
ALTER TABLE poll ADD CONSTRAINT chk_poll_type
    CHECK (poll_type IN ('DATE_TIME','DATE_ONLY','GENERIC','OPEN'));
