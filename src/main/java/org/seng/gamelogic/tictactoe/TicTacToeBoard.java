package main.java.org.seng.gamelogic.tictactoe;

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

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = Mark.EMPTY;
            }
        }
    }

    public boolean validMove(int row, int col) {
        return row >= 0 && row < SIZE &&
               col >= 0 && col < SIZE &&
               board[row][col] == Mark.EMPTY;
    }
    public boolean makeMove(int row, int col, Mark mark) {
        if (validMove(row, col)) {
            board[row][col] = mark;
            return true;
        }
        return false;
    }

    public Mark getMark(int row, int col) {
        return board[row][col];
    }

    public void setMark(int row, int col, Mark mark) {
        board[row][col] = mark;
    }

    public void resetBoard() {
        initializeBoard();
    }

}
