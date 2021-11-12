package ggc.app.transactions;

import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import ggc.WarehouseManager;
import ggc.exceptions.DuplicateProductKeyException;
import ggc.exceptions.InvalidRecipeException;

/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("partnerKey", Prompt.partnerKey());
    addStringField("productKey", Prompt.productKey());
    addRealField("price", Prompt.price());
    addIntegerField("amount", Prompt.amount());
  }

  @Override
  public final void execute() throws CommandException {
    String partneyKey = stringField("partnerKey");
    String productKey = stringField("productKey");
    double price = realField("price");
    int amount = integerField("amount");

    try {
      try {
        // Register acquisition
        _receiver.registerAcquisition(partneyKey, productKey, amount, price);
      } catch (ggc.exceptions.UnknownProductKeyException e) {
        // The produt wasn't registered before, register it now
        if (Form.confirm(Prompt.addRecipe())) {
          // Request recipe
          int componentCount = Form.requestInteger(Prompt.numberOfComponents());
          double aggravation = Form.requestReal(Prompt.alpha());

          String componentKeys[] = new String[componentCount];
          int componentAmounts[] = new int[componentCount];
          for (int i = 0; i < componentCount; i++) {
            componentKeys[i] = Form.requestString(Prompt.productKey());
            componentAmounts[i] = Form.requestInteger(Prompt.amount());
          }

          // Register derivate product
          _receiver.registerProduct(productKey, aggravation, componentKeys, componentAmounts);
        } else
          // Register simple product
          _receiver.registerProduct(productKey);

        // Register acquisition
        _receiver.registerAcquisition(partneyKey, productKey, amount, price);
      }
    } catch (ggc.exceptions.UnknownPartnerKeyException e) {
      throw new ggc.app.exceptions.UnknownPartnerKeyException(e.getPartnerKey());
    } catch (ggc.exceptions.UnknownProductKeyException e) {
      throw new ggc.app.exceptions.UnknownProductKeyException(e.getProductKey());
    } catch (DuplicateProductKeyException | InvalidRecipeException e) {
      e.printStackTrace(); // This should never happen.
    }
  }
}
