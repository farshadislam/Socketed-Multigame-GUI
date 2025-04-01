package org.seng.gamelogic.connectfour;
import main.java.org.seng.gamelogic.Player;

public class ConnectFourMove {
    public Player player; // to be changed so it refers to ExtendedHumanPlayer
    public int column;
    public int row;


    public ConnectFourMove(Player player, int column, int row) {
        this.player = player;
        this.column = column;
        this.row = row;
    }

    // returns string of move details. This can be a visual display (GUI) if needed?
    public String getMoveDetails() {
        String message = "Player " + player.getName() + "has dropped a piece in column " + column;
        return message;
    }

    // add other methods for functionality

}