package ggc.partners;

import java.io.Serializable;

import ggc.products.Product;
import ggc.notifications.Observer;

/**
 * Class Partner implements a warehouse partner.
 */
public class Partner extends Observer {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110151910L;

  /** Partner key. */
  private String _key;

  /** Partner name. */
  private String _name;

  /** Partner address. */
  private String _address;

  /** Partner rank. */
  private Rank _rank;

  /** Total number of points. */
  private double _points;

  /** Total value of acquisitions. */
  private double _acquisitionsValue;

  /** Total value of sales. */
  private double _salesValue;

  /** Total value of paid sales. */
  private double _paidSalesValues;

  /** Partner rank. */
  public abstract class Rank implements Serializable {
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202111101024L;

    /**
     * Called when the partner executes a payment.
     * 
     * @param product The product that was paid.
     * @param delay Delay of the payment (can be negative).
     * @param basePrice Base price of the payment.
     * @return The price paid with fees/discounts.
     */
    public abstract double getSalePaymentPrice(Product product, int delay, double basePrice);

    /**
     * Called when the partner executes a sale payment.
     * 
     * @param product The product that was sold.
     * @param delay Delay of the sale payment (can be negative).
     * @param basePrice Base price of the sale payment.
     * @return The price paid with fees/discounts.
     */
    public abstract double registerSalePayment(Product product, int delay, double basePrice);

    /**
     * Called when the partner's points change.
     */
    public abstract void onPointsChange();

    /**
     * Sets the rank of the partner.
     * 
     * @param rank New rank.
     */
    protected void setRank(Rank rank) {
      Partner.this._rank = rank;
    }

    /**
     * @return The points of the partner.
     */
    public double points() {
      return Partner.this._points;
    }

    /**
     * Sets the points of the partner.
     * 
     * @param points New points.
     */
    protected void setPoints(double points) {
      Partner.this._points = points;
    }

    /**
     * Gets the partner which holds this rank.
     * 
     * @return Partner which holds this rank.
     */
    protected Partner partner() {
      return Partner.this;
    }
  }

  /**
   * @param key Partner key.
   * @param name Partner name.
   * @param address Partner address.
   */
  public Partner(String key, String name, String address) {
    this._key = key;
    this._name = name;
    this._address = address;
    this._rank = new NormalRank(this);
    this._points = 0.0;
    this._acquisitionsValue = 0.0;
    this._salesValue = 0.0;
    this._paidSalesValues = 0.0;
  }

  /**
   * Returns the partner's key.
   * 
   * @return Partner key.
   */
  public String key() {
    return this._key;
  }

  /**
   * Returns the partner's name.
   * 
   * @return Partner name.
   */
  public String name() {
    return this._name;
  }

  /**
   * Returns the partner's address.
   * 
   * @return Partner address.
   */
  public String address() {
    return this._address;
  }

  /**
   * Gets the price of a sale with fees/discounts.
   * 
   * @param product The product that was sold.
   * @param delay Delay of the sale payment (can be negative).
   * @param basePrice Base price of the sale.
   * @return The price paid with fees/discounts.
   */
  public double getSalePaymentPrice(Product product, int delay, double basePrice) {
    return this._rank.getSalePaymentPrice(product, delay, basePrice);
  }

  /**
   * Registers a sale payment.
   * 
   * @param product The product that was sold.
   * @param delay Delay of the sale payment (can be negative).
   * @param basePrice Base price of the sale.
   * @return The price paid with fees/discounts.
   */
  public double registerSalePayment(Product product, int delay, double basePrice) {
    double price = this._rank.registerSalePayment(product, delay, basePrice);
    this._paidSalesValues += price;
    return price;
  }

  /**
   * Registers a sale made to this partner.
   * 
   * @param value Sale value.
   */
  public void registerSale(double value) {
    this._salesValue += value;
  }

  /**
   * Registers an acquisition made to this partner.
   * 
   * @param value Acquisition value.
   */
  public void registerAcquisition(double value) {
    this._acquisitionsValue += value;
  }

  /**
   * Registers a breakdown requested by this partner.
   * 
   * @param value Breakdown value.
   */
  public void registerBreakdown(double value) {
    if (value > 0.0) {
      this._points += value * 10.0;
      this._rank.onPointsChange();
    }
  }

  @Override
  public String toString() {
    return this._key + "|" + this._name + "|" + this._address + "|" + this._rank.toString() + "|"
        + Math.round(this._points) + "|" + Math.round(this._acquisitionsValue) + "|" + Math.round(this._salesValue)
        + "|" + Math.round(this._paidSalesValues);
  }
}
