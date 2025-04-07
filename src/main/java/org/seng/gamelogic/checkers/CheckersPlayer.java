package org.seng.gamelogic.checkers;

import org.seng.gamelogic.Player;
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

}
