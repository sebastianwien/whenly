package net.whenly.repo;

import java.util.UUID;
import net.whenly.domain.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollOptionRepository extends JpaRepository<PollOption, UUID> {}
