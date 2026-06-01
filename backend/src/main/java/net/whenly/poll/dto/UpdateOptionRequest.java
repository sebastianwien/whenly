package net.whenly.poll.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateOptionRequest(
    @NotBlank @Size(max = 200) String label
) {}
