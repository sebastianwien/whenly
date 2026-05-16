package net.whenly.poll;

import java.time.Instant;
import java.util.List;
import net.whenly.domain.Poll;
import net.whenly.repo.PollRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * GDPR: polls disappear once their retention window expires.
 *
 * Default cron `0 30 3 * * *` runs at 03:30 daily. Override with
 * `whenly.retention.cleanup-cron` if needed.
 */
@Component
public class CleanupScheduler {

  private static final Logger log = LoggerFactory.getLogger(CleanupScheduler.class);

  private final PollRepository pollRepository;

  public CleanupScheduler(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  @Scheduled(cron = "${whenly.retention.cleanup-cron}")
  @Transactional
  public void purgeExpiredPolls() {
    Instant now = Instant.now();
    List<Poll> expired = pollRepository.findByRetentionUntilBefore(now);
    if (expired.isEmpty()) return;
    log.info("Retention cleanup: deleting {} expired poll(s)", expired.size());
    pollRepository.deleteAll(expired);
  }
}
