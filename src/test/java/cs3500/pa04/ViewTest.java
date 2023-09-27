package cs3500.pa04;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa04.model.Board;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.view.View;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewTest {
  ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  PrintStream originalOut = System.out;
  View view;
  Board board;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
    view = new View();
    board = new Board(6, 6);
  }

  @Test
  void testLine() {
    view.line();
    assertEquals("---------------------------------------------------------------------------"
        + "----------\n", outContent.toString());
  }

  @Test
  void testInvalidDimensions() {
    view.invalidDimensions();
    String expectedOutput = "-------------------------------------------------------------------------------------\n"
        + "Uh Oh! You've entered invalid dimensions. "
        + "Please remember that the height and width\n"
        + "of the game must be in the range [6, 15], inclusive. Try again!\n"
        + "-------------------------------------------------------------------------------------\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void testInvalidFleetSize() {
    view.invalidFleetSize(5);
    String expectedOutput = "-------------------------------------------------------------------------------------\n"
        + "Please enter an Integer for the fleet and\n"
        + "Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].\n"
        + "Remember, your fleet may not exceed size 5.\n"
        + "-------------------------------------------------------------------------------------\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void testViewSingleBoard() {
    view.viewBoard(board);
    // Assuming board's toString() implementation returns a string of 0s for an empty 6x6 board
    String expectedOutput = "+ + + + + + \n".repeat(6) + "OurRep\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void testGetShots() {
    view.getShots(3);
    String expectedOutput = "\nPlease Enter 3 Shots:\n"
        + "-------------------------------------------------------------------------------------\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void testRepeatCoordinates() {
    view.repeatCoordinates("1,2");
    String expectedOutput = "Coordinates 1,2 are already chosen!\nNew Coordinates: ";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void testNewCoordinates() {
    view.newCoordinates();
    String expectedOutput = "New Coordinates: ";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void testInvalidCoordinates() {
    view.invalidCoordinates(new int[]{6,6});
    String expectedOutput = "Invalid Coordinates! Enter values between 5 and 5\nNew Coordinates: ";
    assertEquals(expectedOutput, outContent.toString());
  }

  @AfterEach
  void restoreStreams() {
    System.setOut(originalOut);
  }

  @Test
  void initGame() {
    view.initGame();
    String expectedOutput = "Hello! Welcome to the our BattleShip Game! "
        + "Please let us know what you think!\n"
        + "Please enter a valid height and width below: \n"
        + "-------------------------------------------------------------------------------------\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void fleetSelection() {
    View view = new View();
    view.fleetSelection(5);
    String expectedOutput = "Please enter your fleet in the order [Carrier, Battleship, "
        + "Destroyer, Submarine].\n"
        + "Remember, your fleet may not exceed size 5.\n"
        + "-------------------------------------------------------------------------------------\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void ending() {
    view.ending(GameResult.WIN);
    String expectedOutput = "You won the game!\n";
    assertEquals(expectedOutput, outContent.toString());
  }

  @Test
  void viewBoard() {
    view.viewBoard(board, board, "Player", "AI");
    assertTrue(outContent.toString().contains("0"));
  }
}