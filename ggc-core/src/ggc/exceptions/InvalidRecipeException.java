package ggc.exceptions;

/**
 * Class for representing an invalid recipe error. Thrown when the a recipe is
 * constructed with an 'products' array that does not match the 'amounts' array
 * in size.
 */
public class InvalidRecipeException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202110180909L;

  /**
   * Default constructor
   */
  public InvalidRecipeException() {
    // do nothing
  }
}
