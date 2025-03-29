package gamelogic;

public class HumanPlayer {
    private int playerRank;

    public HumanPlayer(int playerRank) {
        this.playerRank = playerRank;
    }

    public int getRank() {
        return playerRank;
    }

    public boolean readyStart() {

        return true;
    }
}