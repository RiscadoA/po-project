package ggc.visitors;

import java.util.Collection;
import java.util.ArrayList;

import ggc.transactions.Transaction;
import ggc.transactions.TransactionVisitor;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;

/**
 * Visitor used to collect paid transactions.
 */
public class PaidTransactionsCollector implements TransactionVisitor {

  private Collection<Transaction> _result = new ArrayList<>();

  public Collection<Transaction> result() {
    return this._result;
  }

  @Override
  public void visit(Acquisition acquisition) {
    // Do nothing
  }

  @Override
  public void visit(Sale sale) {
    if (sale.paid())
      this._result.add(sale);
  }

  @Override
  public void visit(Breakdown breakdown) {
    // Do nothing
  }
}