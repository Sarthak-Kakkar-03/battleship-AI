package cs3500.pa04.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
  Board board;

  @BeforeEach
  void setUp() {
    board = new Board(6, 10);
  }

  @Test
  void setShips() {
    board.setShips(1, 1, 1, 1);
    assertEquals(4, board.getAssignedShips().size());
  }

  @Test
  void getAssignedShips() {
  }

  @Test
  void getHeight() {
    assertEquals(6, board.getHeight());
  }

  @Test
  void getWidth() {
    assertEquals(10, board.getWidth());
  }

  @Test
  void getCoordinate() {
    assertThrows(IllegalArgumentException.class, () -> board.getCoordinate(10, 10));
    assertNotNull(board.getCoordinate(0, 0));
    assertThrows(IllegalArgumentException.class, () -> board.getCoordinate(new Coord(10, 10)));
    assertNotNull(board.getCoordinate(new Coord(0, 0)));
  }

}