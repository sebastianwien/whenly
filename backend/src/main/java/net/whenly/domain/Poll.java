package net.whenly.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "poll")
public class Poll {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "public_id", nullable = false, unique = true, length = 64)
  private String publicId;

  @Column(name = "admin_token", nullable = false, unique = true, length = 64)
  private String adminToken;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 255)
  private String location;

  @Enumerated(EnumType.STRING)
  @Column(name = "poll_type", nullable = false, length = 20)
  private PollType pollType;

  @Column(nullable = false, length = 64)
  private String timezone = "UTC";

  @Column(name = "allow_maybe", nullable = false)
  private boolean allowMaybe = true;

  @Column(name = "allow_multiple", nullable = false)
  private boolean allowMultiple = true;

  @Column(name = "hide_results_until_voted", nullable = false)
  private boolean hideResultsUntilVoted = false;

  @Column(name = "require_participant_name", nullable = false)
  private boolean requireParticipantName = true;

  @Column(name = "finalized_option_id")
  private UUID finalizedOptionId;

  @Column(name = "finalized_at")
  private Instant finalizedAt;

  @Column(name = "closed_at")
  private Instant closedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_user_id")
  private AppUser owner;

  @Column(name = "owner_email", length = 255)
  private String ownerEmail;

  @Column(name = "retention_until", nullable = false)
  private Instant retentionUntil;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();

  @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @OrderBy("position ASC")
  private List<PollOption> options = new ArrayList<>();

  protected Poll() {}

  public Poll(String publicId, String adminToken, String title, PollType pollType, Instant retentionUntil) {
    this.publicId = publicId;
    this.adminToken = adminToken;
    this.title = title;
    this.pollType = pollType;
    this.retentionUntil = retentionUntil;
  }

  public void touch() { this.updatedAt = Instant.now(); }

  public boolean isClosed() { return closedAt != null; }
  public boolean isFinalized() { return finalizedOptionId != null; }

  public UUID getId() { return id; }
  public String getPublicId() { return publicId; }
  public String getAdminToken() { return adminToken; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public String getLocation() { return location; }
  public PollType getPollType() { return pollType; }
  public String getTimezone() { return timezone; }
  public boolean isAllowMaybe() { return allowMaybe; }
  public boolean isAllowMultiple() { return allowMultiple; }
  public boolean isHideResultsUntilVoted() { return hideResultsUntilVoted; }
  public boolean isRequireParticipantName() { return requireParticipantName; }
  public UUID getFinalizedOptionId() { return finalizedOptionId; }
  public Instant getFinalizedAt() { return finalizedAt; }
  public Instant getClosedAt() { return closedAt; }
  public AppUser getOwner() { return owner; }
  public String getOwnerEmail() { return ownerEmail; }
  public Instant getRetentionUntil() { return retentionUntil; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public List<PollOption> getOptions() { return options; }

  public void setTitle(String title) { this.title = title; }
  public void setDescription(String description) { this.description = description; }
  public void setLocation(String location) { this.location = location; }
  public void setTimezone(String timezone) { this.timezone = timezone; }
  public void setAllowMaybe(boolean v) { this.allowMaybe = v; }
  public void setAllowMultiple(boolean v) { this.allowMultiple = v; }
  public void setHideResultsUntilVoted(boolean v) { this.hideResultsUntilVoted = v; }
  public void setRequireParticipantName(boolean v) { this.requireParticipantName = v; }
  public void setOwner(AppUser owner) { this.owner = owner; }
  public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }
  public void setRetentionUntil(Instant retentionUntil) { this.retentionUntil = retentionUntil; }

  public void finalize(UUID optionId) {
    this.finalizedOptionId = optionId;
    this.finalizedAt = Instant.now();
    this.closedAt = Instant.now();
  }

  public void close() { this.closedAt = Instant.now(); }
  public void reopen() { this.closedAt = null; this.finalizedOptionId = null; this.finalizedAt = null; }
}
