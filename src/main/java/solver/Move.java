package solver;

public class Move
{
  public final int x1;
  public final int y1;
  public final int x2;
  public final int y2;


  public Move(int x1, int y1, int x2, int y2)
  {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }

  public String toString()
  {
    return String.format("[%d, %d, %d, %d],%n", x1, y1, x2, y2);
  }
}
