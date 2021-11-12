package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    addStringField("key", Prompt.partnerKey());
    addStringField("name", Prompt.partnerName());
    addStringField("address", Prompt.partnerAddress());
  }

  @Override
  public void execute() throws CommandException {
    String key = stringField("key");
    String name = stringField("name");
    String address = stringField("address");
    try {
      _receiver.registerPartner(key, name, address);
    } catch (ggc.exceptions.DuplicatePartnerKeyException e) {
      throw new ggc.app.exceptions.DuplicatePartnerKeyException(e.getPartnerKey());
    }
  }
}
