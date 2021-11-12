package ggc.transactions;

import java.io.Serializable;

import ggc.products.Product;
import ggc.partners.Partner;

/**
 * Implements a transaction.
 */
public abstract class Transaction implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111051317L;

  /** The transaction's id. */
  private int _id;

  /** The transaction's date. */
  private int _date;

  /** The amount of the product involved in this transaction. */
  private int _amount;

  /** The product involved in this transaction. */
  private Product _product;

  /** The partner involved in the transaction. */
  private Partner _partner;

  /**
   * @param id The transaction's id.
   * @param date The transaction's date.
   * @param amount The amount of the product involved in this transaction.
   * @param product The product involved in this transaction.
   * @param partner The partner involved in the transaction.
   */
  public Transaction(int id, int date, int amount, Product product, Partner partner) {
    this._id = id;
    this._date = date;
    this._amount = amount;
    this._product = product;
    this._partner = partner;
  }

  /**
   * Returns the id of this transaction.
   *
   * @return Transaction id.
   */
  public int id() {
    return this._id;
  }

  /**
   * Returns the date of this transaction.
   *
   * @return Transaction date.
   */
  public int date() {
    return this._date;
  }

  /**
   * Returns the amount of the transactioned product.
   *
   * @return Amount of product transactioned.
   */
  public int amount() {
    return this._amount;
  }

  /**
   * Returns the product that is being transactioned.
   *
   * @return Product.
   */
  public Product product() {
    return this._product;
  }

  /**
   * Returns the partner involved in the transaction.
   *
   * @return Partner.
   */
  public Partner partner() {
    return this._partner;
  }

  /**
   * Accepts a visitor.
   * 
   * @param visitor Visitor.
   */
  public abstract void accept(TransactionVisitor visitor);

  @Override
  public String toString() {
    return this._id + "|" + this._partner.key() + "|" + this._product.key() + "|" + this._amount;
  }
}
