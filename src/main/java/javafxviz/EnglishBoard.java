package javafxviz;


public class EnglishBoard
{
  public static void main(String[] args)
  {
    SearchRunner sr = new SearchRunner();

    sr.boardInitArray = new int[] {
        0, 0, 1, 1, 1, 0, 0,
        0, 0, 1, 1, 1, 0, 0,
        1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1,
        0, 0, 1, 1, 1, 0, 0,
        0, 0, 1, 1, 1, 0, 0
    };

    sr.boardName = "English Board";
    sr.pruningFactor = 121;
    sr.boardSizeX = 7;
    sr.boardSizeY = 7;
    sr.initPosX = 3;
    sr.initPosY = 3;

    sr.visualize();
  }
}
