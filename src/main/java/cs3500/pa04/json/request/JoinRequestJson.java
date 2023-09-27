package cs3500.pa04.json.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record to handle the request to join
 *
 * @param name expects join
 * @param gameType Multi or Single
 */
public record JoinRequestJson(
    @JsonProperty("name") String name,
    @JsonProperty("game-type") String gameType) {
  public String getName() {
    return name;
  }

  public String getGameType() {
    return gameType;
  }
}
