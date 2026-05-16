package net.whenly.poll.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import net.whenly.domain.VoteValue;

public record VoteRequest(
    @Size(max = 120) String name,
    @Valid @NotEmpty List<Entry> votes) {

  public record Entry(@NotNull UUID optionId, @NotNull VoteValue value) {}
}
