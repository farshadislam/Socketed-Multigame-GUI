package leaderboard_matchmaking;

public class connect4Stats {
    private String playerId;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int ties;
    private Rank rank;

    public connect4Stats(String playerId) {
        this.playerId = playerId;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.rank = Rank.BRONZE;
    }

    public void updateStats(boolean win, boolean tie) {
        gamesPlayed++;
        if (win) wins++;
        else if (tie) ties++;
        else losses++;
        updateRank();
    }

    private void updateRank() {
        // mmr logic
    }

    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getTies() { return ties; }
    public Rank getRank() { return rank; }

    @Override
    public String toString() {
        return "Connect4Stats -> Games: " + gamesPlayed + ", Wins: " + wins + ", Losses: " + losses + ", Ties: " + ties + ", Rank: " + rank;
    }
}
