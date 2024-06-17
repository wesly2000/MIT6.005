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

    Board(int row, int col, BombGenerator generator) {
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

    Board(int row, int col) { this(row, col, (r, c) -> false); }

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
                        .filter((neighbour) -> getCellState(neighbour) == Cell.UNTOUCHED && checkCellBomb(neighbour))
                        .count();
        return count;
    }

    // Set the dug state of a cell, i.e., set its state to ' ' or '1'-'8' according to
    // the number of bombs.
    private void setDug(Cell cell, int count){
        if (count == 0) setCellState(cell, Cell.ZEROBOMB);
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
                if(getCellState(neighbour) == Cell.UNTOUCHED)
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
    public boolean dig(int r, int c){
        Cell cell;
        try{
            cell = getCell(r, c);
        }catch(AssertionError e){
            return false; // Invalid coords
        }
        if(getCellState(cell) != Cell.UNTOUCHED)
            return false;

        boolean hasBomb = checkCellBomb(cell);
        if(hasBomb){
            setCellBomb(cell, Cell.NOTBOMB); // Remove the bomb
            Set<Cell> neighbours = getNeighbours(cell);
            for(Cell neighbour : neighbours){
                if(getCellState(neighbour) != Cell.UNTOUCHED && getCellState(neighbour) != Cell.FLAGGED){
                    // Decrease the number of neighbouring bombs by 1
                    int prevBombCount = (int)getCellState(neighbour) - '0';
                    setDug(neighbour, prevBombCount - 1);
                }
            }
        }
        sniff(cell);
        return hasBomb;
    }

    /**
     * Flag an untouched cell.
     * @param r the row of the cell.
     * @param c the column of the cell.
     */
    public void flag(int r, int c){
        Cell cell;
        try{
            cell = getCell(r, c);
        }catch(AssertionError e){
            return; // Invalid coords
        }
        if(getCellState(cell) == Cell.UNTOUCHED){
            setCellState(cell, Cell.FLAGGED);
        }
    }

    /**
     * Deflag a flagged cell, change it to untouched.
     * @param r the row of the cell.
     * @param c the column of the cell.
     */
    public void deflag(int r, int c){
        Cell cell;
        try{
            cell = getCell(r, c);
        }catch(AssertionError e){
            return; // Invalid coords
        }
        if(getCellState(cell) == Cell.FLAGGED){
            setCellState(cell, Cell.UNTOUCHED);
        }
    }

    @Override public String toString(){
        String boardString = "";
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                boardString += getCellState(getCell(r, c)) + " ";
            }
            boardString += '\n';
        }
        return boardString;
    }

    void checkRep(){
        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++)
                getCell(r, c).checkRep();
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
    public static final char FLAGGED = 'F';
    public static final char UNTOUCHED = '-';
    public static final char ZEROBOMB = ' ';

    public static final boolean BOMB = true;
    public static final boolean NOTBOMB = false;

    private Set<Character> validStates = new HashSet<>(Arrays.asList(FLAGGED, UNTOUCHED, ZEROBOMB, '1', '2', '3', '4', '5', '6', '7', '8'));

    private final int r;
    private final int c;
    private char state;
    private boolean isBomb;

    Cell(int r, int c, boolean isBomb){
        this.r = r;
        this.c = c;
        this.state = UNTOUCHED;
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