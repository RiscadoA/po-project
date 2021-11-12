package ggc.visitors;

import ggc.transactions.TransactionVisitor;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;

/**
 * Visitor used to pay transactions.
 */
public class TransactionPayer implements TransactionVisitor {
  private int _date;
  private double _value;

  public TransactionPayer(int date) {
    this._date = date;
    this._value = 0.0;
  }

  /**
   * @return The value of the transaction paid.
   */
  public double value() {
    return this._value;
  }

  @Override
  public void visit(Acquisition acquisition) {
    // Do nothing
  }

  @Override
  public void visit(Sale sale) {
    this._value = sale.pay(this._date);
  }

  @Override
  public void visit(Breakdown breakdown) {
    // Do nothing
  }
}