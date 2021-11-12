package ggc.visitors;

import java.util.ArrayList;
import java.util.Collection;

import ggc.transactions.Transaction;
import ggc.transactions.TransactionVisitor;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;

/**
 * Visitor used to collect acquisitions.
 */
public class AcquisitionCollector implements TransactionVisitor {

  private Collection<Transaction> _result = new ArrayList<>();

  public Collection<Transaction> result() {
    return this._result;
  }

  @Override
  public void visit(Acquisition acquisition) {
    this._result.add(acquisition);
  }

  @Override
  public void visit(Sale sale) {
    // Do nothing
  }

  @Override
  public void visit(Breakdown breakdown) {
    // Do nothing
  }
}