package cs3500.pa04.model;






import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoordTest {

  private Coord coord;

  @BeforeEach
  void setUp() {
    coord = new Coord(5, 10);
  }

  @Test
  void getX() {
    assertEquals(10, coord.getColumn());
  }

  @Test
  void getY() {
    assertEquals(5, coord.getRow());
  }

  @Test
  void hit() {
    assertFalse(coord.isHit());
    coord.hit();
    assertTrue(coord.isHit());
  }

  @Test
  void isHit() {
    assertFalse(coord.isHit());
    coord.hit();
    assertTrue(coord.isHit());
  }

  @Test
  void occupy() {
    assertFalse(coord.isOccupied());
    coord.occupy(ShipType.SUBMARINE); // Assuming ShipType.CRUISER exists in your enum
    assertTrue(coord.isOccupied());
  }

  @Test
  void isOccupied() {
    assertFalse(coord.isOccupied());
    coord.occupy(ShipType.SUBMARINE); // Assuming ShipType.CRUISER exists in your enum
    assertTrue(coord.isOccupied());
  }

  @Test
  void testToString() {
    assertEquals("+", coord.toString());
    coord.hit();
    assertEquals("x", coord.toString());
    coord = new Coord(5, 10);
    coord.occupy(ShipType.SUBMARINE); // Assuming ShipType.CRUISER exists in your enum
    assertEquals("S", coord.toString()); // Assuming "C" is the representation of the Cruiser
    coord.hit();
    assertEquals("H", coord.toString());
  }

  @Test
  void testEquals() {
    Coord otherCoord = new Coord(5, 10);
    assertEquals(coord, otherCoord);
    otherCoord = new Coord(10, 5);
    assertNotEquals(coord, otherCoord);
  }

  @Test
  void testHashCode() {
    Coord otherCoord = new Coord(5, 10);
    assertEquals(coord.hashCode(), otherCoord.hashCode());
    otherCoord = new Coord(10, 5);
    assertNotEquals(coord.hashCode(), otherCoord.hashCode());
  }
}