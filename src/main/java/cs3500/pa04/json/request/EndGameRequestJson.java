package cs3500.pa04.json.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.GameResult;

/**
 * Responds to the request to end the game
 *
 * @param result the outcome
 * @param reason why the game ended
 */
public record EndGameRequestJson(
    @JsonProperty("result") GameResult result,
    @JsonProperty("reason") String reason) {
}

