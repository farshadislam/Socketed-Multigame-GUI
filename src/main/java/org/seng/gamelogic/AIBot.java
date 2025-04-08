package org.seng.gamelogic;

public class AIBot extends org.seng.gamelogic.Player {

    public AIBot(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);
    }

    public Object nextMove(Object board) {
        return board;
    }
}