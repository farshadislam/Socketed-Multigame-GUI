package org.seng.gamelogic.tictactoe;

public class TicTacToeBoard {

    public static final int SIZE = 3;

    public enum Mark {
        EMPTY, X, O
    }

    private Mark[][] board;

    public TicTacToeBoard() {
        board = new Mark[SIZE][SIZE];
        initializeBoard();
    }

    // initialize the board to all EMPTY cells
    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = Mark.EMPTY;
            }
        }
    }

    // check if a move is valid (in bounds and on an empty cell)
    public boolean validMove(int row, int col) {
        return row >= 0 && row < SIZE &&
                col >= 0 && col < SIZE &&
                board[row][col] == Mark.EMPTY;
    }

    // attempt to make a move, return true if successful
    public boolean makeMove(int row, int col, Mark mark) {
        if (validMove(row, col)) {
            board[row][col] = mark;
            return true;
        }
        return false;
    }

    // get the mark at a specific board cell
    public Mark getMark(int row, int col) {
        return board[row][col];
    }

    // set the mark at a specific board cell
    public void setMark(int row, int col, Mark mark) {
        board[row][col] = mark;
    }

    // reset the board to empty
    public void resetBoard() {
        initializeBoard();
    }

    // display the current board state in the console
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
                        System.out.print(". "); // dot for empty cell
                        break;
                }
            }
            System.out.println();
        }
    }
}
