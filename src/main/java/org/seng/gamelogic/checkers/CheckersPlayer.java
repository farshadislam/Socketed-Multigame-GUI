package org.seng.gamelogic.checkers;

import org.seng.authentication.Player;
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
     * @param email The email of the player.
     * @param password The password for the players.
     * @param symbol The symbol representing the player's pieces ('r' for red, 'b' for black).
     */
    public CheckersPlayer(String username, String email, String password, char symbol) {
        super(username, email, password);
    }

    /**
     * Indicates whether the player is ready to startGame the game.
     * @return Always returns true, indicating the player is ready.
     */
    public boolean readyStart() {
        return true;
    }

}
