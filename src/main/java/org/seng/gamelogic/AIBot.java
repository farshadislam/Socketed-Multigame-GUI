package org.seng.gamelogic.connectfour;


public class AIBot extends main.java.org.seng.gamelogic.Player {

    public AIBot(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);
    }

    public Object nextMove(Object board) {
        return board;
    }
}