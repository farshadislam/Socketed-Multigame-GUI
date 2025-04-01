package org.seng.gamelogic.connectfour;
import main.java.org.seng.gamelogic.HumanPlayer;

public class ConnectFourPlayer extends HumanPlayer {

    // constructor
    public ConnectFourPlayer(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);
    }

    @Override
    public boolean readyStart() {
        return true;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    // play the chip
    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

}
