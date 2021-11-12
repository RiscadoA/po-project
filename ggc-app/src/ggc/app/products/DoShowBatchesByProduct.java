package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("productKey", Prompt.productKey());
  }

  @Override
  public final void execute() throws CommandException {
    String productKey = stringField("productKey");
    try {
      _display.popup(_receiver.batchesByProduct(productKey));
    } catch (ggc.exceptions.UnknownProductKeyException e) {
      throw new ggc.app.exceptions.UnknownProductKeyException(e.getProductKey());
    }
  }

}
