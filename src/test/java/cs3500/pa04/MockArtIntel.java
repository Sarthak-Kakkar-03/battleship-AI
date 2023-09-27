package cs3500.pa04;

import cs3500.pa04.model.ArtIntel;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import java.util.List;
import java.util.Map;

/**
 * Mock art Intel class for testing
 */
public class MockArtIntel extends ArtIntel {
  public boolean setUpCall = false;
  public boolean takeShotsCall = false;
  public boolean reportDamageCall = false;

  /**
   * Override setup for AI to see if it was called
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return List of ships
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    setUpCall = true;
    return super.setup(height, width, specifications);
  }

  /**
   * Take shots mock
   *
   * @return boolean value and list of coords
   */
  @Override
  public List<Coord> takeShots() {
    takeShotsCall = true;
    return super.takeShots();
  }

  /**
   * Report damage mock method
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return list of coords
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    reportDamageCall = true;
    return super.reportDamage(opponentShotsOnBoard);
  }


}
