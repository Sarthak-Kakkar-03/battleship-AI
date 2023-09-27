package cs3500.pa04.json.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Record to respond to the setup request from the server
 *
 * @param fleet the fleet of ships to be initialized
 */
public record SetupResponseJson(
    @JsonProperty("fleet") List<SetupFleet> fleet) {

  /**
   * Sets the fleet for the game from the server
   *
   * @param coord the coordinates a ship starts
   * @param length length of teh ship depending on type
   * @param direction the direction the ship is going
   */
  public record SetupFleet(
      @JsonProperty("coord") Coord coord,
      @JsonProperty("length") int length,
      @JsonProperty("direction") String direction) {

    /**
     * The json record to hold the coordinates where the ships start
     *
     * @param x column value
     * @param y row value
     */
    public record Coord(
        @JsonProperty("x") int x,
        @JsonProperty("y") int y) {
    }
  }
}
