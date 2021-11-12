package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Registers a sale transaction.
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addIntegerField("deadline", Prompt.paymentDeadline());
    addStringField("productKey", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    String partnerKey = stringField("partnerKey");
    int deadline = integerField("deadline");
    String productKey = stringField("productKey");
    int amount = integerField("amount");

    try {
      _receiver.registerSale(partnerKey, productKey, deadline, amount);
    } catch (ggc.exceptions.UnknownPartnerKeyException e) {
      throw new ggc.app.exceptions.UnknownPartnerKeyException(e.getPartnerKey());
    } catch (ggc.exceptions.UnknownProductKeyException e) {
      throw new ggc.app.exceptions.UnknownProductKeyException(e.getProductKey());
    } catch (ggc.exceptions.UnavailableProductException e) {
      throw new ggc.app.exceptions.UnavailableProductException(e.getProductKey(), e.getRequested(), e.getAvailable());
    }
  }
}
