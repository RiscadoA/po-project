package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("transactionKey", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    int transactionKey = integerField("transactionKey");
    try {
      _receiver.receiveSalePayment(transactionKey);
    } catch (ggc.exceptions.UnknownTransactionKeyException e) {
      throw new ggc.app.exceptions.UnknownTransactionKeyException(e.getTransactionKey());
    }
  }
}
