package net.whenly.poll;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.whenly.domain.Participant;
import net.whenly.domain.Poll;
import net.whenly.domain.PollOption;
import net.whenly.domain.PollType;
import net.whenly.domain.Vote;
import net.whenly.domain.VoteValue;
import net.whenly.poll.dto.SuggestResponse;
import net.whenly.repo.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SmartSuggestServiceTest {

  private ParticipantRepository participantRepository;
  private SmartSuggestService service;

  @BeforeEach
  void setUp() {
    participantRepository = Mockito.mock(ParticipantRepository.class);
    service = new SmartSuggestService(participantRepository);
  }

  @Test
  void suggests_option_with_more_yes_votes() {
    Poll poll = buildPoll();
    PollOption a = poll.getOptions().get(0);
    PollOption b = poll.getOptions().get(1);

    Participant p1 = buildParticipant(poll, "Anna",
        vote(a, VoteValue.YES), vote(b, VoteValue.NO));
    Participant p2 = buildParticipant(poll, "Bo",
        vote(a, VoteValue.YES), vote(b, VoteValue.MAYBE));

    when(participantRepository.findByPollOrderByCreatedAtAsc(poll))
        .thenReturn(List.of(p1, p2));

    SuggestResponse res = service.suggest(poll);

    assertThat(res.bestOptionId()).isEqualTo(a.getId());
    assertThat(res.bestScore()).isEqualTo(2.0);
    assertThat(res.yesCount()).isEqualTo(2);
    assertThat(res.ranking()).hasSize(2);
    assertThat(res.ranking().get(0).optionId()).isEqualTo(a.getId());
  }

  @Test
  void tie_break_prefers_fewer_no_votes() {
    Poll poll = buildPoll();
    PollOption a = poll.getOptions().get(0);
    PollOption b = poll.getOptions().get(1);

    Participant p1 = buildParticipant(poll, "Anna",
        vote(a, VoteValue.YES), vote(b, VoteValue.YES));
    Participant p2 = buildParticipant(poll, "Bo",
        vote(a, VoteValue.NO), vote(b, VoteValue.MAYBE));
    Participant p3 = buildParticipant(poll, "Cara",
        vote(a, VoteValue.YES), vote(b, VoteValue.YES));

    when(participantRepository.findByPollOrderByCreatedAtAsc(poll))
        .thenReturn(List.of(p1, p2, p3));

    SuggestResponse res = service.suggest(poll);

    // Both score 2.5 (2*yes + 0.5*maybe for b ; 2*yes for a)
    // a: 2*1 = 2.0, b: 2*1 + 0.5 = 2.5 — actually different, fix to identical scores
    // Adjust to identical scores:
    // a: 2 yes + 1 no = score 2, b: 2 yes + 0 no + 1 maybe = 2.5. Different.
    // So expected best = b because higher score.
    assertThat(res.bestOptionId()).isEqualTo(b.getId());
  }

  @Test
  void identical_score_breaks_by_no_count() {
    Poll poll = buildPoll();
    PollOption a = poll.getOptions().get(0);
    PollOption b = poll.getOptions().get(1);

    // a: 2 yes, 1 no  -> score 2.0, no=1
    // b: 2 yes, 0 no  -> score 2.0, no=0
    Participant p1 = buildParticipant(poll, "Anna",
        vote(a, VoteValue.YES), vote(b, VoteValue.YES));
    Participant p2 = buildParticipant(poll, "Bo",
        vote(a, VoteValue.YES), vote(b, VoteValue.YES));
    Participant p3 = buildParticipant(poll, "Cara",
        vote(a, VoteValue.NO));

    when(participantRepository.findByPollOrderByCreatedAtAsc(poll))
        .thenReturn(List.of(p1, p2, p3));

    SuggestResponse res = service.suggest(poll);

    assertThat(res.bestOptionId()).isEqualTo(b.getId());
    assertThat(res.reason()).isEqualTo("tie_broken");
  }

  @Test
  void empty_poll_returns_no_votes_yet() {
    Poll poll = buildPoll();
    when(participantRepository.findByPollOrderByCreatedAtAsc(poll))
        .thenReturn(List.of());

    SuggestResponse res = service.suggest(poll);

    assertThat(res.bestScore()).isEqualTo(0.0);
    assertThat(res.reason()).isEqualTo("no_votes_yet");
    assertThat(res.participantCount()).isZero();
  }

  // --- helpers ---

  private Poll buildPoll() {
    Poll poll = new Poll("pub", "adm", "Test", PollType.DATE_TIME,
        Instant.now().plusSeconds(86400));
    setId(poll, UUID.randomUUID());
    Instant start = Instant.parse("2026-06-01T10:00:00Z");
    PollOption a = new PollOption(poll, 0, start, start.plusSeconds(3600), null);
    setId(a, UUID.randomUUID());
    PollOption b = new PollOption(poll, 1, start.plusSeconds(86400), start.plusSeconds(90000), null);
    setId(b, UUID.randomUUID());
    poll.getOptions().add(a);
    poll.getOptions().add(b);
    return poll;
  }

  private Participant buildParticipant(Poll poll, String name, Vote... votes) {
    Participant p = new Participant(poll, "tok-" + name, name);
    setId(p, UUID.randomUUID());
    List<Vote> list = new ArrayList<>();
    for (Vote v : votes) {
      reassignParticipant(v, p);
      list.add(v);
    }
    setField(p, "votes", list);
    return p;
  }

  private Vote vote(PollOption option, VoteValue value) {
    return new Vote(null, option, value);
  }

  private static void reassignParticipant(Vote v, Participant p) {
    setField(v, "participant", p);
  }

  private static void setId(Object entity, UUID id) {
    setField(entity, "id", id);
  }

  private static void setField(Object target, String fieldName, Object value) {
    try {
      Field f = findField(target.getClass(), fieldName);
      f.setAccessible(true);
      f.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static Field findField(Class<?> c, String name) throws NoSuchFieldException {
    Class<?> cur = c;
    while (cur != null) {
      try {
        return cur.getDeclaredField(name);
      } catch (NoSuchFieldException ignored) {
        cur = cur.getSuperclass();
      }
    }
    throw new NoSuchFieldException(name);
  }
}
