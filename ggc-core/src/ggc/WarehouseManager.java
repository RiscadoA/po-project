package ggc;

import java.util.stream.Collectors;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import ggc.exceptions.*;

/** Façade for access. */
public class WarehouseManager {

  /** Name of file storing current store. */
  private String _filename;

  /** The warehouse itself. */
  private Warehouse _warehouse;

  /** Was the warehouse changed since the last save? */
  private boolean _dirtyFlag;

  /** Default constructor. */
  public WarehouseManager() {
    this._filename = null;
    this._warehouse = new Warehouse();
    this._dirtyFlag = true;
  }

  /**
   * Returns the current date.
   * 
   * @return Current date.
   */
  public int date() {
    return this._warehouse.date();
  }

  /**
   * Advances the current date.
   * 
   * @param days Number of days to advance.
   * @throws InvalidDateException
   */
  public void advanceDate(int days) throws InvalidDateException {
    this._warehouse.advanceDate(days);
    this._dirtyFlag = true;
  }

  /**
   * Returns the available balance.
   * 
   * @return Available balance.
   */
  public double availableBalance() {
    return this._warehouse.availableBalance();
  }

  /**
   * Returns the accounting balance.
   * 
   * @return Accounting balance.
   */
  public double accountingBalance() {
    return this._warehouse.accountingBalance();
  }

  /**
   * Returns a string with a list of the known products.
   * 
   * @return String with a list of known products.
   */
  public String products() {
    return this._warehouse.products().stream()
        .map(p -> p.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Registers a new simple product.
   * 
   * @param key Product key.
   * @throws DuplicateProductKeyException
   */
  public void registerProduct(String key) throws DuplicateProductKeyException {
    this._warehouse.registerProduct(key);
    this._dirtyFlag = true;
  }

  /**
   * Registers a new derivate product, which requires a recipe. The recipe is
   * specified through the 'components' and 'amounts' array. The two arrays must
   * have the same length, and for each index i of the array, amounts[i] specifies
   * the amount of the component components[i] that is needed.
   * 
   * @param key Product key.
   * @param aggravation The aggravation of the derivate product.
   * @param componentKeys The keys of the products required to make this product.
   * @param componentAmounts The amount of each component required to make this
   *          product.
   * @throws DuplicateProductKeyException
   * @throws UnknownProductKeyException
   * @throws InvalidRecipeException
   */
  public void registerProduct(String key, double aggravation, String[] componentKeys, int[] componentAmounts)
      throws DuplicateProductKeyException, UnknownProductKeyException, InvalidRecipeException {
    this._warehouse.registerProduct(key, aggravation, componentKeys, componentAmounts);
    this._dirtyFlag = true;
  }

  /**
   * Returns a string with the list of batches the warehouse has.
   * 
   * @return String with a list of batches.
   */
  public String batches() {
    return this._warehouse.batches().stream()
        .map(b -> b.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string with the list of batches the warehouse has which are
   * associated to a partner.
   * 
   * @param partnerKey Partner key.
   * @return String with a list of batches.
   * @throws UnknownPartnerKeyException
   */
  public String batchesByPartner(String partnerKey) throws UnknownPartnerKeyException {
    return this._warehouse.batchesByPartner(this._warehouse.partner(partnerKey))
        .stream().map(b -> b.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string with the list of batches of a certain product the warehouse
   * has.
   * 
   * @param productKey Product key.
   * @return String with a list of batches.
   * @throws UnknownProductKeyException
   */
  public String batchesByProduct(String productKey) throws UnknownProductKeyException {
    return this._warehouse.product(productKey).batches()
        .stream().map(b -> b.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string with the list of batches under a certain price the warehouse
   * has.
   * 
   * @param priceLimit The maximum price of the returned batches.
   * @return String with a list of batches.
   */
  public String batchesByPrice(double priceLimit) {
    return this._warehouse.batchesByPrice(priceLimit)
        .stream().map(b -> b.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns information about a partner from its key.
   * 
   * @param key Partner key.
   * @return String with information about the partner.
   * @throws UnknownPartnerKeyException
   */
  public String partner(String key) throws UnknownPartnerKeyException {
    return this._warehouse.partner(key).toString() + this._warehouse.partnerNotifications(key)
        .stream().map(n -> "\n" + n.toString())
        .collect(Collectors.joining());
  }

  /**
   * Returns a string with the list of partners the warehouse has.
   * 
   * @return String with a list of partners.
   */
  public String partners() {
    return this._warehouse.partners().stream()
        .map(p -> p.toString())
        .collect(Collectors.joining("\n"));
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
    this._warehouse.registerPartner(key, name, address);
    this._dirtyFlag = true;
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
    this._warehouse.toggleNotification(partnerKey, productKey);
    this._dirtyFlag = true;
  }

  /**
   * Returns a string with the list of purchases the warehouse has made to a
   * certain partner.
   * 
   * @param partnerKey Partner key.
   * @return String with a list of transactions.
   * @throws UnknownPartnerKeyException
   */
  public String partnerAcquisitions(String partnerKey) throws UnknownPartnerKeyException {
    return this._warehouse.partnerAcquisitions(partnerKey).stream()
        .map(t -> t.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string with the list of sales and breakdowns the warehouse has made
   * to a certain partner.
   * 
   * @param partnerKey Partner key.
   * @return String with a list of transactions.
   * @throws UnknownPartnerKeyException
   */
  public String partnerSalesAndBreakdowns(String partnerKey) throws UnknownPartnerKeyException {
    return this._warehouse.partnerSalesAndBreakdowns(partnerKey).stream()
        .map(t -> t.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string with the list of paid transactions the warehouse has with a
   * certain partner.
   * 
   * @param partnerKey Partner key.
   * @return String with a list of transactions.
   * @throws UnknownPartnerKeyException
   */
  public String partnerPaidTransactions(String partnerKey) throws UnknownPartnerKeyException {
    return this._warehouse.partnerPaidTransactions(partnerKey).stream()
        .map(t -> t.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string with the list of transactions the warehouse has made with a
   * certain partner.
   * 
   * @param partnerKey Partner key.
   * @return String with a list of transactions.
   * @throws UnknownPartnerKeyException
   */
  public String partnerHistory(String partnerKey) throws UnknownPartnerKeyException {
    return this._warehouse.partnerTransactions(partnerKey).stream()
        .map(t -> t.toString())
        .collect(Collectors.joining("\n"));
  }

  /**
   * Returns a string with information regarding a specific transaction.
   * 
   * @param key Transaction key.
   * @return String with information about a single transaction.
   * @throws UnknownTransactionKeyException
   */
  public String transaction(int key) throws UnknownTransactionKeyException {
    return this._warehouse.transaction(key).toString();
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
    this._warehouse.registerBreakdown(partnerKey, productKey, amount);
    this._dirtyFlag = true;
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
    this._warehouse.registerSale(partnerKey, productKey, deadline, amount);
    this._dirtyFlag = true;
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
    this._warehouse.registerAcquisition(partnerKey, productKey, amount, price);
    this._dirtyFlag = true;
  }

  /**
   * Registers a sale payment.
   * 
   * @param transactionKey Sale transaction key.
   * @throws UnknownTransactionKeyException
   */
  public void receiveSalePayment(int transactionKey) throws UnknownTransactionKeyException {
    this._warehouse.receiveSalePayment(transactionKey);
    this._dirtyFlag = true;
  }

  /**
   * Saves the current state to the associated file.
   * 
   * @throws IOException
   * @throws FileNotFoundException
   * @throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    if (this._filename == null) {
      throw new MissingFileAssociationException();
    }

    if (this._dirtyFlag) {
      FileOutputStream f = new FileOutputStream(this._filename);
      ObjectOutputStream o = new ObjectOutputStream(f);
      o.writeObject(this._warehouse);
      o.close();

      this._dirtyFlag = false;
    }
  }

  /**
   * Saves the current state to a file.
   * 
   * @param filename File name.
   * @throws IOException
   * @throws FileNotFoundException
   */
  public void saveAs(String filename) throws FileNotFoundException, IOException {
    this._filename = filename;
    try {
      save();
    } catch (MissingFileAssociationException e) {
      e.printStackTrace(); // This should never happen.
    }
  }

  /**
   * Loads the current state from a file previously written by save().
   * 
   * @@param filename File name.
   * @@throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {
    try {
      FileInputStream f = new FileInputStream(filename);
      ObjectInputStream o = new ObjectInputStream(f);
      this._warehouse = (Warehouse) o.readObject();
      o.close();
    } catch (IOException | ClassNotFoundException e) {
      throw new UnavailableFileException(filename);
    }

    this._filename = filename;
    this._dirtyFlag = false;
  }

  /**
   * Imports data from a text file.
   * 
   * @param textfile File name to be imported.
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException | DuplicatePartnerKeyException | DuplicateProductKeyException
        | UnknownPartnerKeyException | UnknownProductKeyException | InvalidRecipeException e) {
      throw new ImportFileException(textfile);
    }

    this._dirtyFlag = true;
  }
}
