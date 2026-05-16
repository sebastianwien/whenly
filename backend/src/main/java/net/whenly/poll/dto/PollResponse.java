package net.whenly.poll.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import net.whenly.domain.PollType;
import net.whenly.domain.VoteValue;

public record PollResponse(
    String publicId,
    String title,
    String description,
    String location,
    PollType type,
    String timezone,
    Settings settings,
    List<Option> options,
    List<Participant> participants,
    int participantCount,
    boolean resultsHidden,
    List<Comment> comments,
    UUID finalizedOptionId,
    Instant finalizedAt,
    Instant closedAt,
    Instant createdAt,
    Instant retentionUntil,
    Admin admin) {

  public record Settings(
      boolean allowMaybe,
      boolean allowMultiple,
      boolean hideResultsUntilVoted,
      boolean requireParticipantName) {}

  public record Option(
      UUID id,
      int position,
      Instant startAt,
      Instant endAt,
      String label) {}

  public record Participant(
      UUID id,
      String name,
      List<VoteEntry> votes,
      Instant updatedAt) {}

  public record VoteEntry(UUID optionId, VoteValue value) {}

  public record Comment(
      UUID id,
      UUID optionId,
      String authorName,
      String body,
      Instant createdAt) {}

  /** Admin block is only populated when the request used the admin token. */
  public record Admin(
      String adminToken,
      String ownerEmail) {}
}
