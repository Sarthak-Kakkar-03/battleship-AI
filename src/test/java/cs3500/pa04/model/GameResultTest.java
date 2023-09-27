package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GameResultTest {

  @Test
  void getMessage() {
    assertEquals("You won the game!", GameResult.WIN.getMessage());
    assertEquals("You lost the game!", GameResult.LOSS.getMessage());
    assertEquals("All ships have been destroyed, it's a tie!!",
        GameResult.TIE.getMessage());
  }

  @Test
  void values() {
    GameResult[] expected = {GameResult.WIN, GameResult.LOSS, GameResult.TIE};
    assertArrayEquals(expected, GameResult.values());
  }


}