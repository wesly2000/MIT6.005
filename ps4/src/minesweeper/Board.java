/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: Specification
 * Board is a grid of Cells
 */
public class Board {
    
    // TODO: Abstraction function, rep invariant, rep exposure, thread safety
    // Abstraction function:
    //      Represents the game board whose grid is defined as a 2-dim
    //      array of Cells.
    // Representation invariant:
    //      Each of the cell should be rep invariant
    // Safety from rep exposure:
    // TODO: Specify, test, and implement in problem 2
    private final int row;
    private final int col;
    private final Cell[][] board;

    Board(int row, int col) {
        this.row = row;
        this.col = col;
        board = new Cell[row][col];
        for (int r = 0; r < row; r++)
            for(int c = 0; c < col; c++)
                board[r][c] = new Cell();
    }

    void checkRep(){
        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++)
                board[r][c].checkRep();
    }

    @Override public String toString(){
        String boardString = "";
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                boardString += board[r][c].getState() + " ";
            }
            boardString += '\n';
        }
        return boardString;
    }
}

class Cell {
    // Abstraction function:
    //      A board cell whose state represents if a cell was
    //      dug/flagged
    // Representation invariant:
    //      The state must fall in {'F', '-', ' ', '1'-'8'}
    // Safety from rep exposure:
    //      The state is set to private, and it is of primitive type,
    //      thus it is guaranteed immutable.
    public final char FLAGGED = 'F';
    public final char UNTOUCHED = '-';
    public final char NOBOMB = ' ';

    private Set<Character> validStates = new HashSet<>(Arrays.asList(FLAGGED, UNTOUCHED, NOBOMB, '1', '2', '3', '4', '5', '6', '7', '8'));

    private char state;

    Cell(){ this.state = UNTOUCHED; }

    Cell(char state) {
        this.state = state;
        checkRep();
    }

    char getState() { return state; }
    void setState(char state) {
        this.state = state;
        checkRep();
    }

    void checkRep(){
        assert validStates.contains(this.state): "Invalid state: " + this.state;
    }
}