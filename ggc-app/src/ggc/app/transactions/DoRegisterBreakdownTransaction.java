package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    String partnerKey = stringField("partnerKey");
    String productKey = stringField("productKey");
    int amount = integerField("amount");
    try {
      _receiver.registerBreakdown(partnerKey, productKey, amount);
    } catch (ggc.exceptions.UnknownProductKeyException e) {
      throw new ggc.app.exceptions.UnknownProductKeyException(e.getProductKey());
    } catch (ggc.exceptions.UnknownPartnerKeyException e) {
      throw new ggc.app.exceptions.UnknownPartnerKeyException(e.getPartnerKey());
    } catch (ggc.exceptions.UnavailableProductException e) {
      throw new ggc.app.exceptions.UnavailableProductException(e.getProductKey(), e.getRequested(), e.getAvailable());
    }
  }
}
