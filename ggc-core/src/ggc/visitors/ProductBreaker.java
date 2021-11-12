package ggc.visitors;

import ggc.products.ProductVisitor;
import ggc.products.Recipe;
import ggc.products.Product;
import ggc.products.SimpleProduct;
import ggc.products.DerivateProduct;

import ggc.transactions.Breakdown;

import ggc.partners.Partner;

import ggc.exceptions.UnavailableProductException;

/**
 * Breaks down a derivate product into its components.
 */
public class ProductBreaker implements ProductVisitor<UnavailableProductException> {
  /** Partner that requested the breakdown. */
  private Partner _partner;

  /** Amount of the product to break. */
  private int _amount;

  /** Breakdown value. */
  private double _value;

  /** Breakdown components. */
  private Breakdown.Component[] _components;

  public ProductBreaker(Partner partner, int amount) {
    this._partner = partner;
    this._amount = amount;
    this._value = 0.0;
  }

  public Breakdown.Component[] breakdownComponents() {
    return this._components;
  }

  public double breakdownValue() {
    return this._value;
  }

  @Override
  public void visit(SimpleProduct product) throws UnavailableProductException {
    // Do nothing
  }

  @Override
  public void visit(DerivateProduct product) throws UnavailableProductException {
    this._value = product.take(this._amount);

    Recipe.Component[] rComponents = product.recipe().components();
    this._components = new Breakdown.Component[rComponents.length];

    for (int i = 0; i < rComponents.length; i++) {
      int amount = rComponents[i].amount() * this._amount;
      double value = this.productValue(rComponents[i].product());
      
      this._components[i] = new Breakdown.Component(rComponents[i].product(), amount, value);
      rComponents[i].product().addBatch(this._partner, amount, value);
      this._value -= value * amount;
    }
  }

  private double productValue(Product product) {
    return product.stock() != 0 ? product.cheapestBatch().price() : product.maxPrice();
  }
}
