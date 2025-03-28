package leaderboard_matchmaking;

public abstract class generalStats {
    protected String playerId;
    protected int gamesPlayed;
    protected int wins;
    protected int loss;
    protected int ties;
    protected int mmr;
    protected Rank rank;

    public generalStats(String playerId) {
        this.playerId = playerId;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.loss = 0;
        this.ties = 0;
        this.mmr = 0;
        this.rank = Rank.BRONZE;
    }

    public void updateStats(boolean win, boolean tie) {
        gamesPlayed++;
        if (win) { wins++;}
        else if (tie) { ties++;}
        else { loss++;}
        // update mmr too
    }

    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLoss() { return loss; }
    public int getTies() { return ties; }
    public Rank getRank() { return rank; }


    //toString, change to whatever format needed
    @Override
    public String toString() {
        return String.format("Games: %d, Wins: %d, Losses: %d, Ties: %d, Rank: %s",
                gamesPlayed, wins, loss, ties, rank);
    }
}
