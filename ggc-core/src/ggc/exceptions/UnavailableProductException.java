package ggc.exceptions;

/**
 * Class for representing an unavailable product error.
 */
public class UnavailableProductException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110141417L;

  /** The requested product key. */
  private String _productKey;

  /** The requested amount of product. */
  private int _requested;

  /** The amount of product available. */
  private int _available;

  /**
   * @param productKey Product key.
   */
  public UnavailableProductException(String productKey, int requested, int available) {
    this._productKey = productKey;
    this._requested = requested;
    this._available = available;
  }

  /**
   * @return The requested product key.
   */
  public String getProductKey() {
    return this._productKey;
  }

  /**
   * @return The requested amount of product.
   */
  public int getRequested() {
    return this._requested;
  }

  /**
   * @return The amount of product available.
   */
  public int getAvailable() {
    return this._available;
  }
}
