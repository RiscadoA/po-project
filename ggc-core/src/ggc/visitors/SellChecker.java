package ggc.visitors;

import java.util.HashMap;
import java.util.Stack;

import ggc.products.ProductVisitor;
import ggc.products.Product;
import ggc.products.SimpleProduct;
import ggc.products.DerivateProduct;
import ggc.products.Recipe;
import ggc.exceptions.UnavailableProductException;

/**
 * Used to check if a certain amount of product can be sold, without interfering
 * with the state of the warehouse.
 */
public class SellChecker implements ProductVisitor<UnavailableProductException> {

  /** The current 'virtual' stock of each product. */
  private HashMap<Product, Integer> _currentStocks = new HashMap<Product, Integer>();

  /**
   * Stack whose top value corresponds is the needed amount of the visited
   * product.
   */
  private Stack<Integer> _neededAmounts = new Stack<Integer>();

  /** Root product. */
  private Product _root;

  /** The amount of product we want to sell. */
  private int _amount;

  /** The missing component. */
  private Product _missingComponent;

  /** How many of the component is missing. */
  private int _missingAmount;

  /**
   * @param amount Amount of product to sell.
   */
  public SellChecker(int amount) {
    this._root = null;
    this._amount = amount;
    this._missingComponent = null;
    this._missingAmount = 0;
  }

  /**
   * Visit used for the 'find missing component' stage.
   * 
   * @param product Simple product to visit.
   * @throws UnavailableProductException
   */
  private void findMissing(SimpleProduct product) throws UnavailableProductException {
    // The product wasn't visited before, so the 'virtual' stock is the same as the
    // real stock.
    this._currentStocks.putIfAbsent(product, product.stock());

    // If the needed amount is greater than the 'virtual' stock, we throw an
    // exception.
    int newStock = this._currentStocks.get(product) - this._neededAmounts.peek();
    if (newStock < 0) {
      this._missingComponent = product;
      return;
    }

    // Otherwise, we update the 'virtual' stock and pop the stack.
    this._currentStocks.put(product, newStock);
    this._neededAmounts.pop();
  }

  /**
   * Visit used for the 'find missing component' stage.
   * 
   * @param product Derivate product to visit.
   * @throws UnavailableProductException
   */
  private void findMissing(DerivateProduct product) throws UnavailableProductException {
    // The product wasn't visited before, so the 'virtual' stock is the same as the
    // real stock.
    _currentStocks.putIfAbsent(product, product.stock());
    int currentStock = _currentStocks.get(product);
    int required = this._neededAmounts.pop() - currentStock;

    // If the needed amount is greater than the 'virtual' stock, the product must be
    // agreggated from its components.
    if (required > 0) {
      // For each component, we recurse on the visit method, to check if we have
      // enough components to make the required amount of product.
      for (Recipe.Component rc : product.recipe().components()) {
        this._neededAmounts.push(required * rc.amount());
        rc.product().accept(this);
        if (this._missingComponent != null)
          return;
      }

      _currentStocks.put(product, 0);
    } else
      // Otherwise, we just need to update the 'virtual' stock.
      _currentStocks.put(product, -required);
  }

  /**
   * Visit used for the 'find missing component' stage.
   * 
   * @param product Simple product to visit.
   * @throws UnavailableProductException
   */
  private void countMissing(SimpleProduct product) throws UnavailableProductException {
    int required = this._neededAmounts.pop();
    if (product == this._missingComponent)
      this._missingAmount += required;
  }

  /**
   * Visit used for the 'find missing component' stage.
   * 
   * @param product Derivate product to visit.
   * @throws UnavailableProductException
   */
  private void countMissing(DerivateProduct product) throws UnavailableProductException {
    // The amount of product that needs to be agreggated
    int required = this._neededAmounts.pop() - product.stock();
    if (required > 0) {
      for (Recipe.Component rc : product.recipe().components()) {
        this._neededAmounts.push(required * rc.amount());
        rc.product().accept(this);
      }
    }
  }

  @Override
  public void visit(SimpleProduct product) throws UnavailableProductException {
    if (this._root == null) {
      // This is the root
      if (product.stock() < this._amount)
        throw new UnavailableProductException(product.key(), this._amount, product.stock());
      return;
    }

    if (this._missingComponent == null)
      this.findMissing(product);
    else
      this.countMissing(product);
  }

  @Override
  public void visit(DerivateProduct product) throws UnavailableProductException {
    if (this._root == null) {
      // This is the root
      this._root = product;
      this._neededAmounts.push(this._amount);

      // Find the missing component
      this.findMissing(product);

      // If we found one
      if (this._missingComponent != null) {
        // We need to count the missing amount
        this._neededAmounts.push(this._amount);
        this.countMissing(product);

        // Throw exception
        throw new UnavailableProductException(this._missingComponent.key(), this._missingAmount,
            this._missingComponent.stock());
      }
    } else {
      // This is not the root, choose the right method
      if (this._missingComponent == null)
        this.findMissing(product);
      else
        this.countMissing(product);
    }
  }
}
