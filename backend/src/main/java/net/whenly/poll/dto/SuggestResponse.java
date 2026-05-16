package net.whenly.poll.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SuggestResponse(
    UUID bestOptionId,
    double bestScore,
    int yesCount,
    int maybeCount,
    int noCount,
    int participantCount,
    String reason,
    List<Ranked> ranking) {

  public record Ranked(
      UUID optionId,
      int position,
      Instant startAt,
      Instant endAt,
      String label,
      double score,
      int yesCount,
      int maybeCount,
      int noCount) {}
}
