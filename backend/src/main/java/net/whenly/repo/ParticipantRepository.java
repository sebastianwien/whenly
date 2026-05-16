package net.whenly.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.whenly.domain.Participant;
import net.whenly.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
  Optional<Participant> findByParticipantToken(String token);
  List<Participant> findByPollOrderByCreatedAtAsc(Poll poll);
}
