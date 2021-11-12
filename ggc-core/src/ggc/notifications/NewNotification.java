package ggc.notifications;

import ggc.products.Product;

/**
 * A 'NEW' notification.
 */
public class NewNotification extends Notification {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111101133L;

  /**
   * @param product The product which caused this notification
   * @param price The price of the product on the new batch
   */
  public NewNotification(Product product, double price) {
    super(product, price);
  }

  @Override
  public String toString() {
    return "NEW|" + super.toString();
  }
}
