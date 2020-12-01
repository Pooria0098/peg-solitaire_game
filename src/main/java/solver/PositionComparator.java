package solver;

import java.io.Serializable;
import java.util.Comparator;

/**
 * PositionComparator implements the comparison operations on Position class.
 */
public class PositionComparator implements Comparator<Position>, Serializable {

    private static final long serialVersionUID = 13033L;

    @Override
    public int compare(Position b1, Position b2) {
        int score1 = b1.score();
        int score2 = b2.score();
        if (score1 < score2) return -1;
        if (score1 == score2) return 0;
        return 1;
    }
}
