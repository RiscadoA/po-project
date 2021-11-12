package ggc.visitors;

import ggc.transactions.TransactionVisitor;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;

/**
 * Visitor used to find the accounting balance.
 */
public class AccountingBalanceCalculator implements TransactionVisitor {
  private double _balance;

  public AccountingBalanceCalculator() {
    this._balance = 0.0;
  }

  /**
   * @return The accounting balance.
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
    this._balance += sale.realValue();
  }

  @Override
  public void visit(Breakdown breakdown) {
    this._balance += breakdown.payedValue();
  }
}