package gamelogic.connectfour;
import gamelogic.HumanPlayer;

public class ConnectFourPlayer extends HumanPlayer {
    private int playerRank; // may be final?
    private char symbol;

    // constructor
    public ConnectFourPlayer(String name, int accountID, int level, int plays, int score, int playerRank, char symbol) {
        super(name, accountID, level, plays, score, playerRank, symbol);
        this.playerRank = playerRank;
        this.symbol = symbol; // connect four chip (piece)
    }

    // return player's rank
    public int getRank() {
        return playerRank;
    }

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
    {

    }
}
