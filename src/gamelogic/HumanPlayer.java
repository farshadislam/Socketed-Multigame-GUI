package gamelogic;
public class HumanPlayer extends Player {
    private int playerRank;
    private char symbol;

    public HumanPlayer(String name, int accountID, int level, int plays, int score, int playerRank, char symbol) {
        super(name, accountID, level, plays, score);
        this.playerRank = playerRank;
        this.symbol = symbol;
    }

    public int getRank() {
        return playerRank;
    }

    public boolean readyStart() {
        // Implement logic here
        return true; // Placeholder return value
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}