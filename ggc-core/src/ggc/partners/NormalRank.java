package ggc.partners;

import ggc.products.Product;

public class NormalRank extends Partner.Rank {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110251450L;

  public NormalRank(Partner partner) {
    partner.super();
  }

  @Override
  public double getSalePaymentPrice(Product product, int delay, double basePrice) {
    if (delay <= -product.getPeriodN()) // Period 1
      return basePrice * 0.9; // 10% discount
    else if (delay <= 0) // Period 2
      return basePrice; // No discount
    else if (delay <= product.getPeriodN()) // Period 3
      return basePrice * (1.0 + 0.05 * delay); // 5% daily fee
    else // Period 4
      return basePrice * (1.0 + 0.1 * delay); // 10% daily fee
  }

  @Override
  public double registerSalePayment(Product product, int delay, double basePrice) {
    double realPrice = this.getSalePaymentPrice(product, delay, basePrice);

    if (delay <= 0)
      this.setPoints(this.points() + realPrice * 10.0);
    else
      this.setPoints(0.0);
    this.onPointsChange();

    return realPrice;
  }

  @Override
  public void onPointsChange() {
    if (this.points() > 25000.0) { // Upgrade to elite
      this.setRank(new EliteRank(this.partner()));
    } else if (this.points() > 2000.0) { // Upgrade to selection
      this.setRank(new SelectionRank(this.partner()));
    }
  }

  @Override
  public String toString() {
    return "NORMAL";
  }
}
