package org.seng.gamelogic.connectfour;

import org.seng.gamelogic.Player;

/**
 * Represents a player in a Connect Four game.
 * This class extends the generic Player class and includes Connect Four-specific functionality.
 */
public class ConnectFourPlayer extends Player {

    /**
     * Constructs a new ConnectFourPlayer with the specified attributes.
     *
     * @param username The username of the player.
     * @param playerID The unique identifier of the player.
     * @param symbol The symbol representing the player's pieces ('X' or 'O').
     * @param rank The player's rank in the game.
     */
    public ConnectFourPlayer(String username, int playerID, char symbol, int rank) {
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
     * Retrieves the symbol representing the player's pieces.
     *
     * @return The character symbol of the player's pieces.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Sets the symbol representing the player's pieces.
     *
     * @param symbol The new symbol to assign to the player.
     */
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
