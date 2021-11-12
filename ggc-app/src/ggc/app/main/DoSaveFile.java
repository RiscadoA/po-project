package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

import ggc.app.exceptions.FileOpenFailedException;

import ggc.WarehouseManager;
import ggc.exceptions.MissingFileAssociationException;

import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    try {
      try {
        _receiver.save();
      } catch (MissingFileAssociationException e) {
        _receiver.saveAs(Form.requestString(Prompt.newSaveAs()));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
