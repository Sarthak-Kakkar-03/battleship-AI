package cs3500.pa04.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ShipTypeTest {

  @Test
  void getName() {
    assertEquals("Carrier", ShipType.CARRIER.getName());
    assertEquals("Battleship", ShipType.BATTLESHIP.getName());
    assertEquals("Destroyer", ShipType.DESTROYER.getName());
    assertEquals("Submarine", ShipType.SUBMARINE.getName());


  }

  @Test
  void getSize() {
    assertEquals(6, ShipType.CARRIER.getSize());
    assertEquals(4, ShipType.DESTROYER.getSize());
    assertEquals(3, ShipType.SUBMARINE.getSize());
    assertEquals(5, ShipType.BATTLESHIP.getSize());

  }

  @Test
  void getRepresentation() {
    assertEquals("C", ShipType.CARRIER.getRepresentation());
    assertEquals("B", ShipType.BATTLESHIP.getRepresentation());
    assertEquals("D", ShipType.DESTROYER.getRepresentation());
    assertEquals("S", ShipType.SUBMARINE.getRepresentation());

  }

}