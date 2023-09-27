package cs3500.pa04.json.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import java.util.List;

/**
 * Handles report damage request
 *
 * @param coordinates the coordinates that were hit on us
 */
public record ReportDamageRequestJson(
    @JsonProperty("coordinates") List<Coord> coordinates) {

  /**
   * Record to show json value of hit coordinates
   *
   * @param x column number
   * @param y row number
   */
  public record Coord(
      @JsonProperty("x") int x,
      @JsonProperty("y") int y) {
  }
}
