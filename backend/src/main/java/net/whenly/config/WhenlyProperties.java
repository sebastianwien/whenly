package net.whenly.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "whenly")
public record WhenlyProperties(
    String baseUrl,
    Retention retention,
    RateLimit rateLimit,
    Mail mail) {

  public record Retention(int defaultDaysAfterLastOption, int maxDays, String cleanupCron) {}

  public record RateLimit(int pollsPerIpPerHour, int votesPerIpPerMinute) {}

  public record Mail(boolean enabled, String from) {}
}
