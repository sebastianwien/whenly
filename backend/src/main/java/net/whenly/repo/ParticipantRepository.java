package net.whenly.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.whenly.domain.Participant;
import net.whenly.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
  Optional<Participant> findByParticipantToken(String token);

  /**
   * Composite lookup — defends against confused-deputy attacks where a token
   * from poll A is presented along with poll B's publicId in the URL.
   */
  Optional<Participant> findByParticipantTokenAndPoll(String token, Poll poll);

  List<Participant> findByPollOrderByCreatedAtAsc(Poll poll);

  /**
   * Fetch participants + their votes + the voted option in one round-trip,
   * to avoid N+1 when assembling a poll response.
   */
  @Query("select distinct p from Participant p "
      + "left join fetch p.votes v "
      + "left join fetch v.option "
      + "where p.poll = :poll "
      + "order by p.createdAt asc")
  List<Participant> findByPollWithVotes(Poll poll);
}
