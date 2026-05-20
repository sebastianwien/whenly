package net.whenly.poll;

import jakarta.validation.Valid;
import net.whenly.domain.Poll;
import org.springframework.transaction.annotation.Transactional;
import net.whenly.poll.dto.CreatePollRequest;
import net.whenly.poll.dto.CreatePollResponse;
import net.whenly.poll.dto.FinalizeRequest;
import net.whenly.poll.dto.PollResponse;
import net.whenly.poll.dto.SuggestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/polls")
public class PollController {

  private final PollService pollService;
  private final PollResponseMapper mapper;
  private final SmartSuggestService suggestService;

  public PollController(
      PollService pollService,
      PollResponseMapper mapper,
      SmartSuggestService suggestService) {
    this.pollService = pollService;
    this.mapper = mapper;
    this.suggestService = suggestService;
  }

  @PostMapping
  public ResponseEntity<CreatePollResponse> create(@Valid @RequestBody CreatePollRequest request) {
    CreatePollResponse res = pollService.create(request, null);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @Transactional(readOnly = true)
  @GetMapping("/{publicId}")
  public PollResponse get(
      @PathVariable String publicId,
      @RequestHeader(value = "X-Participant-Token", required = false) String participantToken) {
    Poll poll = pollService.getByPublicId(publicId);
    return mapper.toResponse(poll, false, participantToken);
  }

  @Transactional(readOnly = true)
  @GetMapping("/{publicId}/suggest")
  public SuggestResponse suggest(@PathVariable String publicId) {
    Poll poll = pollService.getByPublicId(publicId);
    return suggestService.suggest(poll);
  }

  @Transactional(readOnly = true)
  @GetMapping("/admin/{adminToken}")
  public PollResponse getAsAdmin(@PathVariable String adminToken) {
    Poll poll = pollService.getByAdminToken(adminToken);
    return mapper.toResponse(poll, true, null);
  }

  @Transactional
  @PostMapping("/admin/{adminToken}/finalize")
  public PollResponse finalize(
      @PathVariable String adminToken,
      @Valid @RequestBody FinalizeRequest request) {
    pollService.finalize(adminToken, request.optionId());
    Poll poll = pollService.getByAdminToken(adminToken);
    return mapper.toResponse(poll, true, null);
  }

  @Transactional
  @PostMapping("/admin/{adminToken}/reopen")
  public PollResponse reopen(@PathVariable String adminToken) {
    pollService.reopen(adminToken);
    Poll poll = pollService.getByAdminToken(adminToken);
    return mapper.toResponse(poll, true, null);
  }

  @DeleteMapping("/admin/{adminToken}")
  public ResponseEntity<Void> delete(@PathVariable String adminToken) {
    pollService.delete(adminToken);
    return ResponseEntity.noContent().build();
  }
}
