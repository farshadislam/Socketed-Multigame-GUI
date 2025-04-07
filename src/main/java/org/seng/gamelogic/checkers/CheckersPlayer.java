package org.seng.gamelogic.checkers;

import org.seng.gamelogic.Player;
import org.seng.gamelogic.checkers.CheckersMove;
import org.seng.gamelogic.checkers.CheckersBoard;

/**
 * Represents a player in a Checkers game.
 * This class extends the generic Player class and includes checkers-specific functionality.
 */
public class CheckersPlayer extends Player {

    /**
     * Constructs a new CheckersPlayer with the specified attributes.
     *
     * @param username The username of the player.
     * @param playerID The unique identifier of the player.
     * @param symbol The symbol representing the player's pieces ('r' for red, 'b' for black).
     * @param rank The player's rank in the game.
     */
    public CheckersPlayer(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);
    }

    /**
     * Indicates whether the player is ready to start the game.
     * @return Always returns true, indicating the player is ready.
     */
    public boolean readyStart() {
        return true;
    }

    /**
     * Attempts to make a move on the given Checkers board.
     *
     * @param board The CheckersBoard where the move will be made.
     * @param move The move to be executed.
     * @return True if the move is successfully made, false otherwise.
     */
    public boolean makeMove(CheckersBoard board, CheckersMove move) {
        // Extract move coordinates from the CheckersMove object
        int rowStart = move.getRowStart();
        int rowEnd = move.getRowEnd();
        int colStart = move.getColStart();
        int colEnd = move.getColEnd();

        // Attempt to make the move on the board
        boolean movePiece = board.makeMove(rowStart, colStart, rowEnd, colEnd);
        return movePiece;
    }
}
