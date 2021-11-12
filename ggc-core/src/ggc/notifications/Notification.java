package ggc.notifications;

import java.io.Serializable;

import ggc.products.Product;

/**
 * A notification is a message sent when a new batch of a product is received.
 */
public abstract class Notification implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111101133L;

  /** The product which caused this notification. */
  private Product _product;

  /** The price of the product on the new batch. */
  private double _price;

  /**
   * @param product The product which caused this notification
   * @param price The price of the product on the new batch
   */
  public Notification(Product product, double price) {
    this._product = product;
    this._price = price;
  }

  /**
   * @return The product which caused this notification
   */
  public Product product() {
    return this._product;
  }

  /**
   * @return The price of the product on the new batch
   */
  public double price() {
    return this._price;
  }

  @Override
  public String toString() {
    return this._product.key() + "|" + Math.round(this._price);
  }
}
