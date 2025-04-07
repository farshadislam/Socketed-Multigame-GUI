package org.seng.gamelogic.tictactoe;

import org.seng.gamelogic.Player;

public class TicTacToePlayer extends Player {
    private String pieceColor;
    // Not really sure how the constructor is going to exactly work, but this is just a skeleton code so whatever
    // Everything under this is theoretical code however im not entirely sure this is correct
    public TicTacToePlayer(String name, int playerID, char symbol, int rank) {
        super(name, playerID, symbol, rank);
    }


    public boolean readyStart() {
        return true;
    }


    public char getSymbol() {
        return symbol;
    }


    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

}
