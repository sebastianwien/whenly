package net.whenly.poll.dto;

import java.time.Instant;

public record CreatePollResponse(
    String publicId,
    String adminToken,
    String shareUrl,
    String adminUrl,
    Instant retentionUntil) {}
