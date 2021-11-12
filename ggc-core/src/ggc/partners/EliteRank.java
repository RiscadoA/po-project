package ggc.partners;

import ggc.products.Product;

public class EliteRank extends Partner.Rank {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111101049L;

  public EliteRank(Partner partner) {
    partner.super();
  }

  @Override
  public double getSalePaymentPrice(Product product, int delay, double basePrice) {
    if (delay <= 0) // Period 1 and 2
      return basePrice * 0.9; // 10% discount
    else if (delay <= product.getPeriodN()) // Period 3
      return basePrice * 0.95; // 5% discount
    else // Period 4
      return basePrice; // No fee
  }

  @Override
  public double registerSalePayment(Product product, int delay, double basePrice) {
    double realPrice = this.getSalePaymentPrice(product, delay, basePrice);

    if (delay <= 0)
      this.setPoints(this.points() + realPrice * 10.0);
    else if (delay > 15) {
      this.setPoints(this.points() * 0.25); // Lose 75% of the points
      this.setRank(new SelectionRank(this.partner()));
    }

    return realPrice;
  }

  @Override
  public void onPointsChange() {
    // Do nothing
  }

  @Override
  public String toString() {
    return "ELITE";
  }
}
