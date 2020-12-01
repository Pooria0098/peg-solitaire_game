package javafxviz;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import solver.Board;
import solver.Move;
import solver.Position;

import java.util.List;

public class Visualizer extends Application
{
  static String title;
  static Board board;
  static Position initPosition;
  static List<Move> solution;

  private static final double CELL_SIZE = 50.0;
  private static final double ARR = 10.0;
  private Canvas canvas = null;
  private GraphicsContext gc = null;
  private Position currentPosition = null;
  private int solutionIndex = 0;
  private boolean showArrow = false;


  @Override
  public void start(Stage stage)
  {
    double w = board.X * CELL_SIZE + 2 * CELL_SIZE;
    double h = board.Y * CELL_SIZE + 4 * CELL_SIZE;
    Background bg = whiteBackground();

    stage.setTitle(title);
    stage.setMinWidth(w);
    stage.setMinHeight(h);

    canvas = new Canvas(board.X*CELL_SIZE, board.Y*CELL_SIZE);
    gc = canvas.getGraphicsContext2D();
    currentPosition = initPosition.copy();
    paintBoard();

    BorderPane rootPane = new BorderPane();
    rootPane.setBackground(whiteBackground());

    rootPane.setTop(header(title));
    rootPane.setCenter(canvas);
    rootPane.setBottom(footer());

    Scene scene = new Scene(rootPane, w, h);

    stage.setScene(scene);
    stage.show();
  }


  private Background whiteBackground()
  {
    BackgroundFill bf = new BackgroundFill(Color.WHITE, null, null);
    return new Background(bf);
  }


  private HBox header(String titleText)
  {
    String defaultFontName = Font.getDefault().getName();
    Font font = new Font(defaultFontName, 25);
    Text text = new Text(titleText);
    text.setFont(font);
    text.setFontSmoothingType(FontSmoothingType.LCD);

    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(10, 10, 10, 10));
    hbox.getChildren().addAll(text);
    return hbox;
  }


  private HBox footer()
  {
    Button startButton = new Button();
    startButton.setText("Start");
    startButton.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        solutionIndex = 0;
        showArrow = false;
        currentPosition = initPosition.copy();
        paintBoard();
      }
    });

    Button advanceButton = new Button();
    advanceButton.setText("Advance >>");
    advanceButton.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        if(solutionIndex >= solution.size())
        {
          return;
        }

        if(showArrow)
        {

          Move move = solution.get(solutionIndex);
          currentPosition.set(move.x1, move.y1, false);
          currentPosition.set(move.x2, move.y2, true);

          int x3;
          int y3;

          if(move.x1 == move.x2)
          {
            x3 = move.x1;
            y3 = (move.y1 + move.y2)/2;
          }
          else
          {
            y3 = move.y1;
            x3 = (move.x1 + move.x2)/2;
          }

          currentPosition.set(x3, y3, false);

          showArrow = false;
          solutionIndex++;
          paintBoard();
        }
        else
        {
          showArrow = true;
          paintBoard();
        }
      }
    });

    HBox hbox = new HBox();
    hbox.setAlignment(Pos.CENTER);
    hbox.setPadding(new Insets(10, 10, 10, 10));
    hbox.getChildren().addAll(startButton, advanceButton);
    return hbox;
  }


  private void paintBoard()
  {
    gc.setFill(Color.WHITE);
    gc.fillRect(0, 0, board.X*CELL_SIZE, board.Y*CELL_SIZE);

    for(int x = 0; x < board.X; x++)
    {
      for(int y = 0; y < board.Y; y++)
      {
        if(board.allowed(x, y))
        {
          drawCell(x, y);
          if(currentPosition.occupied(x, y))
          {
            drawPeg(x, y);
          }
        }
      }
    }

    if(showArrow)
    {
      drawArrow();
    }
  }


  private void drawCell(int x, int y)
  {
    double x1 = x*CELL_SIZE;
    double x2 = (x+1)*CELL_SIZE;
    if(x == 0)
    {
      x1 += 1.0;
    }
    if(x+1 == board.X)
    {
      x2 -= 1.0;
    }

    double y1 = y*CELL_SIZE;
    double y2 = (y+1)*CELL_SIZE;
    if(y == 0)
    {
      y1 += 1.0;
    }
    if(y+1 == board.Y)
    {
      y2 -= 1.0;
    }

    gc.setLineWidth(1);

    gc.strokeLine(x1, y1, x1, y2);
    gc.strokeLine(x1, y1, x2, y1);
    gc.strokeLine(x2, y1, x2, y2);
    gc.strokeLine(x1, y2, x2, y2);

  }


  private void drawPeg(int x, int y)
  {
    gc.setFill(Color.LIGHTGREEN);
    double x1 = x*CELL_SIZE + 3.0;
    double y1 = y*CELL_SIZE + 3.0;

    gc.fillOval(x1, y1, CELL_SIZE - 6.0, CELL_SIZE - 6.0);
  }


  private void drawArrow()
  {
    Move move = solution.get(solutionIndex);

    double x1 = move.x1 * CELL_SIZE + CELL_SIZE/2.0;
    double y1 = move.y1 * CELL_SIZE + CELL_SIZE/2.0;
    double x2 = move.x2 * CELL_SIZE + CELL_SIZE/2.0;
    double y2 = move.y2 * CELL_SIZE + CELL_SIZE/2.0;

    gc.setLineWidth(3);
    gc.strokeLine(x1, y1, x2, y2);

    if(move.x1 == move.x2)
    {
      if(move.y1 < move.y2)
      {
        drawDownArrow(x2, y2);
      }
      else
      {
        drawUpArrow(x2, y2);
      }
    }
    else if(move.y1 == move.y2)
    {
      if(move.x1 < move.x2)
      {
        drawRightArrow(x2, y2);
      }
      else
      {
        drawLeftArrow(x2, y2);
      }
    }
  }


  private void drawDownArrow(double x, double y)
  {
    drawTopLeft(x, y);
    drawTopRight(x, y);
  }


  private void drawUpArrow(double x, double y)
  {
    drawBottomLeft(x, y);
    drawBottomRight(x, y);
  }


  private void drawRightArrow(double x, double y)
  {
    drawTopLeft(x, y);
    drawBottomLeft(x, y);
  }


  private void drawLeftArrow(double x, double y)
  {
    drawTopRight(x, y);
    drawBottomRight(x, y);
  }


  private void drawTopLeft(double x, double y)
  {
    gc.setLineWidth(3);
    gc.strokeLine(x, y, x-ARR, y-ARR);
  }


  private void drawTopRight(double x, double y)
  {
    gc.setLineWidth(3);
    gc.strokeLine(x, y, x+ARR, y-ARR);
  }


  private void drawBottomLeft(double x, double y)
  {
    gc.setLineWidth(3);
    gc.strokeLine(x, y, x-ARR, y+ARR);
  }


  private void drawBottomRight(double x, double y)
  {
    gc.setLineWidth(3);
    gc.strokeLine(x, y, x+ARR, y+ARR);
  }
}
