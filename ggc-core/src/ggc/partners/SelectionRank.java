package ggc.partners;

import ggc.products.Product;

public class SelectionRank extends Partner.Rank {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202111101048L;

  public SelectionRank(Partner partner) {
    partner.super();
  }

  @Override
  public double getSalePaymentPrice(Product product, int delay, double basePrice) {
    if (delay <= -product.getPeriodN()) // Period 1
      return basePrice * 0.9; // 10% discount
    else if (delay <= -2) // Period 2 with discount
      return basePrice * 0.95; // 5% discount
    else if (delay <= 1) // Period 2 with no discount
      return basePrice; // No discount
    else if (delay <= product.getPeriodN()) // Period 3
      return basePrice * (1.0 + 0.02 * delay); // 2% daily fee
    else // Period 4
      return basePrice * (1.0 + 0.05 * delay); // 5% daily fee
  }

  @Override
  public double registerSalePayment(Product product, int delay, double basePrice) {
    double realPrice = this.getSalePaymentPrice(product, delay, basePrice);

    if (delay <= 0) {
      this.setPoints(this.points() + realPrice * 10.0);
      this.onPointsChange();
    } else if (delay > 2) {
      this.setPoints(this.points() * 0.1); // Lose 90% of the points
      this.setRank(new NormalRank(this.partner()));
    }

    return realPrice;
  }

  @Override
  public void onPointsChange() {
    if (this.points() > 25000.0) { // Upgrade to elite
      this.setRank(new EliteRank(this.partner()));
    }
  }

  @Override
  public String toString() {
    return "SELECTION";
  }
}
