package net.whenly.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = {"participant_id", "option_id"}))
public class Vote {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "participant_id", nullable = false)
  private Participant participant;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "option_id", nullable = false)
  private PollOption option;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 8)
  private VoteValue value;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  protected Vote() {}

  public Vote(Participant participant, PollOption option, VoteValue value) {
    this.participant = participant;
    this.option = option;
    this.value = value;
  }

  public UUID getId() { return id; }
  public Participant getParticipant() { return participant; }
  public PollOption getOption() { return option; }
  public VoteValue getValue() { return value; }
  public Instant getCreatedAt() { return createdAt; }

  public void setValue(VoteValue value) { this.value = value; }
}
