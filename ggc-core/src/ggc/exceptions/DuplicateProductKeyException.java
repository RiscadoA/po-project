package ggc.exceptions;

/**
 * Class for representing a duplicate product key error.
 */
public class DuplicateProductKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110141414L;

  /** The duplicate product key. */
  private String _productKey;

  /**
   * @param productKey Product key.
   */
  public DuplicateProductKeyException(String productKey) {
    this._productKey = productKey;
  }

  /**
   * @return The duplicate product key.
   */
  public String getProductKey() {
    return this._productKey;
  }
}
