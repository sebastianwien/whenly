package net.whenly.domain;

public enum VoteValue {
  YES,
  MAYBE,
  NO;

  public double weight() {
    return switch (this) {
      case YES -> 1.0;
      case MAYBE -> 0.5;
      case NO -> 0.0;
    };
  }
}
