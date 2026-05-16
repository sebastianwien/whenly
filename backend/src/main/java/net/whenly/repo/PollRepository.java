package net.whenly.repo;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.whenly.domain.AppUser;
import net.whenly.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, UUID> {
  Optional<Poll> findByPublicId(String publicId);
  Optional<Poll> findByAdminToken(String adminToken);
  List<Poll> findByOwnerOrderByCreatedAtDesc(AppUser owner);
  List<Poll> findByRetentionUntilBefore(Instant cutoff);
}
