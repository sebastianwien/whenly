package net.whenly.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TokenGeneratorTest {

  @Test
  void generates_url_safe_strings() {
    String token = TokenGenerator.generate();
    assertThat(token).matches("[A-Za-z0-9_-]+");
  }

  @Test
  void produces_unique_tokens_in_volume() {
    Set<String> seen = new HashSet<>();
    for (int i = 0; i < 10_000; i++) {
      assertThat(seen.add(TokenGenerator.generate()))
          .as("token collision on iteration %d", i)
          .isTrue();
    }
  }
}
