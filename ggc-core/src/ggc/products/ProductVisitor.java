package ggc.products;

/**
 * Visitor interface for products.
 * @param <T> The exception that the visitor may throw.
 */
public interface ProductVisitor<T extends Throwable> {
  /**
   * Visits a simple product.
   * 
   * @param product The product to visit.
   * @return The return value of the visit method.
   */
  public void visit(SimpleProduct product) throws T;

  /**
   * Visits a simple product.
   * 
   * @param product The product to visit.
   * @return The return value of the visit method.
   */
  public void visit(DerivateProduct product) throws T;
}
