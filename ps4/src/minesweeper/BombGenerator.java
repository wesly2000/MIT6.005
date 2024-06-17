package minesweeper;

/**
 * Represent the bomb generation mechanism for the given coordinate.
 */
public interface BombGenerator {
    /**
     * Decide whether the given
     * @param r must lie in the valid range (0 <= r < row)
     * @param c must lie in the valid range (0 <= c < col)
     * @return
     */
    boolean generate(int r, int c);
}
