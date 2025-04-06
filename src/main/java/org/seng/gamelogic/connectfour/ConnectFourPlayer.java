package org.seng.gamelogic.connectfour;

import org.seng.gamelogic.Player;

public class ConnectFourPlayer extends Player {

    // constructor
    public ConnectFourPlayer(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);
    }

    public boolean readyStart() {
        return true;
    }


    public char getSymbol() {
        return symbol;
    }

    // play the chip

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

}
