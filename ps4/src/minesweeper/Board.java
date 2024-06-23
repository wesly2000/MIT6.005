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
    //      Each of the cell's state fall in {' ', 'F', '-', '1'-'8'};
    //      the state of the cell corresponds to the number of its neighbour-with-bomb
    // Safety from rep exposure:
    //      Row, col and board are private and final;
    //      only getRow and getCol which return the row and col are exposed
    //      to the client;
    //      therefore, the board is guaranteed to be safe from rep exposure.
    // Thread safety:
    //      row and col are private and final, board is mutable but never exposed to
    //      clients;
    //      all the mutators are synchronized, and for one game there is only one board,
    //      which implies no dead locks.
    // TODO: Specify, test, and implement in problem 2
    private final int row;
    private final int col;
    private final Cell[][] board;

    public static final char FLAGGED = 'F';
    public static final char UNTOUCHED = '-';
    public static final char ZEROBOMB = ' ';

    public static final boolean BOMB = true;
    public static final boolean NOTBOMB = false;

    public Board(int row, int col, BombGenerator generator) {
        this.row = row;
        this.col = col;
        board = new Cell[row][col];
        for (int r = 0; r < row; r++)
            for(int c = 0; c < col; c++){
                boolean isBomb = generator.generate(r, c);
                board[r][c] = new Cell(r, c, isBomb);
            }

        checkRep();
    }

    public int getCol() { return col; }

    public int getRow() { return row; }

    // Check if the coordinates are valid, and return the reference to the given cell.
    private Cell getCell(int r, int c){
        assert isValidCoordinate(r, c): "Illegal coordinates row=" + r + ", col=" + c;
        return board[r][c];
    }

    // Test if the input coordinates are valid
    private boolean isValidCoordinate(int r, int c){
        return r >= 0 && r < row && c >= 0 && c < col;
    }

    // Find all the valid neighbours of a given cell
    // @return: Valid neighbours represented by a set of cells.
    private Set<Cell> getNeighbours(Cell cell){
        Set<Cell> neighbours = new HashSet<>();
        int r = cell.getRow();
        int c = cell.getCol();
        for(int rTmp = r-1; rTmp <= r+1; rTmp++)
            for(int cTmp = c-1; cTmp <= c+1; cTmp++)
                // The cell itself should be excluded
                if(!(rTmp == r && cTmp == c) && isValidCoordinate(rTmp, cTmp))
                    neighbours.add(getCell(rTmp, cTmp));
        return neighbours;
    }

    // Get the state of the given point.
    private char getCellState(Cell cell){ return cell.getState(); }

    // Set the state of the given point.
    private void setCellState(Cell cell, char state){ cell.setState(state); }

    // Check if a cell contains a bomb.
    private boolean checkCellBomb(Cell cell){ return cell.checkBomb(); }

    // Set the cell with/without a bomb
    private void setCellBomb(Cell cell, boolean isBomb){ cell.setBomb(isBomb); }

    // Count the number of neighbours with a bomb of a given cell
    private int neighbourBombCount(Cell cell){
        Set<Cell> neighbours = getNeighbours(cell);
        int count = (int) neighbours.stream()
                        .filter((neighbour) -> getCellState(neighbour) == Board.UNTOUCHED && checkCellBomb(neighbour))
                        .count();
        return count;
    }

    // Set the dug state of a cell, i.e., set its state to ' ' or '1'-'8' according to
    // the number of bombs.
    private void setDug(Cell cell, int count){
        if (count == 0) setCellState(cell, Board.ZEROBOMB);
        else
            setCellState(cell, (char)('0' + count));
    }

    // Sniff the given cell. The rule is:
    // 1. Count all the neighbours with a bomb of the cell;
    // 2. Set the state of the given cell to setDug(cell, count)
    // 3. If the count == 0, for each of the untouched neighbours, do 1-3 recursively.
    private void sniff(Cell cell){
        Set<Cell> neighbours = getNeighbours(cell);
        int count = neighbourBombCount(cell);
        setDug(cell, count);

        if(count == 0){
            for(Cell neighbour : neighbours){
                if(getCellState(neighbour) == Board.UNTOUCHED)
                    sniff(neighbour);
            }
        }
    }
    /**
     * Dig the given board cell following the steps below:
     * 1. If the cell's coords are invalid, or it has been DUG/FLAGGED, return false;
     * 2. Else, check if the given cell contains a bomb, store the result in hasBomb;
     * 3. If hasBomb == true, set its isBomb variable to false; then, each of its dug
     * neighbour should decrease the number of neighbour-with-bomb by 1.
     * 4. sniff(cell)
     *
     * @param r the row of the cell.
     * @param c the column of the cell.
     * @return if the cell of the given position contains a bomb.
     */
    public synchronized boolean dig(int r, int c){
        Cell cell;
        try{
            cell = getCell(r, c);
        }catch(AssertionError e){
            return false; // Invalid coords
        }
        if(getCellState(cell) != Board.UNTOUCHED)
            return false;

        boolean hasBomb = checkCellBomb(cell);
        if(hasBomb){
            setCellBomb(cell, Board.NOTBOMB); // Remove the bomb
            Set<Cell> neighbours = getNeighbours(cell);
            for(Cell neighbour : neighbours){
                if(getCellState(neighbour) != Board.UNTOUCHED && getCellState(neighbour) != Board.FLAGGED){
                    // Decrease the number of neighbouring bombs by 1
                    int prevBombCount = (int)getCellState(neighbour) - '0';
                    setDug(neighbour, prevBombCount - 1);
                }
            }
        }
        sniff(cell);

        checkRep();

        return hasBomb;
    }

    /**
     * Flag an untouched cell.
     * @param r the row of the cell.
     * @param c the column of the cell.
     */
    public synchronized void flag(int r, int c){
        Cell cell;
        try{
            cell = getCell(r, c);
        }catch(AssertionError e){
            return; // Invalid coords
        }
        if(getCellState(cell) == Board.UNTOUCHED){
            setCellState(cell, Board.FLAGGED);
        }

        checkRep();
    }

    /**
     * Deflag a flagged cell, change it to untouched.
     * @param r the row of the cell.
     * @param c the column of the cell.
     */
    public synchronized void deflag(int r, int c){
        Cell cell;
        try{
            cell = getCell(r, c);
        }catch(AssertionError e){
            return; // Invalid coords
        }
        if(getCellState(cell) == Board.FLAGGED){
            setCellState(cell, Board.UNTOUCHED);
        }

        checkRep();
    }

    @Override public synchronized String toString(){
        String boardString = "";
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                if(c == col - 1){
                    boardString += getCellState(getCell(r, c));
                }else
                    boardString += getCellState(getCell(r, c)) + " ";
            }
            boardString += '\n';
        }
        return boardString;
    }

    void checkRep(){
        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++){
                Cell cell = getCell(r, c);
                cell.checkRep();
                // Check for bomb number and cell state consistency
                char state = getCellState(cell);
                if(state != Board.UNTOUCHED && state != Board.FLAGGED){
                    int bombCount = neighbourBombCount(cell);
                    if(state == Board.ZEROBOMB){
                        assert bombCount == 0 :
                                String.format("Cell (%d, %d) inconsistent state %c and bomb number %d", r, c, state, bombCount);
                    }else{
                        assert bombCount == (int) state - '0' :
                                String.format("Cell (%d, %d) inconsistent state %c and bomb number %d", r, c, state, bombCount);
                    }
                }
            }
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

    private Set<Character> validStates = new HashSet<>(Arrays.asList(Board.FLAGGED, Board.UNTOUCHED, Board.ZEROBOMB, '1', '2', '3', '4', '5', '6', '7', '8'));

    private final int r;
    private final int c;
    private char state;
    private boolean isBomb;

    Cell(int r, int c, boolean isBomb){
        this.r = r;
        this.c = c;
        this.state = Board.UNTOUCHED;
        this.isBomb = isBomb;
    }

    // For test only. Otherwise, the first constructor should always be used.
//    Cell(int r, int c, char state, boolean isBomb) {
//        this.r = r;
//        this.c = c;
//        this.state = state;
//        this.isBomb = isBomb;
//        checkRep();
//    }

    int getRow(){ return r; }
    int getCol(){ return c; }
    char getState() { return state; }

    void setState(char state) {
        this.state = state;
        checkRep();
    }

    boolean checkBomb(){ return isBomb; }
    void setBomb(boolean isBomb){
        this.isBomb = isBomb;
        checkRep();
    }

    void checkRep(){
        assert validStates.contains(this.state): "Invalid state: " + this.state;
    }
}