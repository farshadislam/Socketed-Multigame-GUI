package org.seng.gamelogicTest.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Represents an AI-controlled player in a Checkers game.
 * This bot determines its moves based on available valid moves and selects one randomly.
 */
public class AIBotCheckers extends CheckersPlayer {
    private CheckersGame game;
    private CheckersBoard board;
    private Random random;

    /**
     * Constructs an AI bot player for Checkers.
     *
     * @param symbol The symbol representing the AI's pieces ('r' or 'b').
     * @param game The Checkers game instance the AI is participating in.
     * @param board The board on which the AI will make moves.
     */
    public AIBotCheckers(char symbol, CheckersGame game, CheckersBoard board) {
        super("AIBot", "hi@gmail.com", "bloop");
        this.game = game;
        this.board = board;
        this.random = new Random();
    }

    /**
     * Determines the AI's next move based on the current state of the board.
     *
     * @param boardObj The game board.
     * @return An array representing the move [startRow, startCol, endRow, endCol], or null if no valid moves exist.
     */
    public int[] nextMove(Object boardObj) {
        if (!(boardObj instanceof CheckersBoard))
            return null; // placeholder

        CheckersBoard board = (CheckersBoard) boardObj;

        List<int[]> validMoves = getAllValidMoves(board);
        if (validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }

    /**
     * Executes a move on the board if valid.
     *
     * @param boardObj The game board.
     * @param moveObj The move to be executed, represented as an integer array.
     * @return True if the move was successfully made, false otherwise.
     */
    public boolean makeMove(Object boardObj, Object moveObj) {
        if (!(boardObj instanceof CheckersBoard) || !(moveObj instanceof int[])) {
            return false;
        }
        CheckersBoard board = (CheckersBoard) boardObj;
        int[] move = (int[]) moveObj;

        return board.makeMove(move[0], move[1], move[2], move[3]);
    }

    /**
     * Retrieves all valid moves for the AI player.
     *
     * @param board The game board.
     * @return A list of valid moves, each represented as an integer array.
     */
    private List<int[]> getAllValidMoves(CheckersBoard board) {
        List<int[]> moves = new ArrayList<>();
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                CheckersBoard.Piece piece = board.getPieceAt(row, col);
                if (isAIPiece(piece)) {
                    addValidMovesForPiece(board, row, col, moves);
                }
            }
        }
        return moves;
    }

    /**
     * Checks whether a given piece belongs to the AI player.
     *
     * @param piece The piece to check.
     * @return True if the piece is an AI-controlled piece, false otherwise.
     */
    private boolean isAIPiece(CheckersBoard.Piece piece) {
        return (piece == CheckersBoard.Piece.BLACK) || (piece == CheckersBoard.Piece.BLACK_KING);
    }

    /**
     * Adds valid moves for a given piece to the list of possible moves.
     *
     * @param board The game board.
     * @param row The row of the piece.
     * @param col The column of the piece.
     * @param moves The list of possible moves.
     */
    private void addValidMovesForPiece(CheckersBoard board, int row, int col, List<int[]> moves) {
        int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (board.isValidMove(row, col, newRow, newCol)) {
                moves.add(new int[]{row, col, newRow, newCol});
            }
        }
    }

    /**
     * Sets the symbol representing the AI's pieces.
     *
     * @param symbol The new symbol for the AI's pieces.
     */
    @Override
    public void setSymbol(char symbol) {
        this.setSymbol(symbol);
    }

    /**
     * Retrieves the symbol representing the AI's pieces.
     *
     * @return The symbol of the AI's pieces.
     */
    @Override
    public char getSymbol() {
        return this.getSymbol();
    }
}
