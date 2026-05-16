package net.whenly.poll.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FinalizeRequest(@NotNull UUID optionId) {}
