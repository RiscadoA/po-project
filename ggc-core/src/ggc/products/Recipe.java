package ggc.products;

import java.io.Serializable;

import ggc.exceptions.InvalidRecipeException;

/**
 * Represents a derivate product recipe.
 */
public class Recipe implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110180907L;

  /** Recipe aggravation. */
  private double _aggravation;

  /** Recipe components. */
  private Component[] _components;

  /** Represents a single recipe component. */
  public static class Component implements Serializable {
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202110180912L;

    /** Component product. */
    private Product _product;

    /** Component amount. */
    private int _amount;

    /**
     * @param product Which product is this component.
     * @param amount How many of this component is needed.
     */
    public Component(Product product, int amount) {
      this._product = product;
      this._amount = amount;
    }

    /**
     * @return This component's product.
     */
    public Product product() {
      return this._product;
    }

    /**
     * @return How many of this component is needed.
     */
    public int amount() {
      return this._amount;
    }

    @Override
    public String toString() {
      return this._product.key() + ":" + this._amount;
    }
  }

  /**
   * @param aggravation Recipe aggravation.
   * @param produts Recipe component products.
   * @param amounts Recipe component amounts.
   */
  public Recipe(double aggravation, Product[] products, int[] amounts) throws InvalidRecipeException {
    if (products.length != amounts.length)
      throw new InvalidRecipeException();

    this._aggravation = aggravation;
    this._components = new Component[products.length];
    for (int i = 0; i < products.length; i++)
      this._components[i] = new Component(products[i], amounts[i]);
  }

  /**
   * Returns the aggravation of the recipe.
   * 
   * @return Recipe aggravation.
   */
  public double aggravation() {
    return this._aggravation;
  }

  /**
   * Returns the components of this recipe.
   * 
   * @return Recipe components.
   */
  public Component[] components() {
    return this._components;
  }

  @Override
  public String toString() {
    String out = this._aggravation + "|" + this._components[0].toString();
    for (int i = 1; i < this._components.length; ++i)
      out += "#" + this._components[i].toString();
    return out;
  }
}
