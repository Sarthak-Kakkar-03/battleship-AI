package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Artificial intelligence that plays against the player
 */
public class ArtIntel implements Player {
  private final String name;
  private Board board;
  private List<Coord> opponentCoordinates;
  private List<Coord> mutableOpponentCoordinates;
  private List<Coord> alreadyHit = new ArrayList<>();

  private List<Coord> latestHits = new ArrayList<>();
  private GameResult result;
  private String reason;
  private boolean end;

  /**
   * Public constructor for the artificial intelligence the player plays against
   */
  public ArtIntel() {
    this.name = "Salvo AI";
    board = null;
  }

  /**
   * Constructor to create an ArtIntel fighter with a name
   *
   * @param name the name we want to give him
   */
  public ArtIntel(String name) {
    this.name = name;
    board = null;
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
    board = new Board(height, width);
    board.setShips(specifications.get(ShipType.CARRIER),
        specifications.get(ShipType.BATTLESHIP), specifications.get(ShipType.DESTROYER),
        specifications.get(ShipType.SUBMARINE));
    this.opponentCoordinates = setOpponentCoordinates(height, width);
    this.mutableOpponentCoordinates = new ArrayList<>(opponentCoordinates);
    return board.getAssignedShips();
  }

  /**
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    List<Coord> result = new ArrayList<>();
    int numShips = this.numActiveShips();
    if (!latestHits.isEmpty()) {
      for (Coord hit : latestHits) {
        List<Coord> neighbors = getNeighbors(hit);
        neighbors.removeIf(coord -> !mutableOpponentCoordinates.contains(coord)
            || alreadyHit.contains(coord));
        if (!neighbors.isEmpty()) {
          for (Iterator<Coord> iterator = neighbors.iterator(); iterator.hasNext()
              && result.size() < numShips; ) {
            Coord target = iterator.next();
            result.add(target);
            mutableOpponentCoordinates.remove(target);
            alreadyHit.add(target);
          }
          if (result.size() == numShips) {
            return result;
          }
        }
      }
      latestHits.clear();
    }
    while (result.size() < numShips) {
      if (mutableOpponentCoordinates.isEmpty()) {
        break;
      }
      Coord target = mutableOpponentCoordinates.get(new Random()
          .nextInt(mutableOpponentCoordinates.size()));
      result.add(target);
      mutableOpponentCoordinates.remove(target);
      alreadyHit.add(target);
    }
    return result;
  }


  /**
   * Get neighboring cells of a given cell in the grid.
   *
   * @param hit the coordinate of a hit cell
   * @return a list of coordinates of the neighbors
   */
  public List<Coord> getNeighbors(Coord hit) {
    List<Coord> neighbors = new ArrayList<>();
    int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}}; // Right, Left, Down, Up

    for (int[] dir : dirs) {
      int newX = hit.getRow() + dir[0];
      int newY = hit.getColumn() + dir[1];

      if (isValidCoordinate(newX, newY)) {
        neighbors.add(new Coord(newX, newY));
      }
    }
    return neighbors;
  }

  /**
   * Check if a given cell coordinate is valid.
   *
   * @param row the row index
   * @param column the column index
   * @return true if the cell coordinate is valid, false otherwise
   */
  public boolean isValidCoordinate(int row, int column) {
    return row >= 0 && row < board.getHeight() && column >= 0 && column < board.getWidth();
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
      board.getCoordinate(coord).hit();
      if (board.getCoordinate(coord).isOccupied()) {
        result.add(board.getCoordinate(coord));
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
    latestHits = new ArrayList<>(shotsThatHitOpponentShips);
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
    this.result = result;
    this.reason = reason;
    this.end = true;
  }

  /**
   * Tells the number of active ships
   *
   * @return integer value of active ship
   */
  public int numActiveShips() {
    int result = 0;
    for (Ship ship : board.getAssignedShips()) {
      if (!ship.isSunk()) {
        result++;
      }
    }
    return result;
  }

  /**
   * Getter for the current state of the board
   *
   * @return board for the AI
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Checks if the all ships have been lost
   *
   * @return boolean value
   */
  public boolean checkLoss() {
    return this.numActiveShips() == 0;
  }

  /**
   * Setter to be called in the setup method
   * creates a list of the opponents coords
   *
   * @param height height of the board as int
   * @param width width of the board as int
   * @return a list of coordinates
   */
  private List<Coord> setOpponentCoordinates(int height, int width) {
    List<Coord> result = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        result.add(new Coord(i, j));
      }
    }
    return result;
  }

  /**
   * Getter for the latest hits
   *
   * @return the shots hits received in the last round
   */
  public List<Coord> getLatestHits() {
    return latestHits;
  }

  /**
   * Game result getter
   *
   * @return the enum Game Result
   */
  public GameResult getGameResult() {
    return this.result;
  }

  /**
   * Gives the reason why the game was lost
   *
   * @return a string explaining reason
   */
  public String getReason() {
    return this.reason;
  }
}
