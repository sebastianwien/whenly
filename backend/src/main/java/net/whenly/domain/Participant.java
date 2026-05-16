package net.whenly.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "participant")
public class Participant {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "poll_id", nullable = false)
  private Poll poll;

  @Column(name = "participant_token", nullable = false, unique = true, length = 64)
  private String participantToken;

  @Column(nullable = false, length = 120)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private AppUser user;

  @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Vote> votes = new ArrayList<>();

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();

  protected Participant() {}

  public Participant(Poll poll, String participantToken, String name) {
    this.poll = poll;
    this.participantToken = participantToken;
    this.name = name;
  }

  public void touch() { this.updatedAt = Instant.now(); }

  public UUID getId() { return id; }
  public Poll getPoll() { return poll; }
  public String getParticipantToken() { return participantToken; }
  public String getName() { return name; }
  public AppUser getUser() { return user; }
  public List<Vote> getVotes() { return votes; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }

  public void setName(String name) { this.name = name; }
  public void setUser(AppUser user) { this.user = user; }
}
