package ggc.transactions;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import ggc.products.Product;

import ggc.partners.Partner;

/**
 * Implements a breakdown transaction.
 */
public class Breakdown extends Transaction {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111092023L;

  /** Components that resulted from the breakdown. */
  private Component[] _components;

  /**
   * Base value (diffence between the value of the original product and the
   * components).
   */
  private double _baseValue;

  /** The value that was payed. */
  private double _payedValue;

  /**
   * Represents a component that results from the breakdown.
   */
  public static class Component implements Serializable {

    /** Serial number for serialization. */
    private static final long serialVersionUID = 202111092024L;

    /** The component's product. */
    private Product _product;

    /** The quantity of the component that results from the breakdown. */
    private int _amount;

    /** The component's value. */
    private double _value;

    /**
     * @param product The component's product.
     * @param amount The quantity of the component that results from the breakdown.
     * @param value The component's value.
     */
    public Component(Product product, int amount, double value) {
      this._product = product;
      this._amount = amount;
      this._value = value;
    }

    public Product product() {
      return this._product;
    }

    public int amount() {
      return this._amount;
    }

    @Override
    public String toString() {
      return this._product.key() + ":" + this._amount + ":" + Math.round(this._amount * this._value);
    }
  }

  /**
   * @param id The breakdown's id.
   * @param date The breakdown's date.
   * @param amount The amount of the product that was broke down.
   * @param product The product that was broke down.
   * @param partner The partner that payed for the breakdown.
   * @param baseValue The base value of the breakdown.
   * @param payedValue The value that was payed.
   * @param components The components that resulted from the breakdown.
   */
  public Breakdown(int id, int date, int amount, Product product, Partner partner, double baseValue, double payedValue,
      Component[] components) {
    super(id, date, amount, product, partner);
    this._baseValue = baseValue;
    this._payedValue = payedValue;
    this._components = components;
  }

  /**
   * @return The components that resulted from the breakdown.
   */
  public Component[] components() {
    return this._components;
  }

  /**
   * @return The base value (diffence between the value of the original product
   *         and the components).
   */
  public double baseValue() {
    return this._baseValue;
  }

  /**
   * @return The value that was payed.
   */
  public double payedValue() {
    return this._payedValue;
  }

  @Override
  public void accept(TransactionVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "DESAGREGAÇÃO|" + super.toString() + "|" + Math.round(this._baseValue) + "|" + Math.round(this._payedValue)
        + "|" + this.date() + "|" + Arrays.stream(this._components)
            .map(c -> c.toString())
            .collect(Collectors.joining("#"));
  }
}
