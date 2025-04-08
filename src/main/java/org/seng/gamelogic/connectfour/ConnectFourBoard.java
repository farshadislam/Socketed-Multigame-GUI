package org.seng.gamelogic.connectfour;
import org.seng.gamelogic.Player;

public class ConnectFourBoard {

    /**
     * Constant variables required for the length and height of the board
     */
    public static final int ROW_COUNT = 6;
    public static final int COL_COUNT = 7;

    /**
     * Enum for Chip types that the board is built out of
     * Chips can either be blue, yellow, or empty
     */
    public enum Chip {
        EMPTY, BLUE, YELLOW
    }

    /**
     * Creates the grid setup for the Connect Four board
     */
    private Chip[][] grid;

    /**
     * Initializes the Connect Four board through creating a grid and calling the initialize function
     */
    public ConnectFourBoard () {
        grid = new Chip[ROW_COUNT][COL_COUNT];
        initializeBoard();
    }

    /**
     * Ensures each chip on a grid is assigned the value Empty
     */
    private void initializeBoard() {
        for (int row = 0; row < ROW_COUNT; row ++){
            for (int col = 0; col < COL_COUNT; col ++){
                grid[row][col] = Chip.EMPTY;
            }
        }
    }

    /**
     * Allows a player to drop a Chip onto the grid
     * @param col the column the player would like to drop a chip onto
     * @param player the player that is playing the chip, which determines the color of the dropped chip
     * @return true if the chip was successfully dropped, false otherwise
     */
    public boolean dropPiece(int col, Player player) {
        // Checks if the column is a valid column number
        if (!isColumn(col)){
            return false;
            // Checks if the column has room for a chip to be added
        } else if (columnFull(col)) {
            return false;
        } else {
            // Cycles down each row of the grid
            for (int row = 0; row < ROW_COUNT; row++) {
                // If the current row has a Chip other than Empty, places a chip in the row above
                if (grid[row][col] != Chip.EMPTY) {
                    if (player.getSymbol() == 'b') {
                        grid[row - 1][col] = Chip.BLUE;
                        return true;
                    } else {
                        grid[row - 1][col] = Chip.YELLOW;
                        return true;
                    }
                    // If the current row is the last row of the board, places the chip
                } else if (row == ROW_COUNT - 1) {
                    if (player.getSymbol() == 'b') {
                        grid[row][col] = Chip.BLUE;
                        return true;
                    } else {
                        grid[row][col] = Chip.YELLOW;
                        return true;
                    }
                }
            }
        }
        // If an alternate error occurs, returns false
        return false;
    }

    /**
     * Checks whether a column is full when called
     * @param col the column to be checked for space to drop a chip
     * @return true if there is room in the column for a chip to be placed, false otherwise
     */
    public boolean columnFull(int col) {
        if (grid[0][col] == Chip.EMPTY) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks that the column selected is a valid column number
     * @param col is the column number given
     * @return true if the column is a column number on the board, false otherwise
     */
    public boolean isColumn(int col) {
        if ((col < 0) || (col > 7)){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks whether a move can be made in the selected column
     * @param col the column to check
     * @return true if the move is valid (column exists and is not full), false otherwise
     */
    public boolean validMove(int col) {
        return isColumn(col) && !columnFull(col);
    }

    /**
     * Resets the board layout so that all Chips are marked as Empty by calling the initialize function
     */
    public void resetBoard() {
        initializeBoard();
    }

    /**
     * Gets a Chip from the board
     * @param row the row the Chip is in
     * @param col the column the Chip is in
     * @return the Chip that is found in that location on the board
     */
    public Chip getChip(int row, int col) {
        return grid[row][col];
    }

    /**
     * Sets a Chip value in the board
     * @param row the row of the board
     * @param col the column of the board
     * @param chip the Chip to be placed
     */
    public void setChip(int row, int col, Chip chip) {
        grid[row][col] = chip;
    }

    /**
     * Displays the current board layout in the console for visual reference
     * Prints b for BLUE, y for YELLOW, and . for EMPTY
     */
    public void display() {
        System.out.println("Current board:");
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COL_COUNT; col++) {
                switch (grid[row][col]) {
                    case BLUE:
                        System.out.print("b ");
                        break;
                    case YELLOW:
                        System.out.print("y ");
                        break;
                    default:
                        System.out.print(". ");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println("0 1 2 3 4 5 6"); // column index labels
    }
}
