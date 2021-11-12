package ggc.exceptions;

/**
 * Class for representing a duplicate partner key error.
 */
public class DuplicatePartnerKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110141413L;

  /** The duplicate partner key. */
  private String _partnerKey;

  /**
   * @param partnerKey Partner key.
   */
  public DuplicatePartnerKeyException(String partnerKey) {
    this._partnerKey = partnerKey;
  }

  /**
   * @return The duplicate partner key.
   */
  public String getPartnerKey() {
    return this._partnerKey;
  }
}
