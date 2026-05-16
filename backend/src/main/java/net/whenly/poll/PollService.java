package net.whenly.poll;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import net.whenly.common.ApiException;
import net.whenly.common.TokenGenerator;
import net.whenly.config.WhenlyProperties;
import net.whenly.domain.AppUser;
import net.whenly.domain.Poll;
import net.whenly.domain.PollOption;
import net.whenly.domain.PollType;
import net.whenly.poll.dto.CreatePollRequest;
import net.whenly.poll.dto.CreatePollResponse;
import net.whenly.repo.PollRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PollService {

  private final PollRepository pollRepository;
  private final WhenlyProperties properties;

  public PollService(PollRepository pollRepository, WhenlyProperties properties) {
    this.pollRepository = pollRepository;
    this.properties = properties;
  }

  @Transactional
  public CreatePollResponse create(CreatePollRequest request, AppUser owner) {
    PollType type = request.type();
    validateOptions(type, request.options());

    String publicId = TokenGenerator.generate();
    String adminToken = TokenGenerator.generate();
    Instant retention = computeRetention(request);

    Poll poll = new Poll(publicId, adminToken, request.title().trim(), type, retention);
    poll.setDescription(trimOrNull(request.description()));
    poll.setLocation(trimOrNull(request.location()));
    poll.setTimezone(validateTimezone(request.timezone()));
    if (owner != null) {
      poll.setOwner(owner);
    }
    poll.setOwnerEmail(trimOrNull(request.ownerEmail()));

    CreatePollRequest.Settings s = request.settings();
    if (s != null) {
      if (s.allowMaybe() != null) poll.setAllowMaybe(s.allowMaybe());
      if (s.allowMultiple() != null) poll.setAllowMultiple(s.allowMultiple());
      if (s.hideResultsUntilVoted() != null) poll.setHideResultsUntilVoted(s.hideResultsUntilVoted());
      if (s.requireParticipantName() != null) poll.setRequireParticipantName(s.requireParticipantName());
    }

    int position = 0;
    for (CreatePollRequest.OptionInput o : sortedOptions(request.options(), type)) {
      PollOption option = new PollOption(poll, position++, o.startAt(), o.endAt(), trimOrNull(o.label()));
      poll.getOptions().add(option);
    }

    Poll saved = pollRepository.save(poll);

    String base = properties.baseUrl();
    return new CreatePollResponse(
        saved.getPublicId(),
        saved.getAdminToken(),
        base + "/p/" + saved.getPublicId(),
        base + "/admin/" + saved.getAdminToken(),
        saved.getRetentionUntil());
  }

  @Transactional(readOnly = true)
  public Poll getByPublicId(String publicId) {
    return pollRepository.findByPublicId(publicId)
        .orElseThrow(() -> ApiException.notFound("poll_not_found"));
  }

  @Transactional(readOnly = true)
  public Poll getByAdminToken(String adminToken) {
    return pollRepository.findByAdminToken(adminToken)
        .orElseThrow(() -> ApiException.notFound("poll_not_found"));
  }

  @Transactional
  public void finalize(String adminToken, java.util.UUID optionId) {
    Poll poll = getByAdminToken(adminToken);
    boolean belongs = poll.getOptions().stream().anyMatch(o -> o.getId().equals(optionId));
    if (!belongs) {
      throw ApiException.badRequest("option_not_in_poll", "Option does not belong to this poll");
    }
    poll.finalize(optionId);
    poll.touch();
  }

  @Transactional
  public void reopen(String adminToken) {
    Poll poll = getByAdminToken(adminToken);
    poll.reopen();
    poll.touch();
  }

  @Transactional
  public void delete(String adminToken) {
    Poll poll = getByAdminToken(adminToken);
    pollRepository.delete(poll);
  }

  @Transactional(readOnly = true)
  public List<Poll> listForOwner(AppUser owner) {
    return pollRepository.findByOwnerOrderByCreatedAtDesc(owner);
  }

  private void validateOptions(PollType type, List<CreatePollRequest.OptionInput> options) {
    if (options == null || options.size() < 2) {
      throw ApiException.badRequest("too_few_options", "A poll needs at least two options");
    }
    if (options.size() > 100) {
      throw ApiException.badRequest("too_many_options", "A poll supports at most 100 options");
    }
    for (CreatePollRequest.OptionInput o : options) {
      switch (type) {
        case DATE_TIME, DATE_ONLY -> {
          if (o.startAt() == null) {
            throw ApiException.badRequest("option_missing_start", "Date options need a start timestamp");
          }
          if (o.endAt() != null && o.endAt().isBefore(o.startAt())) {
            throw ApiException.badRequest("option_end_before_start", "End must be after start");
          }
        }
        case GENERIC -> {
          if (o.label() == null || o.label().isBlank()) {
            throw ApiException.badRequest("option_missing_label", "Generic options need a label");
          }
        }
      }
    }
  }

  private List<CreatePollRequest.OptionInput> sortedOptions(List<CreatePollRequest.OptionInput> options, PollType type) {
    return switch (type) {
      case DATE_TIME, DATE_ONLY -> options.stream()
          .sorted(Comparator.comparing(CreatePollRequest.OptionInput::startAt))
          .toList();
      case GENERIC -> options;
    };
  }

  private Instant computeRetention(CreatePollRequest request) {
    int days = Optional.ofNullable(request.retentionDays())
        .orElse(properties.retention().defaultDaysAfterLastOption());
    if (days > properties.retention().maxDays()) {
      throw ApiException.badRequest("retention_too_long",
          "Retention cannot exceed " + properties.retention().maxDays() + " days");
    }
    Instant base = request.options().stream()
        .map(o -> o.endAt() != null ? o.endAt() : o.startAt())
        .filter(java.util.Objects::nonNull)
        .max(Comparator.naturalOrder())
        .orElse(Instant.now());
    return base.plus(days, ChronoUnit.DAYS);
  }

  private String validateTimezone(String tz) {
    if (tz == null || tz.isBlank()) return "UTC";
    try {
      ZoneId.of(tz);
      return tz;
    } catch (Exception e) {
      throw ApiException.badRequest("invalid_timezone", "Unknown timezone: " + tz);
    }
  }

  private String trimOrNull(String s) {
    if (s == null) return null;
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }
}
