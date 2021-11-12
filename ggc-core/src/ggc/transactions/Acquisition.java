package ggc.transactions;

import ggc.products.Product;

import ggc.partners.Partner;

/**
 * Implements an acquisition transaction.
 */
public class Acquisition extends Transaction {

  /* The value of the transaction. */
  private double _value;

  /**
   * @param id The acquisition's id.
   * @param date The acquisition's date.
   * @param amount The amount of the product acquired in this acquisition.
   * @param unitValue The value of each unit of product acquired.
   * @param product The product that was acquired.
   * @param partner The partner which supplied the product.
   */
  public Acquisition(int id, int date, int amount, Product product, Partner partner, double unitValue) {
    super(id, date, amount, product, partner);
    this._value = unitValue * amount;
  }

  /**
   * Returns the value of the transaction.
   *
   * @return The value of the transaction.
   */
  public double value() {
    return this._value;
  }

  @Override
  public void accept(TransactionVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "COMPRA|" + super.toString() + "|" + Math.round(this._value) + "|" + this.date();
  }
}
