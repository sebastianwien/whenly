package net.whenly.poll;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.whenly.poll.dto.VoteRequest;
import net.whenly.poll.dto.VoteResponse;

@RestController
@RequestMapping("/api/polls/{publicId}/votes")
public class VoteController {

  private final VoteService voteService;

  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }

  @PostMapping
  public VoteResponse vote(
      @PathVariable String publicId,
      @RequestHeader(value = "X-Participant-Token", required = false) String participantToken,
      @Valid @RequestBody VoteRequest request) {
    return voteService.cast(publicId, request, participantToken);
  }

  @DeleteMapping("/{participantToken}")
  public ResponseEntity<Void> remove(
      @PathVariable String publicId,
      @PathVariable String participantToken) {
    voteService.deleteParticipant(publicId, participantToken);
    return ResponseEntity.noContent().build();
  }
}
