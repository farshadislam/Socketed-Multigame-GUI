package org.seng.gamelogic.checkers;

import java.util.ArrayList;
import java.util.List;

public class CheckersBoard {

    /**
     *  The checkerboard has a width of 8 spots and a length of 8 spots.
     */
    public static final int BOARD_SIZE = 8;

    /**
     * These enums represent the different checkers pieces possible on a checkerboard
     */
    public enum Piece {
        EMPTY, RED, BLACK, RED_KING, BLACK_KING
    }

    private Piece[][] board;

    public CheckersBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    /**
     * initializeBoard() method creates the starting checkerboard before any player makes a turn.
     */
    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) { // only dark squares
                    if (row < 3) {
                        board[row][col] = Piece.RED;
                    } else if (row > 4) {
                        board[row][col] = Piece.BLACK;
                    } else {
                        board[row][col] = Piece.EMPTY;
                    }
                } else {
                    board[row][col] = Piece.EMPTY;
                }
            }
        }
    }
    /**
     * This method prints the entire board which includes all the pieces and empty spots.
     */
    public void printBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.print(pieceToChar(board[row][col]) + " ");
            }
            System.out.println();
        }
    }

    /**
     * Uses the type of the piece parameter to output a certain character.
     * @return A character representing whether a piece is red, a red king, black or a black king.
     */
    private char pieceToChar(Piece piece) {
        switch (piece) {
            case RED: return 'r';
            case BLACK: return 'b';
            case RED_KING: return 'R';
            case BLACK_KING: return 'B';
            default: return '.';
        }
    }

    /**
     * isValidMove() checks is a piece is able to move from one location in the board to the next
     * @return Checks if a piece can move to a new location. True means the piece can move to this new location. False means that the piece cannot move to this new location.
     */
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) {
            return false;
        }

        Piece current_piece = board[fromRow][fromCol];
        Piece next_move = board[toRow][toCol];

        int col_dist = Math.abs(toCol - fromCol);
        int row_dist = toRow - fromRow;
        boolean is_king = current_piece == Piece.BLACK_KING || current_piece == Piece.RED_KING;
        int row_dir = (current_piece == Piece.BLACK || current_piece == Piece.BLACK_KING) ? -1 : 1;

        if (current_piece == Piece.EMPTY || next_move != Piece.EMPTY) {
            return false;
        }

        if ((row_dist == row_dir || (is_king && Math.abs(row_dist) == 1)) && col_dist == 1) {
            return true;
        }

        if ((row_dist == (2 * row_dir) || (is_king && Math.abs(row_dist) == 2)) && col_dist == 2) {
            int op_piece_row = fromRow + (row_dist/2);
            int op_piece_col = fromCol + ((toCol - fromCol)/2);
            Piece op_piece = board[op_piece_row][op_piece_col];

            boolean red_jump_black = (current_piece == Piece.RED || current_piece == Piece.RED_KING) && (op_piece == Piece.BLACK || op_piece == Piece.BLACK_KING);
            boolean black_jump_red = (current_piece == Piece.BLACK || current_piece == Piece.BLACK_KING) && (op_piece == Piece.RED || op_piece == Piece.RED_KING);

            return red_jump_black || black_jump_red;
        }

        return false;
    }

    /**
     * Checks if a piece is inside the boundaries of the board
     * @return This method returns true if the location specified by the row and column values are inside the board or not. If there are, then it returns true. If not, it returns false.
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * This method moves a piece to a new location.
     * @return This method returns true if the piece movement was successful. It returns false is the movement is not successful.
     */
    public boolean makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        Piece piece = board[fromRow][fromCol];
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = Piece.EMPTY;

        if (Math.abs(toRow - fromRow) == 2) {
            int jumpedRow = (fromRow + toRow) / 2;
            int jumpedCol = (fromCol + toCol) / 2;
            board[jumpedRow][jumpedCol] = Piece.EMPTY;
        }

        if (piece == Piece.RED && toRow == 0) {
            board[toRow][toCol] = Piece.RED_KING;
        }
        if (piece == Piece.BLACK && toRow == BOARD_SIZE - 1) {
            board[toRow][toCol] = Piece.BLACK_KING;
        }

        return true;
    }

    /**
     * This gets the piece located at a position in board.
     * @return This method returns a piece on the board, specified by the row and column values.
     */
    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    /**
     * This sets the piece at a particular location on the board.
     */
    public void setPieceAt(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    /**
     * display() prints the current board which includes all the pieces and empty spots
     */
    public void display() {
        System.out.println("Current board:");
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.print(pieceToChar(board[row][col]) + " ");
            }
            System.out.println();
        }
        System.out.println(" 0 1 2 3 4 5 6 7");
    }


    //everything under here is experimental
    //this function is checking if there is capturable pieces from a certain spot and gives a list

    public List<int[]> getCapturablePieces(int fromRow, int fromCol, Piece piece) {
        List<int[]> capturableLocations = new ArrayList<>();
        if (piece == Piece.EMPTY) {
            return capturableLocations;//if theres no pieces present it just returns an empty list
        }
        int[] directions; //initialization, nothing serious
        if (piece == Piece.RED_KING || piece == Piece.BLACK_KING) {
            directions = new int[]{-1, 1}; //since kings can move both up and down they should check for pieces both up and down the rows
        } else if (piece == Piece.RED) {
            directions = new int[]{-1}; //since red is at the top conventionally it can only move down visually (up in row count)
        } else if (piece == Piece.BLACK) {
            directions = new int[]{1}; // since black is at the bottom conventionally it can only move up visually (down in row count)
        } else {
            directions = new int[]{-1, 1}; //honestly useless, idk why i even put this here
        }


        for (int rowDir : directions) { //for each row in the directions the piece can go (what we just did earlier)
            for (int colDir : new int[]{-1, 1}) { //in terms of columns pieces can move both left and right no problem
                int targetRow = fromRow + 2 * rowDir; //first we need to check if theres nothing blocking pieces from being captured
                int targetCol = fromCol + 2 * colDir; //technically you move left and up twice in order to capture a piece


                if (inBounds(targetRow, targetCol) && board[targetRow][targetCol] == Piece.EMPTY) { //as long as its in the bounds of the board, ando nothing is blocking it, the if function activates
                    int middleRow = fromRow + rowDir;
                    int middleCol = fromCol + colDir;//finding the middle piece (depending on the piece calculation is different)
                    Piece middlePiece = board[middleRow][middleCol]; //checks what piece it is, we have to make sure that it is not one of the player's own chips

                    //this if statement checking if the middle piece is dfferent
                    if ((piece == Piece.RED || piece == Piece.RED_KING) && (middlePiece == Piece.BLACK || middlePiece == Piece.BLACK_KING) ||
                            (piece == Piece.BLACK || piece == Piece.BLACK_KING) && (middlePiece == Piece.RED || middlePiece == Piece.RED_KING)) {

                        capturableLocations.add(new int[]{targetRow, targetCol}); //this returns the locations where it can go basically, into the list (can be upto 4 if its a king!)
                    }
                }
            }
        }
        return capturableLocations;
    }

}

}
