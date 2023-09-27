package cs3500.pa04.controller;


import cs3500.pa04.model.ArtIntel;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.Human;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import cs3500.pa04.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.annotation.processing.Generated;

/**
 * Controller class to work between the view and the model
 */
public class Controller {

  Human human;
  ArtIntel artIntel1;
  View view;
  Scanner scanner;
  ArtIntel artIntel2;

  /**
   * Constructor for the controller
   */
  public Controller() {
    scanner = new Scanner(System.in);
    artIntel1 = new ArtIntel();
    view = new View();
    human = new Human();
    artIntel2 = new ArtIntel();
  }

  /**
   * Constructor for mockito testing
   *
   * @param human    Mock human object
   * @param artIntel Mock artIntel object
   * @param view     Mock view object
   * @param scanner  mock scanner object
   */
  public Controller(Human human, ArtIntel artIntel, View view, Scanner scanner) {
    this.human = human;
    this.artIntel1 = artIntel;
    this.view = view;
    this.scanner = scanner;
  }


  /**
   * Starts the game using relevant model and view methods
   */
  public void startGame() {
    int[] dimensions = setDimensions();
    Map<ShipType, Integer> specifications = getShips(dimensions);
    setupPlayers(dimensions, specifications);
    playGame(dimensions);
    endGame();
  }

  /**
   * Retrieves the specifications for the player's fleet.
   *
   * @return a map containing the ship types and their corresponding quantities
   */
  public Map<ShipType, Integer> getShips(int[] dimensions) {
    Map<ShipType, Integer> ships = new HashMap<>();

    view.fleetSelection(Math.min(dimensions[0], dimensions[1]));
    while (true) {
      try {
        ships.put(ShipType.CARRIER, getIntInput());
        ships.put(ShipType.BATTLESHIP, getIntInput());
        ships.put(ShipType.DESTROYER, getIntInput());
        ships.put(ShipType.SUBMARINE, getIntInput());

        if (checkFleetSize(dimensions, ships)) {
          break;
        } else {
          view.invalidFleetSize(Math.min(dimensions[0], dimensions[1]));
          ships.clear(); // Clear the existing invalid ships entries
        }
      } catch (Exception e) {
        view.invalidFleetSize(Math.min(dimensions[0], dimensions[1]));
        scanner.nextLine(); // Consume the invalid token
      }
    }

    view.line();
    return ships;
  }

  /**
   * Checks if the fleet size is valid for the given dimensions.
   *
   * @param dimensions the dimensions of the game board
   * @param ships      the ship specifications
   * @return true if the fleet size is valid, false otherwise
   */
  public boolean checkFleetSize(int[] dimensions, Map<ShipType, Integer> ships) {
    int sum = 0;
    int value = Math.min(dimensions[0], dimensions[1]);

    for (ShipType type : ships.keySet()) {
      sum += ships.get(type);
    }

    return (sum <= value) && (sum >= 0);
  }

  /**
   * Sets the dimensions of the game board.
   */
  public int[] setDimensions() {
    int[] dimensions = new int[2];

    view.initGame();
    while (true) {
      try {
        dimensions[0] = getIntInput();
        dimensions[1] = getIntInput();

        if (checkDimensions(dimensions[0], dimensions[1])) {
          break;
        } else {
          view.invalidDimensions();
        }
      } catch (Exception e) {
        view.invalidDimensions();
        scanner.nextLine();
      }
    }
    view.line();
    return dimensions;
  }

  /**
   * Retrieves an integer input from the user.
   *
   * @return the integer input
   */
  private int getIntInput() {
    return scanner.nextInt();
  }

  /**
   * Checks if the dimensions are valid.
   *
   * @param dimension1 the first dimension
   * @param dimension2 the second dimension
   * @return true if the dimensions are valid, false otherwise
   */
  public boolean checkDimensions(int dimension1, int dimension2) {
    return checkDimension(dimension1) && checkDimension(dimension2);
  }

  /**
   * Checks if a single dimension is valid.
   *
   * @param dimension the dimension to check
   * @return true if the dimension is valid, false otherwise
   */
  public boolean checkDimension(int dimension) {
    return (6 <= dimension) && (dimension <= 15);
  }

  /**
   * Uses the inputs to set the human and artIntel classes up
   *
   * @param sizeInput      The size of the board for players
   * @param specifications the map of shipTypes and number of ships
   */
  private void setupPlayers(int[] sizeInput, Map<ShipType, Integer> specifications) {
    artIntel1.setup(sizeInput[0], sizeInput[1], specifications);
    human.setup(sizeInput[0], sizeInput[1], specifications);
    view.viewBoard(human.getHumanBoard(), artIntel1.getBoard(), human.name(), artIntel1.name());
  }

  /**
   * The game loop that continues the game until at least one side loses
   */
  private void playGame(int[] dimensions) {
    while (!human.checkLoss() && !artIntel1.checkLoss()) {
      List<Coord> humanAttack = human.takeShots();
      List<Coord> machineAttack = artIntel1.takeShots();
      List<Coord> humanDamage = human.reportDamage(machineAttack);
      List<Coord> machineDamage = artIntel1.reportDamage(humanAttack);
      view.viewBoard(human.getHumanBoard(), artIntel1.getBoard(), human.name(), artIntel1.name());
    }
  }

  /**
   * Decides the outcome dependent on who lost
   */
  private void endGame() {
    provideEnd(human.checkLoss(), artIntel1);
  }

  /**
   * Reads the coordinate input from the user
   *
   * @param limit the max number of coordinates to be read
   * @return coordinates as an array of integers to be attacked
   */
  private ArrayList<Integer> readCoord(int limit, int[] dimensions) {
    ArrayList<Integer> result = new ArrayList<>();
    System.out.println();
    view.line();
    System.out.println("Please Enter " + limit + " Shots:");
    int i = 0;
    while (scanner.hasNext() && i < limit) {  // using hasNext() instead of hasNextLine()
      try {
        while (true) {
          int input1 = scanner.nextInt();  // using nextInt() instead of nextLine()
          int input2 = scanner.nextInt();  // using nextInt() instead of nextLine()
          result.add(input1);
          result.add(input2);
          if (checkCoord(input1, input2, dimensions)) {
            i++;
          }
        }
      } catch (Exception e) {
        System.out.println("Both inputs need to be integers");
        scanner.nextLine();  // Consume the rest of the line
      }
    }
    return result;
  }

  /**
   * Retrieves the attack coordinates from the user.
   *
   * @param limit      the number of opponent ships
   * @param dimensions the height of the board
   * @return a list of attack coordinates
   */
  public List<Coord> getAttackCoord(int limit, int[] dimensions) {
    List<Coord> shots = new ArrayList<>();
    int[] tempDimensions = new int[] {dimensions[0], dimensions[1]};

    view.getShots(limit);
    for (int i = 0; i < limit; i++) {
      while (true) {
        int x = getIntInput();
        int y = getIntInput();

        if (checkCoord(x, y, tempDimensions)) {
          Coord coord = new Coord(x, y);

          if (!shots.contains(coord)) {
            shots.add(coord);
            break;
          } else {
            view.repeatCoordinates(coord.getCoordinates());
          }
        } else {
          view.invalidCoordinates(tempDimensions);
        }
      }
    }

    view.line();
    return shots;
  }

  /**
   * Checks if the coordinate is within the valid range.
   *
   * @param x          the x-coordinate
   * @param y          the y-coordinate
   * @param dimensions the dimensions of the game board
   * @return true if the coordinate is within the valid range, false otherwise
   */
  public boolean checkCoord(int x, int y, int[] dimensions) {
    boolean check;
    check = (0 <= x && x < dimensions[0]) && (0 <= y && y < dimensions[1]);
    return check;
  }

  /**
   * A method to be ignored, just pits 2 AI's against each other for testing purposes
   */
  @Generated({})
  public void simulateWar() {
    artIntel1 = new ArtIntel();
    artIntel2 = new ArtIntel();
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 2);
    map.put(ShipType.BATTLESHIP, 2);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);
    artIntel1.setup(6, 15, map);
    artIntel2.setup(6, 15, map);
    while (!artIntel1.checkLoss() && !artIntel2.checkLoss()) {
      List<Coord> machineAttack1 = artIntel1.takeShots();
      List<Coord> machineAttack2 = artIntel2.takeShots();
      List<Coord> machineDamage1 = artIntel1.reportDamage(machineAttack2);
      List<Coord> machineDamage2 = artIntel2.reportDamage(machineAttack1);
    }
    provideEnd(artIntel1.checkLoss(), artIntel2);
  }

  /**
   * Use the player you want to use the boolean value for to find result against the AI parameter
   *
   * @param b         boolean value for validating with the loss value of player model end
   * @param artIntel2 artificial intelligence to play against
   */
  public void provideEnd(boolean b, ArtIntel artIntel2) {
    if (b && artIntel2.checkLoss()) {
      view.ending(GameResult.TIE);
      scanner.close();
    } else if (b && !artIntel2.checkLoss()) {
      view.ending(GameResult.LOSS);
      scanner.close();
    } else if (!b && artIntel2.checkLoss()) {
      view.ending(GameResult.WIN);
      scanner.close();
    }
  }

  public List<Ship> generateShipPositions(Map<ShipType, Integer> fleet) {
    ArtIntel artIntel = new ArtIntel();
    return artIntel.setup(6, 6, fleet);
  }
}
