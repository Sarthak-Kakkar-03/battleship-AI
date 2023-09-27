package cs3500.pa04;

import cs3500.pa04.model.Coord;
import cs3500.pa04.model.Human;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import java.util.List;
import java.util.Map;

/**
 * Mock class for Human class
 */
public class MockHuman extends Human {
  private boolean isSetupCalled = false;
  private boolean isTakeShotsCalled = false;

  /**
   * Sets the board up for human player
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return a List of ships as setup
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    isSetupCalled = true;
    return super.setup(height, width, specifications);
  }

  /**
   * Returns the shots human took on the board
   *
   * @return a list of coords
   */
  @Override
  public List<Coord> takeShots() {
    isTakeShotsCalled = true;
    return super.takeShots();
  }

  /**
   * Check if setup was called
   *
   * @return boolean value
   */
  public boolean isSetupCalled() {
    return isSetupCalled;
  }

}
