package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
//CHECK imported classes

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("days", Prompt.daysToAdvance());
  }

  @Override
  public final void execute() throws CommandException {
    int days = integerField("days");
    try {
      _receiver.advanceDate(days);
    } catch (ggc.exceptions.InvalidDateException ide) {
      throw new ggc.app.exceptions.InvalidDateException(ide.getDate());
    }
  }
}
