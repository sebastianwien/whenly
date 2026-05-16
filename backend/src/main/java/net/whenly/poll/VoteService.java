package net.whenly.poll;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.whenly.common.ApiException;
import net.whenly.common.TokenGenerator;
import net.whenly.domain.Participant;
import net.whenly.domain.Poll;
import net.whenly.domain.PollOption;
import net.whenly.domain.Vote;
import net.whenly.domain.VoteValue;
import net.whenly.poll.dto.VoteRequest;
import net.whenly.poll.dto.VoteResponse;
import net.whenly.repo.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

  private final ParticipantRepository participantRepository;
  private final PollService pollService;

  public VoteService(ParticipantRepository participantRepository, PollService pollService) {
    this.participantRepository = participantRepository;
    this.pollService = pollService;
  }

  @Transactional
  public VoteResponse cast(String publicId, VoteRequest request, String existingParticipantToken) {
    Poll poll = pollService.getByPublicId(publicId);
    if (poll.isClosed()) {
      throw ApiException.conflict("poll_closed", "This poll is closed and no longer accepting votes");
    }

    String name = request.name() == null ? "" : request.name().trim();
    if (poll.isRequireParticipantName() && name.isEmpty()) {
      throw ApiException.badRequest("name_required", "Please enter your name to vote");
    }
    if (name.isEmpty()) {
      name = "Anonymous";
    }

    Participant participant;
    boolean created = false;
    if (existingParticipantToken != null && !existingParticipantToken.isBlank()) {
      participant = participantRepository.findByParticipantTokenAndPoll(existingParticipantToken, poll)
          .orElseThrow(() -> ApiException.forbidden("invalid_participant_token"));
      participant.setName(name);
      participant.touch();
    } else {
      participant = new Participant(poll, TokenGenerator.generate(), name);
      participantRepository.save(participant);
      created = true;
    }

    if (!poll.isAllowMultiple()) {
      long yesCount = request.votes().stream().filter(e -> e.value() == VoteValue.YES).count();
      if (yesCount > 1) {
        throw ApiException.badRequest("single_choice_only",
            "This poll allows only one 'yes' vote per participant");
      }
    }

    Map<UUID, PollOption> optionsById = new HashMap<>();
    for (PollOption o : poll.getOptions()) {
      optionsById.put(o.getId(), o);
    }

    Map<UUID, Vote> existingByOption = new HashMap<>();
    for (Vote v : participant.getVotes()) {
      existingByOption.put(v.getOption().getId(), v);
    }

    for (VoteRequest.Entry entry : request.votes()) {
      PollOption option = optionsById.get(entry.optionId());
      if (option == null) {
        throw ApiException.badRequest("option_not_in_poll", "Vote refers to an unknown option");
      }
      VoteValue value = entry.value();
      if (value == VoteValue.MAYBE && !poll.isAllowMaybe()) {
        throw ApiException.badRequest("maybe_not_allowed",
            "This poll does not allow 'maybe' votes");
      }
      Vote existing = existingByOption.get(option.getId());
      if (existing != null) {
        existing.setValue(value);
      } else {
        participant.getVotes().add(new Vote(participant, option, value));
      }
    }

    if (!created) {
      participantRepository.save(participant);
    }

    return new VoteResponse(participant.getParticipantToken(), participant.getName());
  }

  @Transactional
  public void deleteParticipant(String publicId, String participantToken) {
    Poll poll = pollService.getByPublicId(publicId);
    Participant participant = participantRepository.findByParticipantToken(participantToken)
        .orElseThrow(() -> ApiException.notFound("participant_not_found"));
    if (!participant.getPoll().getId().equals(poll.getId())) {
      throw ApiException.forbidden("participant_poll_mismatch");
    }
    participantRepository.delete(participant);
  }
}
