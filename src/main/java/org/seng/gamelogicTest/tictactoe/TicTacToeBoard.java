package org.seng.gamelogicTest.tictactoe;

/**
 * Represents a Tic-Tac-Toe game board.
 */
public class TicTacToeBoard {

    /** The size of the Tic-Tac-Toe board (3x3). */
    public static final int SIZE = 3;

    /** Enum representing the possible marks on the board: EMPTY, X, or O. */
    public enum Mark {
        EMPTY, X, O
    }

    /** The game board represented as a 2D array of Marks. */
    private Mark[][] board;

    /**
     * Constructs a TicTacToeBoard and initializes all cells to EMPTY.
     */
    public TicTacToeBoard() {
        board = new Mark[SIZE][SIZE];
        initializeBoard();
    }

    /**
     * Initializes the board, setting all cells to EMPTY.
     */
    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = Mark.EMPTY;
            }
        }
    }

    /**
     * Checks if a move is valid.
     *
     * @param row The row index (0-2).
     * @param col The column index (0-2).
     * @return true if the move is within bounds and the cell is empty, false otherwise.
     */
    public boolean validMove(int row, int col) {
        return row >= 0 && row < SIZE &&
                col >= 0 && col < SIZE &&
                board[row][col] == Mark.EMPTY;
    }

    /**
     * Attempts to place a mark on the board.
     *
     * @param row  The row index (0-2).
     * @param col  The column index (0-2).
     * @param mark The mark (X or O) to place.
     * @return true if the move was successful, false if the move was invalid.
     */
    public boolean makeMove(int row, int col, Mark mark) {
        if (validMove(row, col)) {
            board[row][col] = mark;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the mark at a specified board cell.
     *
     * @param row The row index (0-2).
     * @param col The column index (0-2).
     * @return The Mark at the given position.
     */
    public Mark getMark(int row, int col) {
        return board[row][col];
    }

    /**
     * Sets the mark at a specific board cell.
     *
     * @param row  The row index (0-2).
     * @param col  The column index (0-2).
     * @param mark The mark (X or O) to set.
     */
    public void setMark(int row, int col, Mark mark) {
        board[row][col] = mark;
    }

    /**
     * Displays the current board state in the console.
     */
    public void display() {
        System.out.println("Current board:");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                switch (board[row][col]) {
                    case X:
                        System.out.print("X ");
                        break;
                    case O:
                        System.out.print("O ");
                        break;
                    default:
                        System.out.print(". "); // Dot represents an empty cell
                        break;
                }
            }
            System.out.println();
        }
    }
}
