package gamelogic;

import java.util.ArrayList;

public abstract class Player {
    public int playerID;

    public String name;

    public char symbol;

    private int rank;

    public Player(String name, int playerID, char symbol, int rank) {
        this.name = name;
        this.playerID = playerID;
        this.symbol = symbol;
        this.rank = rank;
    }

    public String getName(){
        return name;
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

    public void exitGame() {}

}
