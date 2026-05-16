package net.whenly.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class AppUser {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "display_name")
  private String displayName;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "last_login_at")
  private Instant lastLoginAt;

  protected AppUser() {}

  public AppUser(String email, String displayName) {
    this.email = email;
    this.displayName = displayName;
  }

  public UUID getId() { return id; }
  public String getEmail() { return email; }
  public String getDisplayName() { return displayName; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getLastLoginAt() { return lastLoginAt; }

  public void setDisplayName(String displayName) { this.displayName = displayName; }
  public void recordLogin() { this.lastLoginAt = Instant.now(); }
}
