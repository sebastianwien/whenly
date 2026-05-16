package net.whenly.poll;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.whenly.domain.Participant;
import net.whenly.domain.Poll;
import net.whenly.domain.PollOption;
import net.whenly.domain.Vote;
import net.whenly.domain.VoteValue;
import net.whenly.poll.dto.SuggestResponse;
import net.whenly.repo.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ranks options by votes. The score is yes-count + 0.5 × maybe-count.
 *
 * Tie-breakers, in order:
 *   1. fewer NO votes (less conflict),
 *   2. earlier startAt (give people more notice),
 *   3. lower position (creator-defined order).
 */
@Service
public class SmartSuggestService {

  private final ParticipantRepository participantRepository;

  public SmartSuggestService(ParticipantRepository participantRepository) {
    this.participantRepository = participantRepository;
  }

  @Transactional(readOnly = true)
  public SuggestResponse suggest(Poll poll) {
    List<Participant> participants = participantRepository.findByPollOrderByCreatedAtAsc(poll);

    Map<UUID, int[]> counts = new HashMap<>();
    for (PollOption opt : poll.getOptions()) {
      counts.put(opt.getId(), new int[3]);
    }
    for (Participant p : participants) {
      for (Vote v : p.getVotes()) {
        int[] c = counts.get(v.getOption().getId());
        if (c == null) continue;
        c[indexOf(v.getValue())]++;
      }
    }

    List<SuggestResponse.Ranked> ranking = new ArrayList<>();
    for (PollOption opt : poll.getOptions()) {
      int[] c = counts.get(opt.getId());
      int yes = c[0], maybe = c[1], no = c[2];
      double score = yes + 0.5 * maybe;
      ranking.add(new SuggestResponse.Ranked(
          opt.getId(), opt.getPosition(), opt.getStartAt(), opt.getEndAt(), opt.getLabel(),
          score, yes, maybe, no));
    }

    ranking.sort(Comparator
        .comparingDouble(SuggestResponse.Ranked::score).reversed()
        .thenComparingInt(SuggestResponse.Ranked::noCount)
        .thenComparing(SuggestResponse.Ranked::startAt,
            Comparator.nullsLast(Comparator.naturalOrder()))
        .thenComparingInt(SuggestResponse.Ranked::position));

    if (ranking.isEmpty()) {
      return new SuggestResponse(null, 0, 0, 0, 0, 0, "no_options", List.of());
    }
    SuggestResponse.Ranked best = ranking.get(0);
    int totalParticipants = participants.size();
    String reason = reasonFor(best, totalParticipants, ranking);

    return new SuggestResponse(
        best.optionId(), best.score(),
        best.yesCount(), best.maybeCount(), best.noCount(),
        totalParticipants, reason, ranking);
  }

  private static int indexOf(VoteValue v) {
    return switch (v) {
      case YES -> 0;
      case MAYBE -> 1;
      case NO -> 2;
    };
  }

  private static String reasonFor(SuggestResponse.Ranked best, int total, List<SuggestResponse.Ranked> ranking) {
    if (total == 0) return "no_votes_yet";
    if (best.yesCount() == total) return "everyone_yes";
    if (ranking.size() >= 2 && ranking.get(1).score() == best.score()) return "tie_broken";
    if (best.noCount() == 0) return "no_conflicts";
    return "highest_score";
  }
}
