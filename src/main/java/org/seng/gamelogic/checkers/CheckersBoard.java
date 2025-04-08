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
    //confirmed
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

        //condition to check if move is in bounds. extra condition "!inBounds(fromRow, fromCol)" ensures in bound start
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) {
            return false;
        }

        Piece current_piece = board[fromRow][fromCol];                                                                                  //variable holding current piece in current position
        Piece next_move = board[toRow][toCol];                                                                                          //variable holding piece if it was in next move position

        int col_dist = Math.abs(toCol - fromCol);                                                                                       //column distance to next spot
        int row_dist = toRow - fromRow;                                                                                                 //row distance to next spot
        boolean is_king = current_piece == Piece.BLACK_KING || current_piece == Piece.RED_KING;                                         //boolean variable to check if piece is a king in order to negate direction later
        int row_dir = (current_piece == Piece.BLACK || current_piece == Piece.BLACK_KING) ? -1 : 1;                                     //row direction of current piece (-1 up if black, or +1 down if red),
        // kings also assigned direction at first but don't matter because later on absolut value is used to make their direction irrelevant

        //condition checking for empty start or if there is a piece in the place of the next move
        if (current_piece == Piece.EMPTY || next_move != Piece.EMPTY) {
            return false;
        }

        //condition checking if valid move is made for going diagonally up one or down one for any piece
        if ((row_dist == row_dir || (is_king && Math.abs(row_dist) == 1)) && col_dist == 1) {
            return true;
        }

        //condition checking if a valid piece jump/take move is being performed
        if ((row_dist == (2 * row_dir) || (is_king && Math.abs(row_dist) == 2)) && col_dist == 2) {
            int op_piece_row = fromRow + (row_dist/2);
            int op_piece_col = fromCol + ((toCol - fromCol)/2);
            Piece op_piece = board[op_piece_row][op_piece_col];

            boolean red_jump_black = (current_piece == Piece.RED || current_piece == Piece.RED_KING) && (op_piece == Piece.BLACK || op_piece == Piece.BLACK_KING);
            boolean black_jump_red = (current_piece == Piece.BLACK || current_piece == Piece.BLACK_KING) && (op_piece == Piece.RED || op_piece == Piece.RED_KING);

            return red_jump_black || black_jump_red;
        }

        //all other possibilities of moves lead to false as no more
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
