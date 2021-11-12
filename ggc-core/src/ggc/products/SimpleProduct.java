package ggc.products;

import ggc.exceptions.UnavailableProductException;

/**
 * Represents a simple product.
 */
public class SimpleProduct extends Product {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110151907L;

  /**
   * @param id Product ID.
   */
  public SimpleProduct(String id) {
    super(id);
  }
  
  @Override
  public double sell(int amount) throws UnavailableProductException {
    return this.take(amount);
  }

  @Override
  public int getPeriodN() {
    return 5;
  }

  @Override
  public <T extends Throwable> void accept(ProductVisitor<T> visitor) throws T {
    visitor.visit(this);
  }
}
