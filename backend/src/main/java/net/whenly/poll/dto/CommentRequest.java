package net.whenly.poll.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CommentRequest(
    @NotBlank @Size(max = 120) String authorName,
    @NotBlank @Size(max = 2000) String body,
    UUID optionId,
    String participantToken) {}
