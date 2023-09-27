package cs3500.pa04.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa04.MockArtIntel;
import cs3500.pa04.MockHuman;
import cs3500.pa04.MockView;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControllerTest {
  Controller controller;
  Controller controller2;
  Scanner scanner;
  MockHuman human;
  MockArtIntel artIntel;
  MockView view;
  InputStream inputStream;

  @BeforeEach
  void setUp() {
    human = new MockHuman();
    artIntel = new MockArtIntel();
    view = new MockView();
    String input = "6 6\n 1 1 1 1 \n" + inputCoord();
    inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    scanner = new Scanner(inputStream);
    controller = new Controller(human, artIntel, view, scanner);
  }

  @Test
  void testCheckDimensions() {
    assertTrue(controller.checkDimensions(6, 6));
    assertFalse(controller.checkDimensions(5, 6));
    assertFalse(controller.checkDimensions(6, 16));
  }

  @Test
  void testCheckFleetSize() {
    Map<ShipType, Integer> validFleet = new HashMap<>();
    validFleet.put(ShipType.CARRIER, 1);
    validFleet.put(ShipType.BATTLESHIP, 1);
    validFleet.put(ShipType.DESTROYER, 1);
    validFleet.put(ShipType.SUBMARINE, 1);
    assertTrue(controller.checkFleetSize(new int[] {6, 6}, validFleet));
    validFleet.put(ShipType.CARRIER, 5);
    assertFalse(controller.checkFleetSize(new int[] {6, 6}, validFleet));
  }

  @Test
  void testCheckCoord() {
    assertTrue(controller.checkCoord(1, 1, new int[] {6, 6}));
    assertFalse(controller.checkCoord(7, 1, new int[] {6, 6}));
    assertFalse(controller.checkCoord(-1, 1, new int[] {6, 6}));
  }

  @Test
  void testGenerateShipPositions() {
    Map<ShipType, Integer> fleet = new HashMap<>();
    fleet.put(ShipType.CARRIER, 1);
    fleet.put(ShipType.BATTLESHIP, 1);
    fleet.put(ShipType.DESTROYER, 1);
    fleet.put(ShipType.SUBMARINE, 1);
    List<Ship> ships = controller.generateShipPositions(fleet);
    assertEquals(4, ships.size());
  }

  /**
   * Write the coordinate inputs as string
   *
   * @return as string
   */
  private String inputCoord() {
    ArrayList<Coord> temp = new ArrayList<Coord>();
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        temp.add(new Coord(i, j));
      }
    }
    StringBuilder result = new StringBuilder();
    for (int a = 0; a < temp.size(); a++) {
      if (a % 4 == 0 && a != 0) {
        result.append("\n");
      }
      result.append(temp.get(a).getColumn() + " " + temp.get(a).getRow() + " ");

    }
    System.out.printf(result.toString());
    return result.toString();
  }
}