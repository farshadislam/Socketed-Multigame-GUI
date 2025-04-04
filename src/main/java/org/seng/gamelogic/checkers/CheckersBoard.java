package org.seng.gamelogic.checkers;

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
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) { // this method is still unfinished
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) {
            return false;
        }
        Piece piece = board[fromRow][fromCol];
        Piece destination = board[toRow][toCol];
        if (destination != Piece.EMPTY) return false;

        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;
        return false;
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        Piece piece = board[fromRow][fromCol];
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = Piece.EMPTY;

        // If it's a jump, remove the captured piece
        if (Math.abs(toRow - fromRow) == 2) {
            int jumpedRow = (fromRow + toRow) / 2;
            int jumpedCol = (fromCol + toCol) / 2;
            board[jumpedRow][jumpedCol] = Piece.EMPTY;
        }

        // Promote to king
        if (piece == Piece.RED && toRow == 0) {
            board[toRow][toCol] = Piece.RED_KING;
        }
        if (piece == Piece.BLACK && toRow == BOARD_SIZE - 1) {
            board[toRow][toCol] = Piece.BLACK_KING;
        }

        return true;
    }

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public void setPieceAt(int row, int col, Piece piece) {
        board[row][col] = piece;
    }



}
