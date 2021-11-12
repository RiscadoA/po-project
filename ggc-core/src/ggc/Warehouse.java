package ggc;

import java.io.Serializable;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.Map;
import java.util.Collection;
import java.util.TreeMap;
import java.util.stream.Collectors;

import java.lang.Math;

import ggc.exceptions.*;
import ggc.notifications.Notification;
import ggc.notifications.NotificationRegister;
import ggc.products.Batch;
import ggc.products.DerivateProduct;
import ggc.products.Product;
import ggc.products.Recipe;
import ggc.products.SimpleProduct;
import ggc.partners.Partner;
import ggc.transactions.Transaction;
import ggc.transactions.Acquisition;
import ggc.transactions.Sale;
import ggc.transactions.Breakdown;
import ggc.visitors.TransactionPayer;
import ggc.visitors.ProductBreaker;
import ggc.visitors.AccountingBalanceCalculator;
import ggc.visitors.AcquisitionCollector;
import ggc.visitors.AvailableBalanceCalculator;
import ggc.visitors.PaidTransactionsCollector;
import ggc.visitors.SaleAndBreakdownCollector;
import ggc.visitors.TransactionDateUpdater;

/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  /** Current date. */
  private int _date;

  /** Products. */
  private Map<String, Product> _products;

  /** Partners. */
  private Map<String, Partner> _partners;

  /** Transactions. */
  private Map<Integer, Transaction> _transactions;

  /** Default notification method, which registers all notifications sent. */
  private NotificationRegister _notificationRegister;

  /** Default constructor. */
  public Warehouse() {
    this._date = 0;

    this._products = new TreeMap<String, Product>(new CollatorWrapper());
    this._partners = new TreeMap<String, Partner>(new CollatorWrapper());
    this._transactions = new TreeMap<Integer, Transaction>();

    this._notificationRegister = new NotificationRegister();
  }

  /**
   * Returns the current date.
   * 
   * @return Current date.
   */
  public int date() {
    return this._date;
  }

  /**
   * Advances the current date.
   * 
   * @param days Number of days to advance.
   * @throws InvalidDateException
   */
  public void advanceDate(int days) throws InvalidDateException {
    if (days <= 0) {
      throw new InvalidDateException(days);
    } else {
      this._date += days;

      // Update date for every transaction
      TransactionDateUpdater updater = new TransactionDateUpdater(this._date);
      for (Transaction t : this._transactions.values())
        t.accept(updater);
    }
  }

  /**
   * Returns the available balance.
   * 
   * @return Available balance.
   */
  public double availableBalance() {
    AvailableBalanceCalculator calculator = new AvailableBalanceCalculator();
    for (Transaction t : this._transactions.values())
      t.accept(calculator);
    return calculator.result();
  }

  /**
   * Returns the accounting balance.
   * 
   * @return Accounting balance.
   */
  public double accountingBalance() {
    AccountingBalanceCalculator calculator = new AccountingBalanceCalculator();
    for (Transaction t : this._transactions.values())
      t.accept(calculator);
    return calculator.result();
  }

  /**
   * Returns a product from its key.
   * 
   * @param key Product key.
   * @return Product object.
   * @throws UnknownProductKeyException
   */
  public Product product(String key) throws UnknownProductKeyException {
    Product product = this._products.get(key);
    if (product == null)
      throw new UnknownProductKeyException(key);
    return product;
  }

  /**
   * Returns all products known to the warehouse, sorted by their key.
   * 
   * @return Collection of products.
   */
  public Collection<Product> products() {
    return this._products.values();
  }

  /**
   * Returns all batches known to the warehouse.
   * 
   * @return Collection of batches.
   */
  public Collection<Batch> batches() {
    return this._products.values().stream()
        .flatMap(p -> p.batches().stream())
        .collect(Collectors.toList());
  }

  /**
   * Returns all batches known to the warehouse, associated to a partner.
   * 
   * @param partner Partner.
   * @return Collection of batches.
   */
  public Collection<Batch> batchesByPartner(Partner partner) {
    return this._products.values().stream()
        .flatMap(p -> p.batches().stream())
        .filter(b -> b.partner() == partner)
        .collect(Collectors.toList());
  }

  /**
   * Returns all batches below a given price threshold.
   * 
   * @param priceLimit Price threshold.
   * @return Collection of batches.
   */
  public Collection<Batch> batchesByPrice(double priceLimit) {
    return this._products.values().stream()
        .flatMap(p -> p.batches().stream())
        .filter(b -> b.price() <= priceLimit)
        .collect(Collectors.toList());
  }

  /**
   * Registers a new simple product.
   * 
   * @param key Product key.
   * @return Product object.
   * @throws DuplicateProductKeyException
   */
  public Product registerProduct(String key) throws DuplicateProductKeyException {
    Product product = new SimpleProduct(key);
    if (this._products.putIfAbsent(key, product) != null) {
      throw new DuplicateProductKeyException(key);
    }

    // Add default notification method
    product.registerNotificationMethod(this._notificationRegister);

    // Make all partners listen to notifications from this product
    for (Partner partner : this._partners.values())
      product.attachObserver(partner);

    return product;
  }

  /**
   * Registers a new derivate product.
   * 
   * @param key Product key.
   * @param aggravation Product recipe aggravation.
   * @param componentKeys Product recipe component keys.
   * @param amounts Product recipe component amounts.
   * @return Product object.
   * @throws DuplicateProductKeyException
   * @throws UnknownProductKeyException
   * @throws InvalidRecipeException
   */
  public Product registerProduct(String key, double aggravation, String componentKeys[], int amounts[])
      throws DuplicateProductKeyException, UnknownProductKeyException, InvalidRecipeException {
    // Get component products
    Product[] components = new Product[componentKeys.length];
    for (int i = 0; i < componentKeys.length; i++)
      components[i] = this.product(componentKeys[i]);

    // Create derivate product
    Product product = new DerivateProduct(key, new Recipe(aggravation, components, amounts));
    if (this._products.putIfAbsent(key, product) != null) {
      throw new DuplicateProductKeyException(key);
    }

    // Add default notification method
    product.registerNotificationMethod(this._notificationRegister);

    // Make all partners listen to notifications from this product
    for (Partner partner : this._partners.values())
      product.attachObserver(partner);

    return product;
  }

  /**
   * Returns information about a partner from its key.
   * 
   * @param key Partner key.
   * @return Partner object.
   * @throws UnknownPartnerKeyException
   */
  public Partner partner(String key) throws UnknownPartnerKeyException {
    Partner partner = this._partners.get(key);
    if (partner == null)
      throw new UnknownPartnerKeyException(key);
    return partner;
  }

  /**
   * Returns all partners known to the warehouse, sorted by their key.
   * 
   * @return Collection of partners.
   */
  public Collection<Partner> partners() {
    return this._partners.values();
  }

  /**
   * Registers a new partner.
   * 
   * @param key Partner key.
   * @param name Partner name.
   * @param address Partner address.
   * @throws DuplicatePartnerKeyException
   */
  public void registerPartner(String key, String name, String address) throws DuplicatePartnerKeyException {
    Partner partner = new Partner(key, name, address);

    if (this._partners.putIfAbsent(key, partner) != null) {
      throw new DuplicatePartnerKeyException(key);
    }

    for (Product p : this._products.values())
      p.attachObserver(partner);
  }

  /**
   * Toggles whether a partner receives notifications about a product or not.
   * 
   * @param partnerKey Partner key.
   * @param productKey Product key.
   * @throws UnknownPartnerKeyException
   * @throws UnknownProductKeyException
   */
  public void toggleNotification(String partnerKey, String productKey)
      throws UnknownPartnerKeyException, UnknownProductKeyException {
    Partner partner = this.partner(partnerKey);
    Product product = this.product(productKey);
    if (product.hasObserverAttached(partner))
      product.detachObserver(partner);
    else
      product.attachObserver(partner);
  }

  /**
   * Returns the notifications registered for a partner.
   * 
   * @param partnerKey Partner key.
   * @return Collection of notifications.
   * @throws UnknownPartnerKeyException
   */
  public Collection<Notification> partnerNotifications(String partnerKey) throws UnknownPartnerKeyException {
    Partner partner = this.partner(partnerKey);
    return this._notificationRegister.popNotifications(partner);
  }

  /**
   * Returns a transaction from its key.
   * 
   * @param key Transaction key.
   * @return Transaction object.
   * @throws UnknownTransactionKeyException
   */
  public Transaction transaction(int key) throws UnknownTransactionKeyException {
    Transaction transaction = this._transactions.get(key);
    if (transaction == null)
      throw new UnknownTransactionKeyException(key);
    return transaction;
  }

  /**
   * Returns all acquisitions related to a partner.
   *
   * @param partnerKey Partner key.
   * @return Collection of transactions.
   * @throws UnknownPartnerKeyException
   */
  public Collection<Transaction> partnerAcquisitions(String partnerKey) throws UnknownPartnerKeyException {
    AcquisitionCollector collector = new AcquisitionCollector();
    this.partnerTransactions(partnerKey).forEach(t -> t.accept(collector));
    return collector.result();
  }

  /**
   * Returns all sales and breakdowns related to a partner.
   *
   * @param partnerKey Partner key.
   * @return Collection of transactions.
   * @throws UnknownPartnerKeyException
   */
  public Collection<Transaction> partnerSalesAndBreakdowns(String partnerKey) throws UnknownPartnerKeyException {
    SaleAndBreakdownCollector collector = new SaleAndBreakdownCollector();
    this.partnerTransactions(partnerKey).forEach(t -> t.accept(collector));
    return collector.result();
  }

  /**
   * Returns all paid transactions related to a partner.
   * 
   * @param partnerKey Partner key.
   * @return Collection of transactions.
   * @throws UnknownPartnerKeyException
   */
  public Collection<Transaction> partnerPaidTransactions(String partnerKey) throws UnknownPartnerKeyException {
    PaidTransactionsCollector collector = new PaidTransactionsCollector();
    this.partnerTransactions(partnerKey).forEach(t -> t.accept(collector));
    return collector.result();
  }

  /**
   * Returns all transactions related to a partner.
   * 
   * @param partnerKey Partner key.
   * @return Collection of transactions.
   * @throws UnknownPartnerKeyException
   */
  public Collection<Transaction> partnerTransactions(String partnerKey) throws UnknownPartnerKeyException {
    Partner partner = this.partner(partnerKey);
    return this._transactions.values().stream()
        .filter(t -> t.partner() == partner)
        .collect(Collectors.toList());
  }

  /**
   * Registers a breakdown transaction.
   * 
   * @param partnerKey Partner key.
   * @param productKey Product key.
   * @param amount How many units were broke down?
   * @throws UnknownPartnerKeyException
   * @throws UnknownProductKeyException
   * @throws UnavailableProductException
   */
  public void registerBreakdown(String partnerKey, String productKey, int amount)
      throws UnknownPartnerKeyException, UnknownProductKeyException, UnavailableProductException {
    Partner partner = this.partner(partnerKey);
    Product product = this.product(productKey);

    // Breakdown the product
    ProductBreaker breaker = new ProductBreaker(partner, amount);
    product.accept(breaker);

    // If the product was broken down
    if (breaker.breakdownComponents() != null) {
      // Update partner
      partner.registerBreakdown(breaker.breakdownValue());
      double paidValue = breaker.breakdownValue() > 0.0 ? breaker.breakdownValue() : 0.0;

      // Register transaction
      Breakdown breakdown = new Breakdown(this._transactions.size(), this._date, amount, product, partner,
          breaker.breakdownValue(), paidValue, breaker.breakdownComponents());
      this._transactions.put(breakdown.id(), breakdown);
    }

  }

  /**
   * Registers a sale transaction.
   * 
   * @param partnerKey Partner key.
   * @param productKey Product key.
   * @param deadline Sale payment deadline.
   * @param amount Number of units sold.
   * @throws UnknownPartnerKeyException
   * @throws UnknownProductKeyException
   * @throws UnavailableProductException
   */
  public void registerSale(String partnerKey, String productKey, int deadline, int amount)
      throws UnknownPartnerKeyException, UnknownProductKeyException, UnavailableProductException {
    Partner partner = this.partner(partnerKey);
    Product product = this.product(productKey);

    // Sell product
    product.checkSell(amount);
    double baseValue = product.sell(amount);

    // Update partner
    partner.registerSale(baseValue);

    // Register sale transaction
    Sale sale = new Sale(this._transactions.size(), this._date, product, amount, partner, deadline, baseValue);
    this._transactions.put(sale.id(), sale);
  }

  /**
   * Registers an acquisition transaction.
   * 
   * @param partnerKey Partner key.
   * @param productKey Product key.
   * @param amount Number of units bought.
   * @param price Price of each unit.
   * @throws UnknownPartnerKeyException
   * @throws UnknownProductKeyException
   */
  public void registerAcquisition(String partnerKey, String productKey, int amount, double price)
      throws UnknownPartnerKeyException, UnknownProductKeyException {
    Partner partner = this.partner(partnerKey);
    Product product = this.product(productKey);

    // Register new batch
    product.addBatch(partner, amount, price);

    // Update partner
    partner.registerAcquisition(amount * price);

    // Register acquisition transaction
    Acquisition acquisition = new Acquisition(this._transactions.size(), this._date, amount, product, partner, price);
    this._transactions.put(acquisition.id(), acquisition);
  }

  /**
   * Registers a sale payment.
   * 
   * @param transactionKey Sale transaction key.
   * @throws UnknownTransactionKeyException
   */
  public void receiveSalePayment(int transactionKey) throws UnknownTransactionKeyException {
    TransactionPayer payer = new TransactionPayer(this._date);
    this.transaction(transactionKey).accept(payer);
  }

  /**
   * Imports data from a text file.
   * 
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   * @throws DuplicatePartnerKeyException
   * @throws DuplicateProductKeyException
   * @throws UnknownPartnerKeyException
   * @throws UnknownProductKeyException
   * @throws InvalidRecipeException
   */
  void importFile(String txtfile) throws IOException, BadEntryException, DuplicatePartnerKeyException,
      DuplicateProductKeyException, UnknownPartnerKeyException, UnknownProductKeyException, InvalidRecipeException {
    FileReader fr = new FileReader(new File(txtfile));
    BufferedReader br = new BufferedReader(fr);

    try {
      // Parse every line of the file
      String line;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split("\\|");

        if (fields[0].equals("PARTNER")) {
          this.registerPartner(fields[1], fields[2], fields[3]);
        } else if (fields[0].equals("BATCH_S")) {
          // Parse simple product batch field
          Partner partner = this.partner(fields[2]);
          double price = Double.parseDouble(fields[3]);
          int amount = Integer.parseInt(fields[4]);

          // If the product hasn't been registered yet, register it
          // now
          Product product = this._products.get(fields[1]);
          if (product == null)
            product = this.registerProduct(fields[1]);

          // Register batch
          product.addBatch(partner, amount, price);
        } else if (fields[0].equals("BATCH_M")) {
          // Parse derivate product batch field
          String productKey = fields[1];
          Partner partner = this.partner(fields[2]);
          double price = Double.parseDouble(fields[3]);
          int amount = Integer.parseInt(fields[4]);

          // If the product hasn't been registered yet, register it
          // now
          Product product = this._products.get(productKey);
          if (product == null) {
            // Parse recipe
            double aggravation = Double.parseDouble(fields[5]);
            String[] componentFields = fields[6].split("#");
            String[] componentKeys = new String[componentFields.length];
            int[] componentAmounts = new int[componentFields.length];

            for (int i = 0; i < componentFields.length; i++) {
              // Parse each recipe component
              String[] componentField = componentFields[i].split(":");
              componentKeys[i] = componentField[0];
              componentAmounts[i] = Integer.parseInt(componentField[1]);
            }

            // Register derivate product
            product = this.registerProduct(productKey, aggravation, componentKeys, componentAmounts);
          }

          // Register batch
          product.addBatch(partner, amount, price);
        } else {
          throw new BadEntryException(fields[0]);
        }
      }
    } finally {
      // Close streams
      br.close();
      fr.close();
    }
  }
}
