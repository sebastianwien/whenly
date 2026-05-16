package net.whenly.repo;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import net.whenly.domain.LoginToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LoginTokenRepository extends JpaRepository<LoginToken, UUID> {
  Optional<LoginToken> findByTokenHash(String tokenHash);

  @Modifying
  @Query("delete from LoginToken t where t.expiresAt < :cutoff")
  int deleteExpired(Instant cutoff);
}
