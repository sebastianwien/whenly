package net.whenly.poll;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.whenly.domain.Comment;
import net.whenly.domain.Participant;
import net.whenly.domain.Poll;
import net.whenly.domain.PollOption;
import net.whenly.domain.Vote;
import net.whenly.poll.dto.PollResponse;
import net.whenly.repo.CommentRepository;
import net.whenly.repo.ParticipantRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PollResponseMapper {

  private final ParticipantRepository participantRepository;
  private final CommentRepository commentRepository;

  public PollResponseMapper(
      ParticipantRepository participantRepository,
      CommentRepository commentRepository) {
    this.participantRepository = participantRepository;
    this.commentRepository = commentRepository;
  }

  @Transactional(readOnly = true)
  public PollResponse toResponse(Poll poll, boolean asAdmin, String viewerParticipantToken) {
    List<Participant> participants = participantRepository.findByPollWithVotes(poll);
    List<Comment> comments = commentRepository.findByPollOrderByCreatedAtAsc(poll);

    java.util.UUID viewerParticipantId = null;
    if (viewerParticipantToken != null) {
      for (Participant p : participants) {
        if (p.getParticipantToken().equals(viewerParticipantToken)) {
          viewerParticipantId = p.getId();
          break;
        }
      }
    }
    boolean viewerHasVoted = viewerParticipantId != null;
    boolean hideResults = !asAdmin
        && poll.isHideResultsUntilVoted()
        && !viewerHasVoted;

    List<PollResponse.Option> options = poll.getOptions().stream()
        .sorted((a, b) -> Integer.compare(a.getPosition(), b.getPosition()))
        .map(this::mapOption)
        .toList();

    List<PollResponse.Participant> participantDtos = hideResults
        ? List.of()
        : participants.stream().map(this::mapParticipant).toList();

    List<PollResponse.Comment> commentDtos = comments.stream()
        .map(this::mapComment)
        .toList();

    PollResponse.Admin admin = asAdmin
        ? new PollResponse.Admin(poll.getAdminToken(), poll.getOwnerEmail())
        : null;

    return new PollResponse(
        poll.getPublicId(),
        poll.getTitle(),
        poll.getDescription(),
        poll.getLocation(),
        poll.getPollType(),
        poll.getTimezone(),
        new PollResponse.Settings(
            poll.isAllowMaybe(),
            poll.isAllowMultiple(),
            poll.isHideResultsUntilVoted(),
            poll.isRequireParticipantName()),
        options,
        participantDtos,
        participants.size(),
        hideResults,
        commentDtos,
        poll.getFinalizedOptionId(),
        poll.getFinalizedAt(),
        poll.getClosedAt(),
        poll.getCreatedAt(),
        poll.getRetentionUntil(),
        viewerParticipantId,
        admin);
  }

  private PollResponse.Option mapOption(PollOption o) {
    return new PollResponse.Option(o.getId(), o.getPosition(), o.getStartAt(), o.getEndAt(), o.getLabel());
  }

  private PollResponse.Participant mapParticipant(Participant p) {
    List<PollResponse.VoteEntry> entries = p.getVotes().stream()
        .map(this::mapVote)
        .filter(Objects::nonNull)
        .toList();
    return new PollResponse.Participant(p.getId(), p.getName(), entries, p.getUpdatedAt());
  }

  private PollResponse.VoteEntry mapVote(Vote v) {
    UUID optionId = v.getOption() != null ? v.getOption().getId() : null;
    if (optionId == null) return null;
    return new PollResponse.VoteEntry(optionId, v.getValue());
  }

  private PollResponse.Comment mapComment(Comment c) {
    UUID optionId = c.getOption() != null ? c.getOption().getId() : null;
    return new PollResponse.Comment(c.getId(), optionId, c.getAuthorName(), c.getBody(), c.getCreatedAt());
  }
}
