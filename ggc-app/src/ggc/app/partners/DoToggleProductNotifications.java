package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
  }

  @Override
  public void execute() throws CommandException {
    String partnerKey = stringField("partnerKey");
    String productKey = stringField("productKey");
    try {
      _receiver.toggleNotification(partnerKey, productKey);
    } catch (ggc.exceptions.UnknownPartnerKeyException e) {
      throw new ggc.app.exceptions.UnknownPartnerKeyException(e.getPartnerKey());
    } catch (ggc.exceptions.UnknownProductKeyException e) {
      throw new ggc.app.exceptions.UnknownProductKeyException(e.getProductKey());
    }
  }

}
