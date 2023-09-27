package cs3500.pa04;

import cs3500.pa04.model.Board;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.view.View;

/**
 * Mock view for testing
 */
public class MockView extends View {
  StringBuilder outputLog;

  /**
   * Initailzie the mock view
   */
  public MockView() {
    outputLog = new StringBuilder();
  }

  /**
   * Check for init game
   */
  @Override
  public void initGame() {
    outputLog.append("initGame called.\n");
  }

  /**
   * Check for invalid dimensions
   */
  @Override
  public void invalidDimensions() {
    outputLog.append("invalidDimensions was called.\n");
  }

  // Continue to override other methods in the same way

  /**
   * Check for view board
   *
   * @param human   the board for the human/player
   * @param machine the board for the machine/AI they will be playing against
   * @param playerName name of the player
   * @param machineName name of the machine
   */
  @Override
  public void viewBoard(Board human, Board machine, String playerName, String machineName) {
    outputLog.append("viewBoard: "
        + human + ", " + machine + ", " + playerName + ", " + machineName + ".\n");
  }

  /**
   * Checks for ending view
   *
   * @param result end result status
   */
  @Override
  public void ending(GameResult result) {
    outputLog.append("ending was called with parameter: " + result + ".\n");
  }

  /**
   * Checks for final log
   *
   * @return string output
   */
  public String getLog() {
    return outputLog.toString();
  }
}
