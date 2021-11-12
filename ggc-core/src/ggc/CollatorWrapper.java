package ggc;

import java.text.Collator;

import java.util.Comparator;
import java.util.Locale;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Wrapper around the Collator class which allows it to be serialized.
 */
class CollatorWrapper implements Comparator<String>, Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110261856L;

  private transient Collator _collator;

  public CollatorWrapper() {
    this._collator = Collator.getInstance(Locale.getDefault());
    this._collator.setStrength(Collator.SECONDARY);
  }

  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    ois.defaultReadObject();
    this._collator = Collator.getInstance(Locale.getDefault());
    this._collator.setStrength(Collator.SECONDARY);
  }

  @Override
  public int compare(String s1, String s2) {
    return this._collator.compare(s1, s2);
  }
}
