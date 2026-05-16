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
@Table(name = "comment")
public class Comment {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "poll_id", nullable = false)
  private Poll poll;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "option_id")
  private PollOption option;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "participant_id")
  private Participant participant;

  @Column(name = "author_name", nullable = false, length = 120)
  private String authorName;

  @Column(nullable = false, length = 2000)
  private String body;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt = Instant.now();

  protected Comment() {}

  public Comment(Poll poll, PollOption option, Participant participant, String authorName, String body) {
    this.poll = poll;
    this.option = option;
    this.participant = participant;
    this.authorName = authorName;
    this.body = body;
  }

  public UUID getId() { return id; }
  public Poll getPoll() { return poll; }
  public PollOption getOption() { return option; }
  public Participant getParticipant() { return participant; }
  public String getAuthorName() { return authorName; }
  public String getBody() { return body; }
  public Instant getCreatedAt() { return createdAt; }
}
