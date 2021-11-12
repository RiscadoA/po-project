package ggc.visitors;

import ggc.transactions.TransactionVisitor;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;

/**
 * Visitor used to update transaction dates where its needed.
 */
public class TransactionDateUpdater implements TransactionVisitor {
  private int _date;

  public TransactionDateUpdater(int date) {
    this._date = date;
  }

  @Override
  public void visit(Acquisition acquisition) {
    // Do nothing
  }

  @Override
  public void visit(Sale sale) {
    sale.updatePaymentValue(this._date);
  }

  @Override
  public void visit(Breakdown breakdown) {
    // Do nothing
  }
}