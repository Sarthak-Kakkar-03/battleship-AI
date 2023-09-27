package cs3500.pa04.view;

import cs3500.pa04.model.Board;
import cs3500.pa04.model.GameResult;
import java.util.Arrays;

/**
 * Class with the methods and print statements to control the game view to the user
 */
public class View {
  /**
   * View of a line
   */
  public void line() {
    System.out.println("---------------------------------------------------------------------------"
        + "----------");
  }

  /**
   * View of when the game starts
   */
  public void initGame() {
    System.out.println("Hello! Welcome to the our BattleShip Game! "
        + "Please let us know what you think!");
    System.out.println("Please enter a valid height and width below: ");
    line();
  }

  /**
   * The view when we prompt user to select a fleet
   *
   */
  public void invalidDimensions() {
    line();
    System.out.println("Uh Oh! You've entered invalid dimensions. "
        + "Please remember that the height and width\n"
        + "of the game must be in the range [6, 15], inclusive. Try again!");
    line();
  }

  /**
   * The view when we prompt user to select a fleet
   *
   * @param arg total number of ships we allow the user to choose
   */
  public void fleetSelection(int arg) {
    for (String s : Arrays.asList(
        "Please enter your fleet in the order [Carrier, Battleship, Destroyer, Submarine].",
        "Remember, your fleet may not exceed size " + arg + ".")) {
      System.out.println(s);
    }
    line();
  }

  /**
   * Displays an error message for invalid fleet size.
   */
  public void invalidFleetSize(int arg) {
    line();
    System.out.println("Please enter an Integer for the fleet and");
    fleetSelection(arg);
  }

  /**
   * The view representing the board for battle salvo itself
   *
   * @param human   the board for the human/player
   * @param machine the board for the machine/AI they will be playing against
   */
  public void viewBoard(Board human, Board machine, String playerName, String machineName) {
    representBoard(machine, machineName, true);
    System.out.println();
    representBoard(human, playerName, false);
  }

  /**
   * Prints a single board
   *
   * @param board view for the board
   */
  public void viewBoard(Board board) {
    representBoard(board, "OurRep", false);
  }

  /**
   * Displays a prompt for entering shots.
   *
   * @param n the number of shots to enter
   */
  public void getShots(int n) {
    System.out.println("\nPlease Enter " + n + " Shots:");
    line();
  }

  /**
   * Displays a message for repeating already chosen coordinates.
   *
   * @param coordinates the coordinates that were repeated
   */
  public void repeatCoordinates(String coordinates) {
    System.out.println("Coordinates " + coordinates + " are already chosen!");
    newCoordinates();
  }

  /**
   * Displays a prompt for entering new coordinates.
   */
  public void newCoordinates() {
    System.out.print("New Coordinates: ");
  }

  /**
   * Displays an error message for invalid coordinates.
   *
   * @param dimensions the valid dimensions range
   */
  public void invalidCoordinates(int[] dimensions) {
    System.out.println("Invalid Coordinates! Enter values between " + (dimensions[0] - 1) + " and "
        + (dimensions[1] - 1));
    newCoordinates();
  }

  /**
   * Represents the boards
   *
   * @param contender who's board we are representing
   * @param name      name of the contender
   * @param isMachine if the contender is an AI
   */
  private void representBoard(Board contender, String name, boolean isMachine) {
    for (int i = 0; i < contender.getHeight(); i++) {
      for (int j = 0; j < contender.getWidth(); j++) {
        if (isMachine) {
          if (contender.board[i][j].isHit()) {
            System.out.print(contender.board[i][j].toString() + " ");
          } else {
            System.out.print(0 + " ");
          }
        } else {
          System.out.print(contender.board[i][j].toString() + " ");
        }
      }
      System.out.println();
    }
    System.out.println(name);
  }

  /**
   * Prints the end message for the game
   *
   * @param result end result status
   */
  public void ending(GameResult result) {
    System.out.println(result.getMessage());
  }
}
