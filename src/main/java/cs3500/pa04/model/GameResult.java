package cs3500.pa04.model;

/**
 * Enum to show the 3 types of game results.
 */
public enum GameResult {
  WIN("You won the game!"), LOSS("You lost the game!"),
  TIE("All ships have been destroyed, it's a tie!!");
  private final String message;

  GameResult(String message) {
    this.message = message;
  }

  /**
   * Getter for the string message as per the final result
   *
   * @return String message
   */
  public String getMessage() {
    return this.message;
  }
}
