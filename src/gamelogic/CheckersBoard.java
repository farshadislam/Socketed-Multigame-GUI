package gamelogic;

public class CheckersBoard {

    public static final int BOARD_SIZE = 8;

    public enum Piece {
        EMPTY, RED, BLACK, RED_KING, WHITE_KING, BLACK_KING

    }

    private Piece[][] board;

    public CheckersBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();

    }

    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) {
                    board[row][col] = Piece.BLACK;
                } else if (row > 4) {
                    board[row][col] = Piece.RED;
                }
            } else {
                board[row][col] = Piece.EMPTY;
            }
        }
    }
}