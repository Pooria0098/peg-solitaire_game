package javafxviz;


import javafx.application.Application;
import solver.Board;
import solver.Position;
import solver.PruningSearch;

class SearchRunner
{
  int[] boardInitArray;
  String boardName;
  int boardSizeX;
  int boardSizeY;
  int initPosX;
  int initPosY;
  int pruningFactor;

  void visualize()
  {
    String[] args = new String[0];

    Board b = new Board(boardSizeX, boardSizeY, boardInitArray);
    Position p = b.initialPosition(initPosX, initPosY);
    PruningSearch pruningSearch = new PruningSearch(p);
    pruningSearch.prune(pruningFactor);
    int n_sol = pruningSearch.search();

    if(n_sol > 0)
    {
      System.out.printf("Found %d solution(s).%n", n_sol);

      for(int i = 0; i < n_sol; i++)
      {
        Position f = pruningSearch.getFinalPosition(i);
        if(p.isComplement(f))
        {
          System.out.println("Found complement solution.");
          Visualizer.title = String.format("%s Solution #%d", boardName, i+1);
          Visualizer.board = b;
          Visualizer.initPosition = p;
          Visualizer.solution = pruningSearch.getSolution(i);
          Application.launch(Visualizer.class, args);
          return;
        }
      }

      int i = 0;
      System.out.println("Did not find any complement solutions. Displaying solution #1.");
      Visualizer.title = String.format("%s Solution #%d", boardName, i+1);
      Visualizer.board = b;
      Visualizer.initPosition = p;
      Visualizer.solution = pruningSearch.getSolution(i);
      Application.launch(Visualizer.class, args);
    }
    else
    {
      System.out.println("No solutions found.");
    }
  }
}
