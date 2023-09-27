package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipTest {
  ArrayList<Coord> coords;
  Coord coord1;
  Coord coord2;
  Coord coord3;
  Ship testObj;
  ArrayList<Coord> coords2;
  Coord coord4;
  Coord coord5;
  Coord coord6;
  Ship testObj2;




  @BeforeEach
  void setup() {
    coords = new ArrayList<>();
    coord1 = new Coord(0, 1);
    coord2 = new Coord(0, 2);
    coord3 = new Coord(0, 3);
    coords.add(coord1);
    coords.add(coord2);
    coords.add(coord3);
    testObj = new Ship(ShipType.SUBMARINE);
    coords2 = new ArrayList<>();
    coord4 = new Coord(1, 0);
    coord5 = new Coord(2, 0);
    coord6 = new Coord(3, 0);
    coords2.add(coord4);
    coords2.add(coord5);
    coords2.add(coord6);
    testObj2 = new Ship(ShipType.SUBMARINE);

  }


  @Test
  void hit() {
    testObj.setList(coords);
    coord1.hit();
    assertEquals(1, testObj.getHitStatus());
  }

  @Test
  void isSunk() {
    testObj.setList(coords);
    coord1.hit();
    assertFalse(testObj.isSunk());
    coord2.hit();
    coord3.hit();
    assertTrue(testObj.isSunk());
  }

  @Test
  void setList() {
    testObj.setList(coords);
    assertEquals(testObj.getList().size(), testObj.getSize());

  }

  @Test
  void getSize() {
    testObj.setList(coords);
    assertEquals(testObj.getList().size(), testObj.getSize());
  }

  @Test
  void getName() {
    assertEquals("Submarine", testObj.getName());
  }

  @Test
  void getSunkStatus() {
    testObj.setList(coords);
    coord1.hit();
    coord2.hit();
    coord3.hit();
    assertTrue(testObj.getSunkStatus());
  }

  @Test
  void getHitStatus() {
    testObj.setList(coords);
    coord1.hit();
    coord2.hit();
    assertEquals(2, testObj.getHitStatus());
  }

  @Test
  void getList() {
    testObj.setList(coords);
    assertEquals(testObj.getList(), coords);
  }

  @Test
  void getType() {
    assertEquals(testObj.getType(), ShipType.SUBMARINE);
  }

  @Test
  void horizontalTest() {
    testObj.setList(coords);
    assertTrue(testObj.horizontal());
  }

  @Test
  void startingCoord() {
    testObj.setList(coords);
    assertEquals(coord1, testObj.startingCoord());
    testObj2.setList(coords2);
    assertEquals(coord4, testObj2.startingCoord());

  }
}