package ggc.exceptions;

/**
 * Class for representing an unknown partner key error.
 */
public class UnknownPartnerKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110141414L;

  /** The requested partner key. */
  private String _partnerKey;

  /**
   * @param partnerKey Partner key.
   */
  public UnknownPartnerKeyException(String partnerKey) {
    this._partnerKey = partnerKey;
  }

  /**
   * @return The requested partner key.
   */
  public String getPartnerKey() {
    return this._partnerKey;
  }
}
