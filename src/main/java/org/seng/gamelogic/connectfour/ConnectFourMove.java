package org.seng.gamelogic.connectfour;

import org.seng.gamelogic.Player;

/**
 * Represents a move in the Connect Four game.
 * This class stores information about the player making the move
 * and the column and row where the move is made.
 */
public class ConnectFourMove {

    /** The player making the move. To be changed so it refers to ExtendedHumanPlayer. */
    public ConnectFourPlayer player;

    /** The column where the piece is dropped. */
    public int column;

    /** The row where the piece is placed. */
    public int row;

    /**
     * Constructs a new ConnectFourMove.
     *
     * @param player The player making the move.
     * @param column The column where the piece is dropped.
     * @param row The row where the piece is placed.
     */
    public ConnectFourMove(ConnectFourPlayer player, int column, int row) {
        this.player = player;
        this.column = column;
        this.row = row;
    }

    /**
     * Returns a string describing the move details.
     * This can be adapted for visual display (GUI) if needed.
     *
     * @return A string representing the move details.
     */
    public String getMoveDetails() {
        String message = "Player " + player.getName() + " has dropped a piece in column " + column;
        return message;
    }

    // Additional methods for extended functionality can be added here.
}