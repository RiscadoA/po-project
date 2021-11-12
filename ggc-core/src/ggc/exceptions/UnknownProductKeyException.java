package ggc.exceptions;

/**
 * Class for representing an unknown product key error.
 */
public class UnknownProductKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110141415L;

  /** The requested product key. */
  private String _productKey;

  /**
   * @param productKey Product key.
   */
  public UnknownProductKeyException(String productKey) {
    this._productKey = productKey;
  }

  /**
   * @return The requested product key.
   */
  public String getProductKey() {
    return this._productKey;
  }
}
