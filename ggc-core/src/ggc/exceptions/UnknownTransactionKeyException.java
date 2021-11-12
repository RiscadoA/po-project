package ggc.exceptions;

/**
 * Class for representing an unknown transaction key error.
 */
public class UnknownTransactionKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110141416L;

  private int _transactionKey;

  /**
   * @param transactionKey The transaction key that was not found.
   */
  public UnknownTransactionKeyException(int transactionKey) {
    this._transactionKey = transactionKey;
  }

  /**
   * @return The requested transaction key.
   */
  public int getTransactionKey() {
    return this._transactionKey;
  }
}
