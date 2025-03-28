package gamelogic;

import java.util.ArrayList;

public abstract class Player {
    private int accountID;
    public String name;
    public int gameLevel;
    public int plays;
    public int score;
    public ArrayList<GamePiece> hand;
    public ArrayList<GamePiece> spoils;


    public Player(String name, int accountID, int level, int plays, int score) {
        this.name = name;
        this.accountID = accountID;
        this.gameLevel = 0;
        this.plays = 0;
        this.score = 0;
        this.hand = new ArrayList<>();
        this.spoils = new ArrayList<>();
    }

public String getName(){
    return name;
}
public int getAccountID(){
    return accountID;
}
public int getGameLevel(){
    return gameLevel;
}
public int getPlays(){
    return plays;
}
public int getScore(){
    return score;
}
public ArrayList<GamePiece> getSpoils() {
        return spoils;
}
public ArrayList<GamePiece> getHand() {
        return hand;
}


}
