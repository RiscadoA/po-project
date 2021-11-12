package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String partnerKey = stringField("partnerKey");
    try {
      _display.popup(_receiver.partnerSalesAndBreakdowns(partnerKey));
    } catch (ggc.exceptions.UnknownPartnerKeyException e) {
      throw new ggc.app.exceptions.UnknownPartnerKeyException(e.getPartnerKey());
    }
  }

}
