package cs3500.pa04.model;

import cs3500.pa04.controller.Controller;
import cs3500.pa04.model.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Human class representing the human player playing battle salvo
 */
public class Human implements Player {
  private String name;
  private Board humanBoard;
  private List<Coord> shots;

  /**
   * Constructor for human taking inputs from the scanner in the parameter
   *
   */
  public Human() {
    name = "Manual Player";
    this.humanBoard = null;
  }

  /**
   * Get the player's name.
   *
   * @return the player's name
   */
  @Override
  public String name() {
    return name;
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    humanBoard = new Board(height, width);
    humanBoard.setShips(specifications.get(ShipType.CARRIER),
        specifications.get(ShipType.BATTLESHIP), specifications.get(ShipType.DESTROYER),
        specifications.get(ShipType.SUBMARINE));
    return humanBoard.getAssignedShips();
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    int[] dimensions = new int[2];
    dimensions[0] = humanBoard.getHeight();
    dimensions[1] = humanBoard.getWidth();
    Controller controller = new Controller();
    return controller.getAttackCoord(numActiveShips(), dimensions);
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *     ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    ArrayList<Coord> result = new ArrayList<>();
    for (Coord coord : opponentShotsOnBoard) {
      int x = coord.getColumn();
      int y = coord.getRow();
      if (x >= 0 && x < humanBoard.getWidth() && y >= 0 && y < humanBoard.getHeight()) {
        Coord target = humanBoard.getCoordinate(coord);
        target.hit();
        if (target.isOccupied()) {
          result.add(target);
        }
      } else {
        System.out.println("Shot missed: (" + x + ", " + y + ")");
      }
    }
    return result;
  }


  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  @Override
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    // Not needed for the PA3 implementation

  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {

    // Left null for PA04
  }

  /**
   * Tells the number of active ships
   *
   * @return integer value of active ship
   */
  public int numActiveShips() {
    int result = 0;
    for (Ship ship : humanBoard.getAssignedShips()) {
      if (!ship.isSunk()) {
        result++;
      }
    }
    return result;
  }


  /**
   * getter for the board
   *
   * @return the from that getter
   */
  public Board getHumanBoard() {
    return humanBoard;
  }

  /**
   * Checks if all the ships have been lost
   *
   * @return a boolean value
   */
  public boolean checkLoss() {
    return this.numActiveShips() == 0;
  }
}
