package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The board of coords that carries the ships
 */
public class Board {
  private int height;
  private int width;
  private boolean shipCheck;
  public Coord[][] board;
  private ArrayList<Ship> ships;

  /**
   * Constructor to initialize a board
   *
   * @param height int value of height of the board
   * @param width int value of width of the board
   */
  public Board(int height, int width) {
    if (height < 6 || height > 15 || width < 6 || width > 15) {
      throw new IllegalArgumentException("Cannot initialize a board of invalid dimensions");
    }
    this.height = height;
    this.width = width;
    this.board = new Coord[height][width];
    this.ships = new ArrayList<>();
    this.shipCheck = false;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        board[i][j] = new Coord(i, j);
      }
    }
  }

  /**
   * Maxes out the number of allowed ships on the board using the smallest of either dimensions
   *
   * @return an int on the max number of Ships
   */
  private int assignShipLimit() {
    return Math.min(width, height);
  }

  /**
   * Sets the total fleet of ships on this board
   * Assigns the ships on the relevant coordinates on the board
   *
   * @param carrier    number of carrier ships
   * @param battleShip number of battleShips
   * @param destroyer  number of destroyers
   * @param submarine  number of submarines
   */
  public void setShips(int carrier, int battleShip, int destroyer, int submarine) {
    int totalShips = carrier + battleShip + destroyer + submarine;
    if (totalShips > this.assignShipLimit()) {
      throw new IllegalArgumentException("Total number of ships exceeds board limit");
    }
    for (int i = 0; i < carrier; i++) {
      ships.add(new Ship(ShipType.CARRIER));
    }
    for (int i = 0; i < battleShip; i++) {
      ships.add(new Ship(ShipType.BATTLESHIP));
    }
    for (int i = 0; i < destroyer; i++) {
      ships.add(new Ship(ShipType.DESTROYER));
    }
    for (int i = 0; i < submarine; i++) {
      ships.add(new Ship(ShipType.SUBMARINE));
    }
    this.shipCheck = true;
    this.assignShips();
  }

  /**
   * Assigns the ships to the relevant coordinates
   */
  private void assignShips() {
    Random row = new Random();
    Random col = new Random();
    for (Ship ship : ships) {
      boolean placed = false;
      for (int attempt = 0; attempt < 10000 && !placed; attempt++) {
        int rowNum = row.nextInt(height);
        int colNum = col.nextInt(width);
        if (!board[rowNum][colNum].isOccupied()) {
          if (horizontalVacancy(rowNum, colNum, ship)) {
            ArrayList<Coord> temp = new ArrayList<>();
            for (int a = 0; a < ship.getSize(); a++) {
              Coord focus = board[rowNum][colNum + a];
              focus.occupy(ship.getType());
              temp.add(focus);
            }
            placed = true;
            ship.setList(temp);
            /*
            System.out.println("Height: " + this.height + ", Width: " + width);
            for (Coord coord : temp) {
              System.out.print("Horizontal: " + "(" + coord.getX() + ", "+  coord.getY() + ")");
            }
             */
          } else if (verticalVacancy(rowNum, colNum, ship)) {
            ArrayList<Coord> temp = new ArrayList<>();
            for (int a = 0; a < ship.getSize(); a++) {
              Coord focus = board[rowNum + a][colNum];
              focus.occupy(ship.getType());
              temp.add(focus);
            }
            placed = true;
            /*
            for (Coord coord : temp) {
              System.out.print("Vertical: " +  "(" + coord.getX() + ", "+  coord.getY() + ")");
            }
             */
            ship.setList(temp);
          }
        }
      }
      if (!placed) {
        throw new IllegalStateException("Could not place all ships after 1000 attempts.");
      }
    }
  }


  /**
   * Checks if there is space available for the given ship after that coordinate index point
   *
   * @param col  The horizontal point for that coordinate
   * @param ship the ship to be placed
   * @return a boolean value if a horizontal ship there is viable
   */
  private boolean horizontalCheck(int col, Ship ship) {
    return col + ship.getSize() <= this.width;
  }

  /**
   * Checks if there is vertical space available for the given ship
   *
   * @param row  the vertical index of the board
   * @param ship the ship to be placed
   * @return boolean value confirming viability
   */
  private boolean verticalCheck(int row, Ship ship) {
    return row + ship.getSize() <= this.height;
  }

  /**
   * Checks if it is horizontally viable to place a ship at that point
   *
   * @param row  coordinate of the point
   * @param col  the horizontal pointer
   * @param ship the ship to be placed
   * @return a boolean value representing viability
   */
  private boolean horizontalVacancy(int row, int col, Ship ship) {
    if (!horizontalCheck(col, ship)) {
      return false;
    }
    if (horizontalCheck(col, ship)) {
      for (int i = 0; i < ship.getSize(); i++) {
        if (board[row][col + i].isOccupied()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Checks if it is viable to place a ship vertically from those points
   *
   * @param row  the vertical pointer
   * @param col  coordinate of the point
   * @param ship the ship to be placed
   * @return a boolean value as per the viability
   */
  private boolean verticalVacancy(int row, int col, Ship ship) {
    if (!verticalCheck(row, ship)) {
      return false;
    }
    if (verticalCheck(row, ship)) {
      for (int i = 0; i < ship.getSize(); i++) {
        if (board[row + i][col].isOccupied()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Returns the assigned Ships from the board
   *
   * @return assigned ships from the board
   */
  public List<Ship> getAssignedShips() {
    if (this.shipCheck) {
      return this.ships;
    } else {
      throw new IllegalArgumentException("Set ships in board class hasn't been called yet!!");
    }
  }

  /**
   * Getter to obtain height of the board
   *
   * @return integer value of height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Getter to obtain width of board
   *
   * @return width as height
   */
  public int getWidth() {
    return width;
  }

  /**
   * Gets the coordinate on the relevant x y position
   *
   * @param x int row position
   * @param y int column position
   * @return coordinate on that position
   */
  public Coord getCoordinate(int x, int y) {
    if (x >= width || y >= height) {
      throw new IllegalArgumentException("This coordinate does not exist on this board");
    } else {
      return board[x][y];
    }
  }

  /**
   * Overloaded version of get coordinate that will the find at position of the given coordinate
   *
   * @param obj given coordinate
   * @return Coordinate at the same position as the wanted one
   */
  public Coord getCoordinate(Coord obj) {
    if (obj.getColumn() >= width || obj.getRow() >= height) {
      throw new IllegalArgumentException("This coordinate does not exist on this board");
    } else {
      return board[obj.getRow()][obj.getColumn()];
    }

  }

}
