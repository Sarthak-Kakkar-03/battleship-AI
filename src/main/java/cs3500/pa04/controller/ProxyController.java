package cs3500.pa04.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.request.EndGameRequestJson;
import cs3500.pa04.json.request.JoinRequestJson;
import cs3500.pa04.json.request.ReportDamageRequestJson;
import cs3500.pa04.json.request.SetupRequestJson;
import cs3500.pa04.json.request.SuccessfulHitsRequestJson;
import cs3500.pa04.json.response.ReportDamageResponseJson;
import cs3500.pa04.json.response.SetupResponseJson;
import cs3500.pa04.json.response.TakeShotsResponseJson;
import cs3500.pa04.model.ArtIntel;
import cs3500.pa04.model.Board;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import cs3500.pa04.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller that deals with the socket for online connectivity
 */
public class ProxyController {
  public View view = new View();
  public String name;
  public String gameType = "SINGLE";
  private final Socket server;
  public InputStream in;
  public PrintStream out;
  private final ArtIntel player;
  public final ObjectMapper mapper = new ObjectMapper();
  private int height;
  private int width;
  private List<Ship> shipList;
  boolean testing = false;
  private JsonNode jsonResponse;

  /**
   * Constructs an instance of ProxyController.
   *
   * @param server   the socket connection to the server
   * @param artIntel the instance of the player controller
   * @throws IOException if an IO error occurs
   */
  public ProxyController(Socket server, ArtIntel artIntel) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = artIntel;
  }

  /**
   * Looks for the server input and delegates to relevant method to respond
   *
   * @throws IOException if run into an unexpected situation
   */
  public void run() throws IOException {
    JsonParser parser = this.mapper.getFactory().createParser(this.in);
    while (!server.isClosed()) {
      MessageJson message = parser.readValueAs(MessageJson.class);
      delegateMessage(message);
    }
  }

  /**
   * Delegates the relevant message as per the name
   *
   * @param message message json received
   */
  public void delegateMessage(MessageJson message) {
    String methodName = message.messageName();
    switch (methodName) {
      case "join":
        this.joinRequestHandler(message);
        break;
      case "setup":
        this.setUpHandler(message);
        break;
      case "take-shots":
        this.takeShotsHandler();
        break;
      case "report-damage":
        this.reportDamageHandler(message);
        break;
      case "successful-hits":
        this.successfulHitsHandler(message);
        break;
      case "end-game":
        this.endGameHandler(message);
        break;
      default:
        System.out.println("Invalid Response");
        break;
    }
  }

  private void joinRequestHandler(MessageJson messageJson) {
    JoinRequestJson argument = new JoinRequestJson(player.name(), gameType);
    JsonNode argumentsJson = JsonUtils.serializeRecord(argument);
    //System.out.println(argumentsJson);
    messageJson = messageJson.setArguments(argumentsJson);
    JsonNode response = JsonUtils.serializeRecord(messageJson);
    sendResponse(response);
  }

  private void setUpHandler(MessageJson messageJson) {
    SetupRequestJson setupRequestJson = this.mapper.convertValue(messageJson.arguments(),
        SetupRequestJson.class);
    height = setupRequestJson.height();
    width = setupRequestJson.width();
    Map<ShipType, Integer> specifications = setupRequestJson.specifications();
    shipList = player.setup(height, width, specifications);
    List<SetupResponseJson.SetupFleet> fleets = new ArrayList<>();
    for (Ship ship : shipList) {
      SetupResponseJson.SetupFleet.Coord coord =
          new SetupResponseJson.SetupFleet.Coord(ship.startingCoord().getColumn(),
              ship.startingCoord().getRow());
      SetupResponseJson.SetupFleet fleet = new SetupResponseJson.SetupFleet(coord, ship.getSize(),
          ship.getDirection());
      fleets.add(fleet);
    }
    SetupResponseJson setupResponseJson = new SetupResponseJson(fleets);
    JsonNode setupArgumentsJson = JsonUtils.serializeRecord(setupResponseJson);
    messageJson = messageJson.setArguments(setupArgumentsJson);
    JsonNode jsonResponse = JsonUtils.serializeRecord(messageJson);
    sendResponse(jsonResponse);
    view.viewBoard(player.getBoard());
  }

  /**
   * Sends the response to the output stream and shows the response as a print message
   *
   * @param jsonResponse node to be responded with
   */
  public void sendResponse(JsonNode jsonResponse) {
    if (testing) {
      this.jsonResponse = jsonResponse;
    } else {
      this.out.println(jsonResponse);
    }
  }

  public JsonNode getResponse() {
    return this.jsonResponse;
  }

  private void takeShotsHandler() {
    List<Coord> shots = player.takeShots();
    List<TakeShotsResponseJson.Coord> takeShots = new ArrayList<>();
    for (Coord coord : shots) {
      TakeShotsResponseJson.Coord coordJson = new TakeShotsResponseJson.Coord(coord.getColumn(),
          coord.getRow());
      takeShots.add(coordJson);
    }

    TakeShotsResponseJson response = new TakeShotsResponseJson(takeShots);

    JsonNode jsonNode = JsonUtils.serializeRecord(response);

    MessageJson outputMessage = new MessageJson("take-shots", jsonNode);

    JsonNode jsonResponse = JsonUtils.serializeRecord(outputMessage);

    sendResponse(jsonResponse);
  }

  private void reportDamageHandler(MessageJson message) {
    ReportDamageRequestJson reportArguments = this.mapper.convertValue(message.arguments(),
        ReportDamageRequestJson.class);
    ArrayList<Coord> temp = new ArrayList<>();
    for (ReportDamageRequestJson.Coord jsonCoord : reportArguments.coordinates()) {
      temp.add(new Coord(jsonCoord.y(), jsonCoord.x()));
    }
    List<Coord> receivedDamage = player.reportDamage(temp);
    List<ReportDamageResponseJson.ReportDamageResponseArguments.Coord> jsonRecDamage =
        new ArrayList<>();
    for (Coord coord : receivedDamage) {
      ReportDamageResponseJson.ReportDamageResponseArguments.Coord coordJson =
          new ReportDamageResponseJson.ReportDamageResponseArguments.Coord(coord.getColumn(),
              coord.getRow());
      jsonRecDamage.add(coordJson);
    }
    ReportDamageResponseJson.ReportDamageResponseArguments jsonReport =
        new ReportDamageResponseJson.ReportDamageResponseArguments(jsonRecDamage);
    JsonNode argsNode = JsonUtils.serializeRecord(jsonReport);
    MessageJson messageJson = new MessageJson(message.messageName(), argsNode);
    JsonNode finalResponse = JsonUtils.serializeRecord(messageJson);
    sendResponse(finalResponse);
  }

  private void successfulHitsHandler(MessageJson messageJson) {
    SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments arguments
        = this.mapper.convertValue(messageJson.arguments(),
        SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments.class);
    ArrayList<Coord> temp = new ArrayList<>();
    for (SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments.Coord
        jsonCoord : arguments.coordinates()) {
      temp.add(new Coord(jsonCoord.y(), jsonCoord.x()));
    }
    player.successfulHits(temp);
    MessageJson messageJson1 = new MessageJson(messageJson.messageName(),
        mapper.createObjectNode());
    JsonNode finalResponse = JsonUtils.serializeRecord(messageJson1);
    sendResponse(finalResponse);
  }

  private void endGameHandler(MessageJson messageJson) {
    MessageJson outputMessage = new MessageJson(messageJson.messageName(),
        mapper.createObjectNode());
    JsonNode finalReponse = JsonUtils.serializeRecord(outputMessage);
    System.out.println(player.numActiveShips());
    sendResponse(finalReponse);
    EndGameRequestJson arguments = this.mapper.convertValue(messageJson.arguments(),
        EndGameRequestJson.class);
    player.endGame(arguments.result(), arguments.reason());
    try {
      server.close();
    } catch (IOException exception) {
      System.err.println("Unable to close server due to: " + exception);
    }
    view.ending(arguments.result());
  }

  public Board getBoard() {
    return player.getBoard();
  }

  public GameResult getGameResult() {
    return player.getGameResult();
  }

  public String getReason() {
    return player.getReason();
  }
}
