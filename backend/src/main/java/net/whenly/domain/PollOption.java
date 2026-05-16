package net.whenly.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "poll_option")
public class PollOption {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "poll_id", nullable = false)
  private Poll poll;

  @Column(nullable = false)
  private int position;

  @Column(name = "start_at")
  private Instant startAt;

  @Column(name = "end_at")
  private Instant endAt;

  @Column(length = 200)
  private String label;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  protected PollOption() {}

  public PollOption(Poll poll, int position, Instant startAt, Instant endAt, String label) {
    this.poll = poll;
    this.position = position;
    this.startAt = startAt;
    this.endAt = endAt;
    this.label = label;
  }

  public UUID getId() { return id; }
  public Poll getPoll() { return poll; }
  public int getPosition() { return position; }
  public Instant getStartAt() { return startAt; }
  public Instant getEndAt() { return endAt; }
  public String getLabel() { return label; }
  public Instant getCreatedAt() { return createdAt; }

  public void setPosition(int position) { this.position = position; }
  public void setLabel(String label) { this.label = label; }
}
