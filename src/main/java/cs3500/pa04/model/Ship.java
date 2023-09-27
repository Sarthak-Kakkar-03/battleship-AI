package cs3500.pa04.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An object class representing the ship on the Battle salvo board
 */
public class Ship {

  private final int size;
  private String name;
  private boolean sunk;
  private int hit;
  private List<Coord> list;
  private ShipType type;

  Ship(ShipType type) {
    size = type.getSize();
    name = type.getName();
    sunk = false;
    this.list = null;
    hit = 0;
    this.type = type;
  }

  /**
   * Adds to the numbers of spots the ship has been hit with
   */
  private void hit() {
    int result = 0;
    for (Coord point : list) {
      if (point.isHit()) {
        result++;
      }
    }
    this.hit = result;
  }

  /**
   * Decides if the ship has been sunk dependent on the hits suffered
   *
   * @return boolean
   */
  public boolean isSunk() {
    this.hit();
    this.sunk = hit >= this.size;
    return hit >= this.size;
  }

  /**
   * Checks if the provided list of coordinates are of the correct and size
   * Also checks if they are vertical and/or horizontal
   *
   * @param list of coordinates to be assigned
   * @return boolean value
   */
  private boolean validCoordinates(List<Coord> list) {
    boolean horizontal = true;
    boolean vertical = true;
    int x;
    int y;
    if (list.size() == this.size) {
      x = list.get(0).getColumn();
      y = list.get(0).getRow();
      for (Coord coord : list) {
        if (coord.getColumn() != x) {
          vertical = false;
        } else if (coord.getRow() != y) {
          horizontal = false;
        }
      }
    } else {
      return false;
    }
    return vertical || horizontal;
  }

  /**
   * Assigns the list to those coordinates and changes their status to occupied
   *
   * @param list the list to be assigned
   */
  public void setList(List<Coord> list) {
    if (!this.validCoordinates(list)) {
      throw new IllegalArgumentException("The provided list of coordinates is invalid");
    } else {
      this.list = list;
      for (Coord coord : this.list) {
        coord.occupy(this.type);
      }
    }
  }

  /**
   * Getter for size of the ship
   *
   * @return integer size
   */
  public int getSize() {
    return size;
  }

  /**
   * Getter for the name of this ship
   *
   * @return string as name
   */
  public String getName() {
    return name;
  }

  /**
   * If the ship has been sunk
   *
   * @return boolean ship status
   */
  public boolean getSunkStatus() {
    this.isSunk();
    return sunk;
  }

  /**
   * How many times it has been hit
   *
   * @return hit status
   */
  public int getHitStatus() {
    this.hit();
    return hit;
  }

  /**
   * The list of coordinates this ship is on
   *
   * @return a copy array list
   */
  public List<Coord> getList() {
    return new ArrayList<>(list);
  }

  /**
   * The type of this ship
   *
   * @return ShipType
   */
  public ShipType getType() {
    return type;
  }

  /**
   * Gets the x value of all the coordinates
   *
   * @return Array list of integer
   */
  private ArrayList<Integer> getXordinates() {
    ArrayList<Integer> result = new ArrayList<>();
    for (Coord coord : list) {
      result.add(coord.getColumn());
    }
    return result;
  }

  /**
   * Get Y value of all the coordinates
   *
   * @return Array list of integer
   */
  private ArrayList<Integer> getYordinates() {
    ArrayList<Integer> result = new ArrayList<>();
    for (Coord coord : list) {
      result.add(coord.getRow());
    }
    return result;
  }

  /**
   * Tells if the ship is horizontal
   *
   * @return boolean value validating that
   */
  public boolean horizontal() {
    boolean result = true;
    for (Integer value : this.getYordinates()) {
      if (!Objects.equals(this.getYordinates().get(0), value)) {
        result = false;
      }
    }
    return result;
  }


  /**
   * Returns the starting coordinate/point of the ship
   *
   * @return a coord value
   */
  public Coord startingCoord() {
    if (this.horizontal()) {
      int minX = Collections.min(this.getXordinates());
      for (Coord coord : list) {
        if (coord.getColumn() == minX) {
          return coord;
        }
      }
    } else {
      int minY = Collections.min(this.getYordinates());
      for (Coord coord : list) {
        if (coord.getRow() == minY) {
          return coord;
        }
      }
    }
    return null;
  }

  /**
   * Tells if the ship is horizontal or vertical
   *
   * @return boolean value validating that
   */
  public String getDirection() {
    if (horizontal()) {
      return "HORIZONTAL";
    } else {
      return "VERTICAL";
    }
  }




}
