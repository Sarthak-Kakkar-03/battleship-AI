package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArtIntelTest {
  ArtIntel artIntel;

  @BeforeEach
  void setUp() {
    artIntel = new ArtIntel();
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);
    artIntel.setup(10, 10, map);
  }

  @Test
  void setLoss() {

    List<Ship> ships = artIntel.getBoard().getAssignedShips();

    for (Ship ship : ships) {
      List<Coord> coords = ship.getList();

      for (Coord coord : coords) {
        List<Coord> opponentShotsOnBoard = new ArrayList<>();
        opponentShotsOnBoard.add(coord);


        artIntel.reportDamage(opponentShotsOnBoard);
      }

      assertTrue(ship.isSunk());
    }
  }

  @Test
  void name() {
    assertEquals("Salvo AI", artIntel.name());

  }

  @Test
  void setup() {
    assertEquals(4, artIntel.getBoard().getAssignedShips().size());

  }

  @Test
  void takeShots() {
    List<Coord> tester = artIntel.takeShots();
    assertEquals(4, tester.size());
    boolean check = true;
    for (Coord coord : tester) {
      if (coord.getColumn() > 10 || coord.getRow() > 10) {
        check = false;
      }
    }
    assertTrue(check);
  }

  @Test
  void reportDamage() {
    List<Coord> opponentShotsOnBoard = new ArrayList<>();
    Coord coord1 = new Coord(1, 1);
    Coord coord2 = new Coord(2, 2);
    opponentShotsOnBoard.add(coord1);
    opponentShotsOnBoard.add(coord2);

    artIntel.reportDamage(opponentShotsOnBoard);
    Assertions.assertTrue(artIntel.getBoard().getCoordinate(coord1).isHit());
    Assertions.assertTrue(artIntel.getBoard().getCoordinate(coord2).isHit());

  }

  @Test
  public void testIsValidCoordinate() throws Exception {
    Coord coord1 = new Coord(1, 2);
    Coord coord2 = new Coord(1, 2);
    Coord coord3 = new Coord(11, 12);
    Coord coord4 = new Coord(15, 12);
    assertEquals(true, artIntel.isValidCoordinate(coord1.getRow(), coord1.getColumn()));
    assertEquals(true, artIntel.isValidCoordinate(coord2.getRow(), coord2.getColumn()));
    assertEquals(false, artIntel.isValidCoordinate(coord3.getRow(), coord3.getColumn()));
    assertEquals(false, artIntel.isValidCoordinate(coord4.getRow(), coord4.getColumn()));
  }

  @Test
  void getNeighbors() {
    Coord coord = new Coord(5, 5); // for a board of size greater than 5x5
    List<Coord> neighbors = artIntel.getNeighbors(coord);

    assertTrue(neighbors.contains(new Coord(5, 6))); // Right neighbor
    assertTrue(neighbors.contains(new Coord(5, 4))); // Left neighbor
    assertTrue(neighbors.contains(new Coord(6, 5))); // Down neighbor
    assertTrue(neighbors.contains(new Coord(4, 5))); // Up neighbor

    assertEquals(4, neighbors.size()); // A Coord in the middle of the board should have 4 neighbors
  }

  @Test
  void successfulHits() {
    List<Coord> shots = new ArrayList<>();
    Coord coord1 = new Coord(1, 1);
    Coord coord2 = new Coord(2, 2);
    shots.add(coord1);
    shots.add(coord2);
    artIntel.successfulHits(shots);
    Assertions.assertFalse(artIntel.getLatestHits().size() == 0);

    // Make assertions here. You can check if 'artIntel' has updated its state correctly
    // based on the shots that hit.
  }

  @Test
  void endGame() {
  }

  @Test
  void numActiveShips() {
    assertEquals(4, artIntel.numActiveShips());
    List<Ship> ships = artIntel.getBoard().getAssignedShips();

    for (Ship ship : ships) {
      List<Coord> coords = ship.getList();

      for (Coord coord : coords) {
        List<Coord> opponentShotsOnBoard = new ArrayList<>();
        opponentShotsOnBoard.add(coord);


        artIntel.reportDamage(opponentShotsOnBoard);
      }
    }
    assertEquals(artIntel.numActiveShips(), 0);
  }

  @Test
  void getBoard() {
    assertNotNull(artIntel.getBoard());
  }

  @Test
  void checkLoss() {
    assertFalse(artIntel.checkLoss());
    List<Ship> ships = artIntel.getBoard().getAssignedShips();

    for (Ship ship : ships) {
      List<Coord> coords = ship.getList();

      for (Coord coord : coords) {
        List<Coord> opponentShotsOnBoard = new ArrayList<>();
        opponentShotsOnBoard.add(coord);


        artIntel.reportDamage(opponentShotsOnBoard);
      }
    }
    assertTrue(artIntel.checkLoss());
  }
}