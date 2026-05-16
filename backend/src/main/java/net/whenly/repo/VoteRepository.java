package net.whenly.repo;

import java.util.List;
import java.util.UUID;
import net.whenly.domain.Participant;
import net.whenly.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, UUID> {
  List<Vote> findByParticipant(Participant participant);
}
