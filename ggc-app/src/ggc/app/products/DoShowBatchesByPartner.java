package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
  }

  @Override
  public final void execute() throws CommandException {
    String partnerKey = stringField("partnerKey");
    try {
      _display.popup(_receiver.batchesByPartner(partnerKey));
    } catch (ggc.exceptions.UnknownPartnerKeyException e) {
      throw new ggc.app.exceptions.UnknownPartnerKeyException(e.getPartnerKey());
    }
  }

}
