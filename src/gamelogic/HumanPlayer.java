package gamelogic;

public class HumanPlayer extends Player {
    private int playerRank;
    private char symbol;

//constructor for HumanPlayer
    public HumanPlayer(String name, int accountID, int level, int plays, int score, int playerRank, char symbol) {
        super(name, accountID, level, plays, score);
        this.playerRank = playerRank;
        this.symbol = symbol;
    }

    public int getRank() {
        //gets player rank
        return playerRank;
    }

    public boolean readyStart() {
        // check if all conditions are met this should be condition part
        return true;
    }

    @Override
    public char getSymbol() {
        //gets the symbol such as X and O for tic tac toe, likely?
        return symbol;
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}