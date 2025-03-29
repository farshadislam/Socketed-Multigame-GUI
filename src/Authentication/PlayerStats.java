package Authentication;
import leaderboard_matchmaking.Rank;


public class PlayerStats {
    // defined instance variable
    private Player player;

    // created the constructor to initialize the player in the PlayerStats class
    public PlayerStats(Player player) {
        this.player = player;
    }

    // created Methods for Connect4 Game
    public int getWinsForConnect4(){
        return player.getStats("Connect4").getWins();
    }
    public int getLossesForConnect4() {
        return player.getStats("Connect4").getLosses();
    }
    public int getTiesForConnect4() {
        return player.getStats("Connect4").getTies();
    }
    public Rank getRankForConnect4() {
        return player.getStats("Connect4").getRank();
    }
}
