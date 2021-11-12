package ggc.transactions;

import ggc.partners.Partner;
import ggc.products.Product;

/**
 * Implements a sale transaction.
 */
public class Sale extends Transaction {

  /** Deadline for this sale's payment. */
  private int _deadline;

  /** The sale's value with no fees/discounts aplied. */
  private double _baseValue;

  /** The sale's value with fees/discounts applied */
  private double _realValue;

  /** The payment date for this sale. */
  private int _paymentDate;

  /** Default constructor */
  public Sale(int id, int date, Product product, int amount, Partner partner, int deadline, double baseValue) {
    super(id, date, amount, product, partner);
    this._deadline = deadline;
    this._baseValue = baseValue;
    this._paymentDate = -1;
    this.updatePaymentValue(date);
  }

  /**
   * Returns the deadline for this transaction's payment.
   *
   * @return Deadline for this transaction's payment.
   */
  public int deadline() {
    return this._deadline;
  }

  /**
   * Returns the base value to be paid for this transaction, without fees.
   *
   * @return Base value to be paid for this transaction, without fees.
   */
  public double baseValue() {
    return this._baseValue;
  }

  /**
   * Returns the value paid for this transaction, or the value that must be paid
   * if it hasn't been paid yet.
   *
   * @return Value paid for this transaction.
   */
  public double realValue() {
    return this._realValue;
  }

  /**
   * Applies the fees/discounts on the base price of the sale for the current
   * date.
   * 
   * @param date Date.
   */
  public void updatePaymentValue(int date) {
    // Only change it if the transaction hasn't already been paid
    if (!this.paid())
      this._realValue = this.partner().getSalePaymentPrice(this.product(), date - this._deadline, this._baseValue);
  }

  /**
   * Returns the payment date of this transaction.
   *
   * @return Payment date for this transaction. Returns -1 if the sale hasn't been
   *         paid yet.
   */
  public int paymentDate() {
    return this._paymentDate;
  }

  /**
   * Pays the sale.
   * 
   * @param date Current date.
   * @return The value paid for this transaction. If the transaction was already
   *         paid, returns 0.
   */
  public double pay(int date) {
    if (!this.paid()) {
      this._realValue = this.partner().registerSalePayment(this.product(), date - this._deadline, this._baseValue);
      this._paymentDate = date;
      return this._realValue;
    } else
      return 0.0;
  }

  public boolean paid() {
    return this.paymentDate() != -1;
  }

  @Override
  public void accept(TransactionVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "VENDA|" + super.toString() + "|" + Math.round(this._baseValue) + "|" + Math.round(this._realValue) + "|"
        + this._deadline + (!this.paid() ? "" : "|" + this._paymentDate);
  }
}
