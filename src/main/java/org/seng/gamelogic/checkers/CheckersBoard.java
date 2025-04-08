package org.seng.gamelogic.checkers;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Checkers game board with basic game logic.
 */
public class CheckersBoard {

    /**
     * The size of the Checkers board (8x8 grid).
     */
    public static final int BOARD_SIZE = 8;

    /**
     * Enum representing different types of pieces on the board.
     */
    public enum Piece {
        EMPTY, RED, BLACK, RED_KING, BLACK_KING
    }

    /**
     * The game board represented as a 2D array of Pieces.
     */
    private Piece[][] board;

    /**
     * Constructs a new Checkers board and initializes it with starting positions.
     */
    public CheckersBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();
    }

    /**
     * Initializes the board with pieces in their starting positions.
     */
    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) { // Pieces only placed on dark squares
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

    /**
     * Prints the board to the console using characters to represent pieces.
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
     * Converts a Piece to its character representation.
     *
     * @param piece The piece to convert.
     * @return The corresponding character.
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
     * Checks if a move is valid according to Checkers rules.
     *
     * @param fromRow The row of the starting position.
     * @param fromCol The column of the starting position.
     * @param toRow The row of the target position.
     * @param toCol The column of the target position.
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!inBounds(fromRow, fromCol) || !inBounds(toRow, toCol)) {
            return false;
        }

        Piece currentPiece = board[fromRow][fromCol];
        Piece nextMove = board[toRow][toCol];

        int colDist = Math.abs(toCol - fromCol);
        int rowDist = toRow - fromRow;
        boolean isKing = currentPiece == Piece.BLACK_KING || currentPiece == Piece.RED_KING;
        int rowDir = (currentPiece == Piece.BLACK || currentPiece == Piece.BLACK_KING) ? -1 : 1;

        if (currentPiece == Piece.EMPTY || nextMove != Piece.EMPTY) {
            return false;
        }

        if ((rowDist == rowDir || (isKing && Math.abs(rowDist) == 1)) && colDist == 1) {
            return true;
        }

        if ((rowDist == (2 * rowDir) || (isKing && Math.abs(rowDist) == 2)) && colDist == 2) {
            int midRow = fromRow + (rowDist / 2);
            int midCol = fromCol + ((toCol - fromCol) / 2);
            Piece midPiece = board[midRow][midCol];

            boolean redCapturesBlack = (currentPiece == Piece.RED || currentPiece == Piece.RED_KING) && (midPiece == Piece.BLACK || midPiece == Piece.BLACK_KING);
            boolean blackCapturesRed = (currentPiece == Piece.BLACK || currentPiece == Piece.BLACK_KING) && (midPiece == Piece.RED || midPiece == Piece.RED_KING);

            return redCapturesBlack || blackCapturesRed;
        }

        return false;
    }

    /**
     * Checks if a position is within the board bounds.
     */
    private boolean inBounds(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    /**
     * Move a piece from its original location to a new location.
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

        if (piece == Piece.RED && toRow == (BOARD_SIZE - 1)) {
            board[toRow][toCol] = Piece.RED_KING;
        }
        if (piece == Piece.BLACK && toRow == 0) {
            board[toRow][toCol] = Piece.BLACK_KING;
        }

        return true;
    }

    /**
     * Gets the piece at a specified position.
     */
    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    /**
     * Sets a piece at a specified position.
     */
    public void setPieceAt(int row, int col, Piece piece) {
        board[row][col] = piece;
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





