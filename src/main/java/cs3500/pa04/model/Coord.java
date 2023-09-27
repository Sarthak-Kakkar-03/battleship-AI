package cs3500.pa04.model;

/**
 * Coordinates on the board
 */
public class Coord {

  private int row;
  private int column;
  private boolean hit;
  private boolean occupied;
  private ShipType occupier;

  /**
   * Constructor make a coor at a certain position
   *
   * @param row y value
   * @param column x value
   */
  public Coord(int row, int column) {
    this.row = row;
    this.column = column;
    this.hit = false;
    this.occupied = false;
    this.occupier = null;
  }

  /**
   * Returns the x position of a coordinate
   *
   * @return integer
   */
  public int getRow() {
    return this.row;
  }

  /**
   * Return the y position of a coordinate
   *
   * @return integer
   */
  public int getColumn() {
    return this.column;
  }

  /**
   * If the coordinate gets hit, assign the boolean value as hit
   */
  public void hit() {
    hit = true;
  }

  /**
   * Tells if the coordinate has been hit already
   *
   * @return boolean
   */
  public boolean isHit() {
    return hit;
  }

  /**
   * Occupies the coordinate with a ship
   */
  public void occupy(ShipType type) {
    this.occupied = true;
    this.occupier = type;
  }

  /**
   * Tells if there is a ship at that coordinate
   *
   * @return tells if the coordinate has been occupied
   */
  public boolean isOccupied() {
    return this.occupied;
  }

  /**
   * The to String method to represent a coordinate
   *
   * @return strings representation
   */
  @Override
  public String toString() {
    if (this.occupied && !this.hit) {
      return occupier.getRepresentation();
    } else if (this.occupied && this.hit) {
      return "H";
    } else if (!this.occupied && this.hit) {
      return "x";
    } else {
      return "+";
    }
  }


  /**
   * Override the equals method for 2 coordinates
   *
   * @param obj The coordinate to compare equals to
   * @return boolean validating the result
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coord)) {
      return false;
    } else {
      return this.row == ((Coord) obj).row && this.column == ((Coord) obj).column;
    }
  }

  /**
   * Override the hashcode method to adapt to the equals method
   *
   * @return int value of this hashcode
   */
  @Override
  public int hashCode() {
    return this.row * 7 + this.column * 89;
  }

  /**
   * Returns a string representation of the coordinates.
   *
   * @return a string representation of the coordinates
   */
  public String getCoordinates() {
    return "[" + getRow() + "," + getColumn() + "]";
  }
}
