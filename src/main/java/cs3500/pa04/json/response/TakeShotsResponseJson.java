package cs3500.pa04.json.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import java.util.List;

/**
 * Record to handle take shots request from server
 *
 * @param shots that were hit by us
 */
public record TakeShotsResponseJson(
    @JsonProperty("coordinates") List<Coord> shots) {

  public List<Coord> coordinates() {
    return shots;
  }

  /**
   * Holds the json value as record to show the coordinates that we hit
   *
   * @param x the column
   * @param y the row
   */
  public record Coord(
      @JsonProperty("x") int x,
      @JsonProperty("y") int y) {
  }
}

