package net.whenly.poll;

import jakarta.validation.Valid;
import java.util.UUID;
import net.whenly.domain.Comment;
import net.whenly.poll.dto.CommentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/polls")
public class CommentController {

  private final CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  public record CommentCreated(java.util.UUID id, String authorName, String body, java.time.Instant createdAt, java.util.UUID optionId) {}

  @PostMapping("/{publicId}/comments")
  public ResponseEntity<CommentCreated> add(
      @PathVariable String publicId,
      @Valid @RequestBody CommentRequest request) {
    Comment c = commentService.add(publicId, request);
    UUID optionId = c.getOption() != null ? c.getOption().getId() : null;
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CommentCreated(c.getId(), c.getAuthorName(), c.getBody(), c.getCreatedAt(), optionId));
  }

  @DeleteMapping("/admin/{adminToken}/comments/{commentId}")
  public ResponseEntity<Void> delete(
      @PathVariable String adminToken,
      @PathVariable UUID commentId) {
    commentService.delete(adminToken, commentId);
    return ResponseEntity.noContent().build();
  }
}
