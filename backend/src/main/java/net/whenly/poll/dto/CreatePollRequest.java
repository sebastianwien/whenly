package net.whenly.poll.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import net.whenly.domain.PollType;

public record CreatePollRequest(
    @NotBlank @Size(max = 200) String title,
    @Size(max = 5000) String description,
    @Size(max = 255) String location,
    @NotNull PollType type,
    @Size(max = 64) String timezone,
    @Valid @NotNull @Size(min = 2, max = 100) List<OptionInput> options,
    @Valid Settings settings,
    @Email @Size(max = 255) String ownerEmail,
    @Min(0) Integer retentionDays) {

  public record OptionInput(
      Instant startAt,
      Instant endAt,
      @Size(max = 200) String label) {}

  public record Settings(
      Boolean allowMaybe,
      Boolean allowMultiple,
      Boolean hideResultsUntilVoted,
      Boolean requireParticipantName) {}
}
