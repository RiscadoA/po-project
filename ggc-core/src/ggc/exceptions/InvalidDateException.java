package ggc.exceptions;

/**
 * Class for representing an invalid date error.
 */
public class InvalidDateException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110141410L;

  /** Bad date to report. */
  private int _date;

  /**
   * @param date Bad date to report.
   */
  public InvalidDateException(int date) {
    this._date = date;
  }

  /**
   * @return Bad date to report.
   */
  public int getDate() {
    return this._date;
  }
}
