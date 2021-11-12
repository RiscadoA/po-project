package ggc.transactions;

/**
 * Visitor interface for transactions.
 */
public interface TransactionVisitor {

  /**
   * Visits an acquisition.
   * 
   * @param acquisition The acquisition to visit.
   */
  public void visit(Acquisition acquisition);

  /**
   * Visits a sale.
   * 
   * @param sale The sale to visit.
   */
  public void visit(Sale sale);

  /**
   * Visits a breakdown.
   * 
   * @param product The breakdown to visit.
   */
  public void visit(Breakdown breakdown);
}