package net.whenly.repo;

import java.util.Optional;
import java.util.UUID;
import net.whenly.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
  Optional<AppUser> findByEmailIgnoreCase(String email);
}
