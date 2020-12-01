package solver;


class SymmetryHelper
{
    private final int X;
    private final int Y;
    private final boolean[] holes;


    SymmetryHelper(int x, int y, boolean[] holes)
    {
      X = x;
      Y = y;
      this.holes = new boolean[x*y];

      if(holes.length != x*y)
      {
        throw new RuntimeException("array size mismatch");
      }
      
      for(int i = 0; i < holes.length; i++)
      {
        this.holes[i] = holes[i];
      }
    }


    @Override
    public boolean equals(Object o)
    {
      if(o == this)
      {
        return true;
      }

      if(o == null)
      {
        return false;
      }

      if(!(o instanceof SymmetryHelper))
      {
        return false;
      }

      SymmetryHelper other = (SymmetryHelper) o;

      if(this.holes.length != other.holes.length)
      {
        return false;
      }

      for(int i = 0; i < holes.length; i++)
      {
        if(holes[i] != other.holes[i])
        {
          return false;
        }
      }

      return true;
    }


    @Override
    public int hashCode()
    {
      return id().hashCode();
    }


    private boolean[][] from1D()
    {
      boolean[][] t1 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t1[x][y] = holes[y*X + x];
        }
      }

      return t1;
    }


    private boolean[] from2D(boolean[][] t2)
    {
      boolean[] mod_pos = new boolean[X*Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          mod_pos[y*X + x] = t2[x][y];
        }
      }

      return mod_pos;
    }


    SymmetryHelper verticalFlip()
    {
      boolean[][] t1 = from1D();
      boolean[][] t2 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t2[x][y] = t1[(X - 1) - x][y];
        }
      }

      return new SymmetryHelper(X, Y, from2D(t2));
    }


    SymmetryHelper horizontalFlip()
    {
      boolean[][] t1 = from1D();
      boolean[][] t2 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t2[x][y] = t1[x][(Y - 1) - y];
        }
      }

      return new SymmetryHelper(X, Y, from2D(t2));
    }


    SymmetryHelper leftDiagonalFlip()
    {
      boolean[][] t1 = from1D();
      boolean[][] t2 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t2[x][y] = t1[(Y - 1) - y][(X - 1) - x];
        }
      }

      return new SymmetryHelper(X, Y, from2D(t2));
    }


    SymmetryHelper rightDiagonalFlip()
    {
      boolean[][] t1 = from1D();
      boolean[][] t2 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t2[x][y] = t1[y][x];
        }
      }

      return new SymmetryHelper(X, Y, from2D(t2));
    }


    SymmetryHelper rotate90()
    {
      boolean[][] t1 = from1D();
      boolean[][] t2 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t2[x][y] = t1[y][(X - 1) - x];
        }
      }

      return new SymmetryHelper(X, Y, from2D(t2));
    }


    SymmetryHelper rotate180()
    {
      boolean[][] t1 = from1D();
      boolean[][] t2 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t2[x][y] = t1[(X - 1) - x][(Y - 1) - y];
        }
      }

      return new SymmetryHelper(X, Y, from2D(t2));
    }


    SymmetryHelper rotate270()
    {
      boolean[][] t1 = from1D();
      boolean[][] t2 = new boolean[X][Y];

      for(int x = 0; x < X; x++) {
        for(int y = 0; y < Y; y++) {
          t2[x][y] = t1[(Y - 1) - y][x];
        }
      }

      return new SymmetryHelper(X, Y, from2D(t2));
    }


    
    String id() {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < holes.length; i++) {
        if(holes[i]) {
          sb.append('1');
        }
        else {
          sb.append('0');
        }
      }
      return sb.toString();
    }
}

