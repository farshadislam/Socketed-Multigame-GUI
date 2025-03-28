package leaderboard_matchmaking;

public class checkersStats {
    private String playerId;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private int ties;
    private int mmr;
    private Rank rank;

    public checkersStats(String playerId) {
        this.playerId = playerId;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.mmr = 0;
        this.rank = Rank.BRONZE;
    }

    public void updateStats(boolean win, boolean tie) {
        gamesPlayed++;
        if (win) {
            wins++;
            // mmr gain logic
        }
        else if (tie) {
            ties++;
            // mmr tie logic
        }
        else {
            losses++;
            // mmr lose logic
        }
        updateRank();
    }

    private void updateRank() {
        // mmr thresholds to update rank
        // (if mmr > x) rank = Rank.x
    }

    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getTies() { return ties; }
    public int getMMR() { return mmr; }
    public Rank getRank() { return rank; }

    @Override
    public String toString() {
        return "CheckersStats -> Games: " + gamesPlayed + ", Wins: " + wins +
                ", Losses: " + losses + ", Ties: " + ties +
                ", MMR: " + mmr + ", Rank: " + rank;
    }
}
