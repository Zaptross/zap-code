package api.database.enums;

/**
 * Based on the work by The Elite Gentleman
 */
public enum JobStatus {
  PENDING("PENDING"),
  RUNNING("RUNNING"),
  COMPLETED("COMPLETED"),
  FAILED("FAILED"),
  CANCELED("CANCELED"),
  TIMEOUT("TIMEOUT"),
  UNKNOWN("UNKNOWN");

  private final String text;

  JobStatus(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}
