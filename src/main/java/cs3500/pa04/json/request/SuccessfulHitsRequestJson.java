package cs3500.pa04.json.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import java.util.List;

/**
 * Record to handle request for successful hits as json
 *
 * @param methodName expects successful-hits
 * @param arguments the value held by the response
 */
public record SuccessfulHitsRequestJson(
    @JsonProperty("method-name") String methodName,
    @JsonProperty("arguments") SuccessfulHitsRequestArguments arguments) {

  /**
   * Holds the coordinates that were hit successfully
   *
   * @param coordinates the points on the board that were hit
   */
  public record SuccessfulHitsRequestArguments(
      @JsonProperty("coordinates")
      List<SuccessfulHitsRequestJson.SuccessfulHitsRequestArguments.Coord> coordinates) {

    /**
     * The coordinates that were hit
     *
     * @param x column
     * @param y row
     */
    public record Coord(
        @JsonProperty("x") int x,
        @JsonProperty("y") int y) {
    }
  }
}