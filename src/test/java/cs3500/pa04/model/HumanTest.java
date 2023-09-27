package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HumanTest {
  Human human;

  @BeforeEach
  void setUp() {
    human = new Human();
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);
    human.setup(10, 10, map);
  }

  @Test
  void name() {
    assertEquals("Manual Player", human.name());
  }

  @Test
  void setup() {
    assertNotNull(human.getHumanBoard());
    assertEquals(4, human.getHumanBoard().getAssignedShips().size());
  }

  @Test
  void reportDamage() {
  }

  @Test
  void successfulHits() {
  }

  @Test
  void endGame() {
  }

  @Test
  void numActiveShips() {
    assertEquals(4, human.numActiveShips());
    List<Ship> ships = human.getHumanBoard().getAssignedShips();

    for (Ship ship : ships) {
      List<Coord> coords = ship.getList();

      for (Coord coord : coords) {
        List<Coord> opponentShotsOnBoard = new ArrayList<>();
        opponentShotsOnBoard.add(coord);


        human.reportDamage(opponentShotsOnBoard);
      }
    }
    assertEquals(human.numActiveShips(), 0);
  }

  @Test
  void getHumanBoard() {
    assertNotNull(human.getHumanBoard());
  }

  @Test
  void checkLoss() {
    assertFalse(human.checkLoss());
    List<Ship> ships = human.getHumanBoard().getAssignedShips();

    for (Ship ship : ships) {
      List<Coord> coords = ship.getList();

      for (Coord coord : coords) {
        List<Coord> opponentShotsOnBoard = new ArrayList<>();
        opponentShotsOnBoard.add(coord);


        human.reportDamage(opponentShotsOnBoard);
      }
    }
    assertTrue(human.checkLoss());
  }
}