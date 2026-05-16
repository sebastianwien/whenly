package net.whenly.common;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * URL-safe random tokens used for poll public id, admin token, and participant cookies.
 * 192 bits of entropy: more than enough to be unguessable, short enough for a tidy URL.
 */
public final class TokenGenerator {

  private static final SecureRandom RNG = new SecureRandom();
  private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

  private TokenGenerator() {}

  public static String generate() {
    byte[] bytes = new byte[24];
    RNG.nextBytes(bytes);
    return ENCODER.encodeToString(bytes);
  }
}
