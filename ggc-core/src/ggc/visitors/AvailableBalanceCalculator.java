package ggc.visitors;

import ggc.transactions.TransactionVisitor;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;

/**
 * Visitor used to find the available balance.
 */
public class AvailableBalanceCalculator implements TransactionVisitor {
  private double _balance;

  public AvailableBalanceCalculator() {
    this._balance = 0.0;
  }

  /**
   * @return The available balance.
   */
  public double result() {
    return this._balance;
  }

  @Override
  public void visit(Acquisition acquisition) {
    this._balance -= acquisition.value();
  }

  @Override
  public void visit(Sale sale) {
    this._balance += sale.paid() ? sale.realValue() : 0.0;
  }

  @Override
  public void visit(Breakdown breakdown) {
    this._balance += breakdown.payedValue();
  }
}
