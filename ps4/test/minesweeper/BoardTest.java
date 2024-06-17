/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TODO: Description
 */
public class BoardTest {
    
    // TODO: Testing strategy
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO: Tests
    // This test covers the toString of Board
    @Test
    public void testToString() {
        String targetBoardString =  "- - - - \n" +
                                    "- - - - \n" +
                                    "- - - - \n";
        Board board = new Board(3, 4);
        assertEquals(targetBoardString, board.toString());
    }

    //
    // Testing strategies for dig(r, c) -> result
    //
    // Digging place(r, c): Invalid, dug, untouched.
    // result: true, false
    //

    // This test covers digging invalid, dug, untouched cells on a board full of bombs.
    @Test
    public void testDigBombBoard(){
        Board board = new Board(5, 5, (int r, int c) -> Cell.BOMB);
        String targetBoardString =  "- - - - - \n" +
                                    "- - - - - \n" +
                                    "- - - - - \n" +
                                    "- - - - - \n" +
                                    "- - - - - \n";
        assertEquals(targetBoardString, board.toString());

        // Digging an invalid position raises no effects.
        assertFalse(board.dig(-1, -1));
        assertFalse(board.dig(5, 5));
        assertTrue(board.dig(2, 2));
        // Digging a dug cell raises no effects.
        assertFalse(board.dig(2, 2));
        assertTrue(board.dig(1, 3));
        targetBoardString =
                "- - - - - \n" +
                "- - - 7 - \n" +
                "- - 7 - - \n" +
                "- - - - - \n" +
                "- - - - - \n";
        assertEquals(targetBoardString, board.toString());
    }

    // This test covers digging untouched cells on a board with no bombs.
    @Test
    public void testDigNoBombBoard(){
        Board board = new Board(5, 5, (int r, int c) -> Cell.NOTBOMB);
        assertFalse(board.dig(2, 2));
        String targetBoardString =
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n";
        assertEquals(targetBoardString, board.toString());
    }

    // The following tests cover some customized boards
    @Test
    public void testDigBoard1(){
        // The "bomb map" of the board should be
        // - - - - -
        // - B B - -
        // - - - - -
        // - B - - -
        // - - - - -
        Board board = new Board(5, 5, (int r, int c) -> {
            if((r==1 && c==1) || (r==1 && c==2) || (r==3 && c==1))
                return Cell.BOMB;
            return Cell.NOTBOMB;
        });
        assertFalse(board.dig(3, 3));
        String targetBoardString =
                "- - - 1   \n" +
                "- - - 1   \n" +
                "- - 3 1   \n" +
                "- - 1     \n" +
                "- - 1     \n";
        assertEquals(targetBoardString, board.toString());

        assertTrue(board.dig(3, 1));
        targetBoardString =
                "- - - 1   \n" +
                "- - - 1   \n" +
                "1 2 2 1   \n" +
                "          \n" +
                "          \n";
        assertEquals(targetBoardString, board.toString());
    }

    @Test
    public void testDigBoard2(){
        // The "bomb map" of the board should be
        // - - - - -
        // - B B - -
        // - - - - -
        // B B - - -
        // - - - - -
        Board board = new Board(5, 5, (int r, int c) -> {
            if((r==1 && c==1) || (r==1 && c==2) || (r==3 && c==0) || (r==3 && c==1))
                return Cell.BOMB;
            return Cell.NOTBOMB;
        });
        assertFalse(board.dig(3, 3));
        String targetBoardString =
                        "- - - 1   \n" +
                        "- - - 1   \n" +
                        "- - 3 1   \n" +
                        "- - 1     \n" +
                        "- - 1     \n";
        assertEquals(targetBoardString, board.toString());

        assertTrue(board.dig(3, 1));
        targetBoardString =
                        "- - - 1   \n" +
                        "- - - 1   \n" +
                        "- - 2 1   \n" +
                        "- 1       \n" +
                        "- -       \n";
        assertEquals(targetBoardString, board.toString());
    }
}
