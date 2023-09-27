package cs3500.pa04.json.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa04.model.Coord;
import cs3500.pa04.model.Ship;
import cs3500.pa04.model.ShipType;
import java.util.List;
import java.util.Map;

/**
 * Record to respond to setup request
 *
 * @param width the width of the board or num columns
 * @param height the height or num rows
 * @param specifications the number of kinds of ships we want
 */
public record SetupRequestJson(
    @JsonProperty("width") int width,
    @JsonProperty("height") int height,
    @JsonProperty("fleet-spec")  Map<ShipType, Integer> specifications) {
}
