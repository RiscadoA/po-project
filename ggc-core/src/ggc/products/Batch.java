package ggc.products;

import java.io.Serializable;
import java.util.Comparator;

import ggc.partners.Partner;

/**
 * Represents a batch of a certain product.
 */
public class Batch implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110151910L;

  /** The product stored in this batch. */
  private Product _product;

  /** The partner associated to this batch. */
  private Partner _partner;

  /** Number of units of the product in this batch. */
  private int _amount;

  /** The price of each unit in this batch. */
  private double _price;

  /** Compares two batches by their price. */
  public static final Comparator<Batch> PRICE_COMPARATOR = new PriceComparator();

  /** Compares two batches by their product, partner, price and amount. */
  public static final Comparator<Batch> LIST_COMPARATOR = new ListComparator();

  private static class PriceComparator implements Comparator<Batch>, Serializable {
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202110251449L;

    public int compare(Batch lhs, Batch rhs) {
      return Double.compare(lhs._price, rhs._price);
    }
  }

  private static class ListComparator implements Comparator<Batch>, Serializable {
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202110251450L;

    public int compare(Batch lhs, Batch rhs) {
      if (!lhs._product.key().equals(rhs._product.key()))
        return lhs._product.key().compareTo(rhs._product.key());
      else if (!lhs._partner.key().equals(rhs._partner.key()))
        return lhs._partner.key().compareTo(rhs._partner.key());
      else if (lhs._price != rhs._price)
        return Double.compare(lhs._price, rhs._price);
      else
        return Integer.compare(lhs._amount, rhs._amount);
    }
  }

  /**
   * @param product Product stored in the batch.
   * @param partner Partner associated to the batch.
   * @param amount Number of units of the product.
   * @param price Price of each unit.
   */
  public Batch(Product product, Partner partner, int amount, double price) {
    this._product = product;
    this._partner = partner;
    this._amount = amount;
    this._price = price;
  }

  /**
   * Returns the product stored in this batch.
   * 
   * @return Product stored in this batch.
   */
  public Product product() {
    return this._product;
  }

  /**
   * Returns the partner associated to this batch.
   * 
   * @return Partner associated to this batch.
   */
  public Partner partner() {
    return this._partner;
  }

  /**
   * Returns the number of units of the product in this batch.
   * 
   * @return Number of units in this batch.
   */
  public int amount() {
    return this._amount;
  }

  /**
   * Returns the price of each unit in this batch.
   * 
   * @return Price of each unit in this batch.
   */
  public double price() {
    return this._price;
  }

  /**
   * Sets the number of units on this batch.
   * 
   * @param amount Number of units.
   */
  public void setAmount(int amount)  {
    this._amount = amount;
  }

  @Override
  public String toString() {
    return this._product.key() + "|" + this._partner.key() + "|" + Math.round(this._price) + "|" + this._amount;
  };
}
