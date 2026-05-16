package net.whenly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class VoteValueTest {

  @Test
  void weight_yes_is_1() {
    assertThat(VoteValue.YES.weight()).isEqualTo(1.0);
  }

  @Test
  void weight_maybe_is_half() {
    assertThat(VoteValue.MAYBE.weight()).isEqualTo(0.5);
  }

  @Test
  void weight_no_is_0() {
    assertThat(VoteValue.NO.weight()).isEqualTo(0.0);
  }
}
