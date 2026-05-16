package net.whenly.poll;

import java.util.UUID;
import net.whenly.common.ApiException;
import net.whenly.domain.Comment;
import net.whenly.domain.Participant;
import net.whenly.domain.Poll;
import net.whenly.domain.PollOption;
import net.whenly.poll.dto.CommentRequest;
import net.whenly.repo.CommentRepository;
import net.whenly.repo.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final ParticipantRepository participantRepository;
  private final PollService pollService;

  public CommentService(
      CommentRepository commentRepository,
      ParticipantRepository participantRepository,
      PollService pollService) {
    this.commentRepository = commentRepository;
    this.participantRepository = participantRepository;
    this.pollService = pollService;
  }

  @Transactional
  public Comment add(String publicId, CommentRequest request) {
    Poll poll = pollService.getByPublicId(publicId);
    if (poll.isClosed()) {
      throw ApiException.conflict("poll_closed", "Comments are closed");
    }

    PollOption option = null;
    if (request.optionId() != null) {
      option = poll.getOptions().stream()
          .filter(o -> o.getId().equals(request.optionId()))
          .findFirst()
          .orElseThrow(() -> ApiException.badRequest("option_not_in_poll",
              "Comment refers to an unknown option"));
    }

    Participant participant = null;
    if (request.participantToken() != null && !request.participantToken().isBlank()) {
      participant = participantRepository.findByParticipantToken(request.participantToken())
          .filter(p -> p.getPoll().getId().equals(poll.getId()))
          .orElse(null);
    }

    Comment comment = new Comment(
        poll, option, participant,
        request.authorName().trim(),
        request.body().trim());
    return commentRepository.save(comment);
  }

  @Transactional
  public void delete(String adminToken, UUID commentId) {
    Poll poll = pollService.getByAdminToken(adminToken);
    Comment c = commentRepository.findById(commentId)
        .orElseThrow(() -> ApiException.notFound("comment_not_found"));
    if (!c.getPoll().getId().equals(poll.getId())) {
      throw ApiException.forbidden("comment_poll_mismatch");
    }
    commentRepository.delete(c);
  }
}
