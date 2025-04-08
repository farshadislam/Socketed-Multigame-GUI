package org.seng.gamelogic.tictactoe;

import org.seng.gamelogic.Player;

/**
 * Represents a Tic-Tac-Toe player, extending the generic Player class.
 */
public class TicTacToePlayer extends Player {

    /** The color of the player's piece (not currently used in logic). */
    private String pieceColor;

    /**
     * Constructs a TicTacToePlayer with the specified attributes.
     *
     * @param name     The name of the player.
     * @param playerID The unique identifier for the player.
     * @param symbol   The character representing the player's mark (X or O).
     * @param rank     The player's rank (if applicable).
     */
    public TicTacToePlayer(String name, int playerID, char symbol, int rank) {
        super(name, playerID, symbol, rank);
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

    /**
     * Retrieves the player's symbol (X or O).
     *
     * @return The character representing the player's mark.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Sets the player's symbol (X or O).
     *
     * @param symbol The character to assign as the player's mark.
     */
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
