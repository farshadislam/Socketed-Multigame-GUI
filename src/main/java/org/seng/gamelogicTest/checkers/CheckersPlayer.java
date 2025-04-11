package org.seng.gamelogicTest.checkers;

import org.seng.authentication.Player;

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
     */
    public CheckersPlayer(String username, String email, String password) {
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
