package net.whenly.poll.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddParticipantOptionRequest(
    @NotBlank @Size(max = 200) String label) {}
