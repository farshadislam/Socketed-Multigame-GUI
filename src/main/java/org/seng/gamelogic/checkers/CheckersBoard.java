package main.java.org.seng.gamelogic.checkers;

public class CheckersBoard {

    public static final int BOARD_SIZE = 8;

    public enum Piece {
        EMPTY, RED, BLACK, RED_KING, BLACK_KING
    }

    private Piece[][] board;

    public CheckersBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) { // only dark squares
                    if (row < 3) {
                        board[row][col] = Piece.BLACK;
                    } else if (row > 4) {
                        board[row][col] = Piece.RED;
                    } else {
                        board[row][col] = Piece.EMPTY;
                    }
                } else {
                    board[row][col] = Piece.EMPTY;
                }
            }
        }
    }

    public void printBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.print(pieceToChar(board[row][col]) + " ");
            }
            System.out.println();
        }
    }

    private char pieceToChar(Piece piece) {
        switch (piece) {
            case RED: return 'r';
            case BLACK: return 'b';
            case RED_KING: return 'R';
            case BLACK_KING: return 'B';
            default: return '.';
        }
    }
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        return false;
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        return false;
    }

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public void setPieceAt(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

}
