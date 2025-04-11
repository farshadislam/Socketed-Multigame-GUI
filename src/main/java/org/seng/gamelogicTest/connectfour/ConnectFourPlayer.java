package org.seng.gamelogicTest.connectfour;

import org.seng.authentication.Player;

/**
 * Represents a player in a Connect Four game.
 * This class extends the generic Player class and includes Connect Four-specific functionality.
 */
public class ConnectFourPlayer extends Player {

    /**
     * Constructs a new ConnectFourPlayer with the specified attributes.
     *
     * @param username The username of the player.
     * @param email The email the player account is linked to.
     * @param password The password the player uses to log on.
     */
    public ConnectFourPlayer(String username, String email, String password) {
        super(username, email, password);
    }

    /**
     * Indicates whether the player is ready to startGame the game.
     *
     * @return Always returns true, indicating the player is ready.
     */
    public boolean readyStart() {
        return true;
    }
}
