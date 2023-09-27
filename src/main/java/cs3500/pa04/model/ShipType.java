package cs3500.pa04.model;

/**
 * Enum representing the 4 types of ships
 */
public enum ShipType {

  CARRIER(6, "Carrier"),
  BATTLESHIP(5, "Battleship"),
  DESTROYER(4, "Destroyer"),
  SUBMARINE(3, "Submarine");

  private final int size;

  private final String name;

  ShipType(int size, String name) {
    this.size = size;
    this.name = name;
  }

  /**
   * Getter for the name
   *
   * @return String as name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Getter for size of the ship
   *
   * @return Int as size
   */
  public int getSize() {
    return this.size;
  }

  /**
   * Gives the first letter as the representation of that kind of ship
   *
   * @return String representation
   */
  public String getRepresentation() {
    return this.name.substring(0, 1);
  }
}
