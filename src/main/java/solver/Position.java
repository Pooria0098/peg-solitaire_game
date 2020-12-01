package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Position class represents the state of the Peg Solitaire board
 * at any particular step during the game.
 * <p>
 * There is no public constructor. The initial Position is constructed
 * by the Board class.
 */
public class Position {
    private final Board board;
    private final boolean[] occupied;
    private final List<Move> history;

    private String id = null;
    private String symmID = null;
    private int compactnessScore = -1;


    // constructor is not public. Should only be used by
    // Board to return initial state.
    // may also be used by in-package unit tests.
    Position(Board board) {
        this.board = board;
        occupied = new boolean[board.X * board.Y];
        history = new ArrayList<Move>();

        for (int i = 0; i < occupied.length; i++) {
            if (board.allowed(i)) {
                occupied[i] = true;
            } else {
                occupied[i] = false;
            }
        }
    }


    public boolean occupied(int x, int y) {
        if (board.allowed(x, y)) {
            int i = y * board.X + x;
            return occupied[i];
        } else {
            return false;
        }
    }


    public Position copy() {
        Position p = new Position(this.board);
        for (int i = 0; i < occupied.length; i++) {
            p.occupied[i] = this.occupied[i];
        }
        return p;
    }


    public void set(int x, int y, boolean state) {
        occupied[y * board.X + x] = state;
    }


    public boolean isComplement(Position other) {
        for (int i = 0; i < occupied.length; i++) {
            if (board.allowed(i)) {
                if (occupied[i] == other.occupied[i]) {
                    return false;
                }
            }
        }
        return true;
    }


    void set(int i, boolean state) {
        occupied[i] = state;
    }


    boolean isFinal() {
        int counter = 0;

        for (int i = 0; i < occupied.length; i++) {
            if (occupied[i]) {
                counter++;
            }
        }

        return counter == 1;
    }


    String id() {
        if (id == null) {
            id = calculateID();
        }

        return id;
    }


    private String calculateID() {
        StringBuilder sb = new StringBuilder(board.X * board.Y);
        for (int i = 0; i < occupied.length; i++) {
            if (occupied[i]) {
                sb.append('1');
            } else {
                sb.append('0');
            }
        }
        return sb.toString();
    }


    String symmID() {
        if (symmID == null) {
            symmID = calculateSymmID();
        }

        return symmID;
    }


    private String calculateSymmID() {
        SymmetryHelper h = new SymmetryHelper(board.X, board.Y, this.occupied);
        List<String> l = new ArrayList<String>();
        l.add(h.id());

        if (board.rotate270) {
            l.add(h.rotate270().id());
        }

        if (board.rotate180) {
            l.add(h.rotate180().id());
        }

        if (board.rotate90) {
            l.add(h.rotate90().id());
        }

        if (board.verticalFlip) {
            l.add(h.verticalFlip().id());
        }

        if (board.horizontalFlip) {
            l.add(h.horizontalFlip().id());
        }

        if (board.rightDiagonalFlip) {
            l.add(h.rightDiagonalFlip().id());
        }

        if (board.leftDiagonalFlip) {
            l.add(h.leftDiagonalFlip().id());
        }

        return Collections.min(l);
    }


    int score() {
        if (compactnessScore == -1) {
            compactnessScore = calculateCompactnessScore();
        }

        return compactnessScore;
    }


    /**
     * This function is critical to the speed of the solver. It calculates
     * the heuristic value that allows an aggressive pruning of the search tree.
     * <p>
     * To quantify compactness I calculate the length of the border of the position.
     * If an occupied cell is next to an empty cell on any side, a 1 is added to
     * the length of the border.
     *
     * @return int measure of the board position's border length. The longer the border's
     * length, the less likely the position is to yield a solution. Shorter border
     * means more compact position, that is more likely to produce a solution.
     */
    int calculateCompactnessScore() {
        int score = 0;

        for (int i = 0; i < occupied.length; i++) {
            if (board.allowed(i) && occupied[i]) {
                int x = i % board.X;
                int y = i / board.Y;
                if (empty(x, y + 1)) {
                    score += 1;
                }
                if (empty(x, y - 1)) {
                    score += 1;
                }
                if (empty(x + 1, y)) {
                    score += 1;
                }
                if (empty(x - 1, y)) {
                    score += 1;
                }
            }
        }

        return score;
    }


    boolean empty(int x, int y) {
        if (x < 0) return true;
        if (y < 0) return true;
        if (x >= board.X) return true;
        if (y >= board.Y) return true;

        int pos = y * board.X + x;
        if (!board.allowed(pos)) return true;
        if (!occupied[pos]) return true;
        return false;
    }


    boolean isValidMove(int x1, int y1, int x2, int y2) {
        if (x1 < 0) return false;
        if (x2 < 0) return false;
        if (y1 < 0) return false;
        if (y2 < 0) return false;
        if (x1 >= board.X) return false;
        if (x2 >= board.X) return false;
        if (y1 >= board.Y) return false;
        if (y2 >= board.Y) return false;

        int i1 = y1 * board.X + x1;
        int i2 = y2 * board.X + x2;

        if (!board.allowed(i1)) return false;
        if (!board.allowed(i2)) return false;
        if (!occupied[i1]) return false;
        if (occupied[i2]) return false;

        return true;
    }


    // private pseudo constructor. To be used by the children method
    private Position beget(int x1, int y1, int x2, int y2, int x3, int y3) {
        Position child = new Position(board);
        for (int i = 0; i < occupied.length; i++) {
            child.set(i, occupied[i]);
        }

        child.set(x1, y1, false);
        child.set(x2, y2, false);
        child.set(x3, y3, true);

        child.history.addAll(this.history);
        child.history.add(new Move(x1, y1, x3, y3));

        return child;
    }


    List<Position> children() {
        List<Position> children = new ArrayList<Position>();

        for (int i = 0; i < occupied.length; i++) {
            if (board.allowed(i) && occupied[i]) {
                int x = i % board.X;
                int y = i / board.X;

                if (isValidMove(x, y + 1, x, y + 2)) {
                    children.add(beget(x, y, x, y + 1, x, y + 2));
                }

                if (isValidMove(x, y - 1, x, y - 2)) {
                    children.add(beget(x, y, x, y - 1, x, y - 2));
                }

                if (isValidMove(x + 1, y, x + 2, y)) {
                    children.add(beget(x, y, x + 1, y, x + 2, y));
                }

                if (isValidMove(x - 1, y, x - 2, y)) {
                    children.add(beget(x, y, x - 1, y, x - 2, y));
                }

            }
        }

        return children;
    }


    List<Move> getHistory() {
        return history;
    }
}
