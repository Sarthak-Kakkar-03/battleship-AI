package cs3500.pa04.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa04.controller.ProxyController;
import cs3500.pa04.json.JsonUtils;
import cs3500.pa04.json.MessageJson;
import cs3500.pa04.json.request.EndGameRequestJson;
import cs3500.pa04.json.request.JoinRequestJson;
import cs3500.pa04.json.request.ReportDamageRequestJson;
import cs3500.pa04.json.request.SetupRequestJson;
import cs3500.pa04.json.request.SuccessfulHitsRequestJson;
import cs3500.pa04.json.response.TakeShotsResponseJson;
import cs3500.pa04.model.ArtIntel;
import cs3500.pa04.model.Board;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.GameResult;
import cs3500.pa04.model.ShipType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Tester for the proxy controller
 */
public class ProxyControllerTest {
  @Mock
  private Socket server;
  @Mock
  private ArtIntel artIntel;
  @Mock
  private PrintStream out;

  private ProxyController proxyController;
  private ObjectMapper objectMapper;

  /**
   * Set up method for this tester
   *
   * @throws IOException in case we run into an error
   */
  @BeforeEach
  private void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    objectMapper = new ObjectMapper();

    // Mock server's getOutputStream() and getInputStream()
    when(server.getOutputStream()).thenReturn(new ByteArrayOutputStream());
    when(server.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

    ArtIntel artIntel = new ArtIntel("pa04-font-gpt");
    proxyController = new ProxyController(server, artIntel);
    proxyController.testing = false;
    proxyController.out = out; // If 'out' is accessible
  }

  @Test
  void testSendResponse() {
    // Create a sample JsonNode
    JsonNode jsonResponse = objectMapper.createObjectNode().put("key", "value");

    // Call the sendResponse method
    proxyController.sendResponse(jsonResponse);

    // Verify that out.println() was called with the correct argument
    verify(out).println(jsonResponse);
  }

  @Test
  void joinRequestHandlerTest() {
    // Create a MessageJson object with a specific messageName and arguments
    JsonNode emptyArguments = objectMapper.createObjectNode();
    MessageJson messageJson = new MessageJson("join", emptyArguments);
    proxyController.delegateMessage(messageJson);

    ObjectMapper objectMapper = new ObjectMapper();
    JoinRequestJson joinRequestJson = new JoinRequestJson("pa04-font-gpt", "SINGLE");
    JsonNode argumentsJson = objectMapper.valueToTree(joinRequestJson);

    MessageJson outputMessage = new MessageJson("join", argumentsJson);
    JsonNode output = JsonUtils.serializeRecord(outputMessage);

    // Verify that out.println() was called with the correct argument
    verify(out).println(output);
  }

  @Test
  void setUpHandlerTest() {
    // Create the fleet specifications
    Map<ShipType, Integer> fleetSpec = new HashMap<>();
    fleetSpec.put(ShipType.CARRIER, 1);
    fleetSpec.put(ShipType.BATTLESHIP, 1);
    fleetSpec.put(ShipType.DESTROYER, 2);
    fleetSpec.put(ShipType.SUBMARINE, 1);

    // Create the SetupRequestJson object
    SetupRequestJson setupRequestJson = new SetupRequestJson(6, 7, fleetSpec);

    // Serialize the SetupRequestJson object to a JsonNode
    JsonNode argumentsJson = objectMapper.valueToTree(setupRequestJson);
    // Create the MessageJson object
    MessageJson messageJson = new MessageJson("setup", argumentsJson);
    proxyController.delegateMessage(messageJson);

    Board board = proxyController.getBoard();

    // Verify that out.println() was called with the correct argument
    assertEquals(6, board.getWidth());
    assertEquals(7, board.getHeight());
    assertEquals(5, board.getAssignedShips().size());
  }

  List<Coord> getTakeShotsListCoords() {
    String json = String.valueOf(proxyController.getResponse());

    ObjectMapper mapper = new ObjectMapper();
    MessageJson message = null;
    try {
      message = mapper.readValue(json, MessageJson.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    TakeShotsResponseJson reportArguments = mapper.convertValue(message.arguments(),
        TakeShotsResponseJson.class);
    List<TakeShotsResponseJson.Coord> jsonCoords = reportArguments.coordinates();
    List<Coord> temp = new ArrayList<>();

    for (TakeShotsResponseJson.Coord jsonCoord : jsonCoords) {
      temp.add(new Coord(jsonCoord.y(), jsonCoord.x()));
    }

    return temp;
  }

  @Test
  void testTakeShotsHandler() {
    setUpHandlerTest();
    proxyController.testing = true;
    // Create a MessageJson object with a specific messageName and arguments
    JsonNode emptyArguments = objectMapper.createObjectNode();
    MessageJson messageJson = new MessageJson("take-shots", emptyArguments);
    proxyController.delegateMessage(messageJson);

    int size = getTakeShotsListCoords().size();
    assertEquals(5, size);
  }

  @Test
  void testReportDamageHandler() {
    setUpHandlerTest();
    proxyController.testing = true;
    List<ReportDamageRequestJson.Coord> coordinates = new ArrayList<>();
    coordinates.add(new ReportDamageRequestJson.Coord(0, 0));
    coordinates.add(new ReportDamageRequestJson.Coord(0, 1));
    coordinates.add(new ReportDamageRequestJson.Coord(0, 2));

    ReportDamageRequestJson requestJson = new ReportDamageRequestJson(coordinates);
    JsonNode arguments = new ObjectMapper().valueToTree(requestJson);

    MessageJson messageJson = new MessageJson("report-damage", arguments);
    proxyController.delegateMessage(messageJson);

    int size = getTakeShotsListCoords().size();
    assertTrue(size == 0 || size == 1 || size == 2 || size == 3);
  }

  @Test
  void testSuccessfulHitsHandler() {
    setUpHandlerTest();
    proxyController.testing = true;
    List<SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments.Coord> coordinates;
    coordinates = new ArrayList<>();
    coordinates.add(new SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments.Coord(0, 0));
    SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments requestJson1;
    requestJson1 = new SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments(coordinates);

    coordinates.add(new SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments.Coord(3, 3));
    SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments requestJson2;
    requestJson2 = new SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments(coordinates);
    JsonNode arguments2 = new ObjectMapper().valueToTree(requestJson2);

    coordinates.add(new SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments.Coord(2, 2));
    SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments requestJson3;
    requestJson3 = new SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments(coordinates);
    JsonNode arguments3 = new ObjectMapper().valueToTree(requestJson3);

    JsonNode arguments1 = new ObjectMapper().valueToTree(requestJson1);
    MessageJson messageJson1 = new MessageJson("successful-hits", arguments1);
    MessageJson messageJson2 = new MessageJson("successful-hits", arguments2);
    MessageJson messageJson3 = new MessageJson("successful-hits", arguments3);
    proxyController.delegateMessage(messageJson3);

    String json = String.valueOf(proxyController.getResponse());

    JsonNode emptyArguments = objectMapper.createObjectNode();
    MessageJson messageJson = new MessageJson("successful-hits", emptyArguments);
    JsonNode node  = JsonUtils.serializeRecord(messageJson);
    JsonNode node1 = JsonUtils.serializeRecord(messageJson1);
    JsonNode node2 = JsonUtils.serializeRecord(messageJson2);
    JsonNode node3 = JsonUtils.serializeRecord(messageJson3);

    assertTrue(json.equals(String.valueOf(node))
        || json.equals(String.valueOf(node1))
        || json.equals(String.valueOf(node2))
        || json.equals(String.valueOf(node3)));
  }

  @Test
  void testEndGameHandler() {
    setUpHandlerTest();
    proxyController.testing = true;
    EndGameRequestJson endGameRequest = new EndGameRequestJson(GameResult.WIN,
        "Player 1 sank all of Player 2's ships");
    JsonNode jsonNode = JsonUtils.serializeRecord(endGameRequest);
    MessageJson messageJson = new MessageJson("end-game", jsonNode);
    proxyController.delegateMessage(messageJson);

    JsonNode response = proxyController.getResponse();

    JsonNode emptyArguments = objectMapper.createObjectNode();
    MessageJson outputJson = new MessageJson("end-game", emptyArguments);
    JsonNode node  = JsonUtils.serializeRecord(outputJson);

    assertEquals(node, response);
    assertEquals(GameResult.WIN, proxyController.getGameResult());
    assertEquals("Player 1 sank all of Player 2's ships", proxyController.getReason());
  }
}
