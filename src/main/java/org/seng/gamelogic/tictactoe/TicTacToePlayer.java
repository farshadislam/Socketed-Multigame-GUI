package org.seng.gamelogic.tictactoe;

import org.seng.authentication.Player;

/**
 * Represents a Tic-Tac-Toe player, extending the generic Player class.
 */
public class TicTacToePlayer extends Player {

    /** The color of the player's piece (not currently used in logic). */
    private String pieceColor;

    /**
     * Constructs a TicTacToePlayer with the specified attributes.
     *
     * @param username     The name of the player.
     * @param email The email connected to the player account.
     * @param password The password for the player account.
     */
    public TicTacToePlayer(String username, String email, String password) {
        super(username, email, password);
    }

    /**
     * Determines if the player is ready to start the game.
     * Currently, always returns true.
     *
     * @return true, indicating the player is ready.
     */
    public boolean readyStart() {
        return true;
    }

}
