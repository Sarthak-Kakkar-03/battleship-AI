package cs3500.pa04.json.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import java.util.List;

/**
 * Reports the damage in response to request from server
 *
 * @param methodName expects report-damage from server
 * @param arguments the coordinates that were hit
 */
public record ReportDamageResponseJson(
    @JsonProperty("method-name") String methodName,
    @JsonProperty("arguments") ReportDamageResponseArguments arguments) {

  /**
   * Holds the arguments for the report damage request
   *
   * @param volley holds the coordinates that were damaged
   */
  public record ReportDamageResponseArguments(
      @JsonProperty("coordinates")
      List<ReportDamageResponseJson.ReportDamageResponseArguments.Coord> volley) {

    /**
     * Record to hold the coordinates data
     *
     * @param x column on the board
     * @param y row on the board
     */
    public record Coord(
        @JsonProperty("x") int x,
        @JsonProperty("y") int y) {
    }
  }
}
