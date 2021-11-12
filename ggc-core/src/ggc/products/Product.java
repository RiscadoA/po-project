package ggc.products;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import ggc.exceptions.UnavailableProductException;
import ggc.notifications.BargainNotification;
import ggc.notifications.NewNotification;
import ggc.notifications.Subject;
import ggc.partners.Partner;
import ggc.visitors.SellChecker;

/**
 * Implements a product.
 */
public abstract class Product extends Subject {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110151906L;

  /** Product key. */
  private String _key;

  /** Maximum product price registered until now. */
  private double _maxPrice;

  /** Current product stock. */
  private int _stock;

  /** The current batches of this product. */
  private PriorityQueue<Batch> _batches;

  /**
   * @param key Product key.
   */
  public Product(String key) {
    this._key = key;
    this._maxPrice = 0.0;
    this._stock = 0;
    this._batches = new PriorityQueue<Batch>(Batch.PRICE_COMPARATOR);
  }

  /**
   * Returns the product key.
   * 
   * @return Product key.
   */
  public String key() {
    return this._key;
  }

  /**
   * Returns the maximum price at which the product was valued.
   * 
   * @return Maximum price.
   */
  public double maxPrice() {
    return this._maxPrice;
  }

  /**
   * Updates the maximum price of this product, if the price is bigger than the
   * previous maximum.
   * 
   * @param price New price.
   */
  protected void addPrice(double price) {
    if (price > this._maxPrice) {
      this._maxPrice = price;
    }
  }

  /**
   * Returns the current stock of this product.
   * 
   * @return Current stock.
   */
  public int stock() {
    return this._stock;
  }

  /**
   * Returns the batches of this product, sorted by their partner, price and
   * amount.
   * 
   * @return Array of batches.
   */
  public Collection<Batch> batches() {
    List<Batch> batches = new ArrayList<Batch>(this._batches);
    Collections.sort(batches, Batch.LIST_COMPARATOR);
    return batches;
  }

  /**
   * @return The cheapest batch of this product, or null if there are no batches.
   */
  public Batch cheapestBatch() {
    return this._batches.peek();
  }

  /**
   * Adds a new batch of this product.
   * 
   * @param partner Partner associated to the batch.
   * @param amount Amount of units in the batch.
   * @param price Price of each unit in the batch.
   */
  public void addBatch(Partner partner, int amount, double price) {
    // Notify observers
    if (this._maxPrice > 0.0) {
      if (this._stock == 0)
        this.notifyObservers(new NewNotification(this, price));
      else if (this.cheapestBatch().price() > price)
        this.notifyObservers(new BargainNotification(this, price));
    }

    Batch batch = new Batch(this, partner, amount, price);
    this._batches.add(batch);
    this._stock += amount;
    this.addPrice(price);
  }

  /**
   * Removes an amount of this product, consuming the batches in a way so that the
   * total price of the units is as low as possible.
   * 
   * @param amount Amount of product to remove.
   * @return Total price of the units removed.
   * @throws UnavailableProductException
   */
  public double take(int amount) throws UnavailableProductException {
    if (this._stock < amount) {
      throw new UnavailableProductException(this._key, amount, this._stock);
    }

    double price = 0.0;

    // Gets batches until enough units of the product are found
    this._stock -= amount;
    while (amount > 0) {
      Batch batch = this._batches.peek();
      int units = Math.min(batch.amount(), amount);
      price += batch.price() * units;
      amount -= units;

      batch.setAmount(batch.amount() - units);
      if (batch.amount() == 0)
        this._batches.poll(); // Remove batch from the queue.
    }

    return price;
  }

  /**
   * Checks if it is possible to sell the given amount of this product. If it
   * isn't, an exception is thrown.
   * 
   * @param amount Amount of product to sell.
   * @throws UnavailableProductException
   */
  public void checkSell(int amount) throws UnavailableProductException {
    this.accept(new SellChecker(amount));
  }

  /**
   * Sells an amount of this product, consuming the batches in a way so that the
   * total price of the units is as low as possible. If there aren't enough units
   * of the product, it will be fabricated, if possible.
   * 
   * @param amount Amount of product to sell.
   * @return Total price of the units sold.
   * @throws UnavailableProductException
   */
  public abstract double sell(int amount) throws UnavailableProductException;

  /**
   * Returns the constant used to calculate transaction fees for this product.
   * 
   * @return Constant used to calculaet transaction fees for this product.
   */
  public abstract int getPeriodN();

  /**
   * Accepts a product visitor.
   * 
   * @param <T> Type of exception thrown by the visitor.
   * @param visitor Visitor to be called.
   * @param param Parameter to be passed to the visitor.
   */
  public abstract <T extends Throwable> void accept(ProductVisitor<T> visitor) throws T;

  @Override
  public String toString() {
    return this._key + "|" + Math.round(this._maxPrice) + "|" + this._stock;
  }
}
