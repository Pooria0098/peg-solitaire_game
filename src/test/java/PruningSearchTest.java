import static org.junit.Assert.*;

import solver.Board;
import solver.Position;
import solver.PruningSearch;
import org.junit.*;


public class PruningSearchTest
{
  private static int[] englishBoard = new int[]{
      0, 0, 1, 1, 1, 0, 0,
      0, 0, 1, 1, 1, 0, 0,
      1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 1, 1, 1, 1,
      0, 0, 1, 1, 1, 0, 0,
      0, 0, 1, 1, 1, 0, 0
  };


  @Test
  public void solveEnglishBoard()
  {
    Board b = new Board(7, 7, englishBoard);
    Position p = b.initialPosition(3, 3);
    PruningSearch pruningSearch = new PruningSearch(p);

    pruningSearch.prune(121);
    int solutions = pruningSearch.search();
    if (solutions < 1)
    {
      fail("Solution to English board has not been found");
    }
  }

  @Test
  public void minimalBoardSize()
  {
    Board b = new Board(1, 1, new int[] { 1 });
  }


  @Test(expected = RuntimeException.class)
  public void boardTooSmall()
  {
    Board b = new Board(0, 0, new int[] { 1 });
  }

}
