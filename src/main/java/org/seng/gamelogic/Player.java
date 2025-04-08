package org.seng.gamelogic;


import java.io.Serializable;        //for saving and loading
import java.util.Objects;
import java.util.ArrayList;
import java.io.Serial;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public int playerID;

    public String username;

    public char symbol;

    private int rank;
    private int wins;
    private int losses;

    public Player(String username, int playerID, char symbol, int rank) {
        this.username = username;
        this.playerID = playerID;
        this.symbol = symbol;
        this.rank = rank;
        this.wins = 0;
        this.losses = 0;
    }

    public String getName(){
        return username;
    }
    public void setName(String username){
        this.username = username;
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
        this.rank = rank;

    }
    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
    public void incrementWins() {
        wins++;
    }
    public void incrementLosses() {
        losses++;
    }

    public void exitGame() {


        System.out.println(username + " has left the game.");
    }

}

// should we add overrides here?

