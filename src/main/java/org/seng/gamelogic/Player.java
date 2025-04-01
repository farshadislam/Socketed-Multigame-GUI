package main.java.org.seng.gamelogic;

import java.util.Objects;
import java.util.ArrayList;

public class Player {
    public int playerID;

    public String username;

    public char symbol;

    private int rank;

    public Player(String username, int playerID, char symbol, int rank) {
        this.username = username;
        this.playerID = playerID;
        this.symbol = symbol;
        this.rank = rank;
    }

    public String getName(){
        return username;
    }

    public int getPlayerID(){
        return playerID;
    }

    public char getSymbol() {
            return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {

    }

    public void exitGame() {
    }

}
