package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("transactionKey", Prompt.transactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    int transactionKey = integerField("transactionKey");
    try {
      _display.popup(_receiver.transaction(transactionKey));
    } catch (ggc.exceptions.UnknownTransactionKeyException e) {
      throw new ggc.app.exceptions.UnknownTransactionKeyException(e.getTransactionKey());
    }
  }

}
