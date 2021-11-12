package ggc.products;

import ggc.exceptions.UnavailableProductException;
import ggc.partners.Partner;

/**
 * Represents a product which is made out of other products.
 */
public class DerivateProduct extends Product {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110151909L;

  /** Product recipe. */
  private Recipe _recipe;

  /**
   * @param id Product ID.
   * @param Recipe Product recipe.
   */
  public DerivateProduct(String id, Recipe recipe) {
    super(id);
    this._recipe = recipe;
  }

  /**
   * Gets the product recipe.
   * 
   * @return Product recipe.
   */
  public Recipe recipe() {
    return this._recipe;
  }

  /**
   * Breaks down a certain amount of this product. If the product type doesn't
   * support this operation, nothing is done.
   * 
   * @param partner Partner which requested the breakdown.
   * @param amount Amount of product to breakdown.
   * @return The value produced (or destroyed) by the breakdown: the the value of
   *         the produced components - the value of the original product.
   */
  public double breakdown(Partner partner, int amount) throws UnavailableProductException {
    double oldValue = this.take(amount);
    double newValue = 0.0;

    Recipe.Component[] components = this._recipe.components();
    for (int i = 0; i < components.length; i++) {
      Product product = components[i].product();
      Batch cheapestBatch = product.cheapestBatch();

      // Get value of the new batch of the component
      double value;
      if (cheapestBatch == null)
        value = product.maxPrice();
      else
        value = cheapestBatch.price();

      product.addBatch(partner, amount, value);
      newValue += value;
    }

    return newValue - oldValue;
  }

  @Override
  public double sell(int amount) throws UnavailableProductException {
    if (this.stock() < amount) {
      double value = 0.0;
      int amountLeft = amount - this.stock();

      for (Recipe.Component component : this._recipe.components())
        value += component.product().sell(amountLeft * component.amount());

      value *= 1.0 + this._recipe.aggravation();
      this.addPrice(value / amountLeft);
      return value + this.take(this.stock());
    } else
      return this.take(amount);
  }

  @Override
  public int getPeriodN() {
    return 3;
  }

  @Override
  public <T extends Throwable> void accept(ProductVisitor<T> visitor) throws T {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return super.toString() + "|" + this._recipe.toString();
  }
}