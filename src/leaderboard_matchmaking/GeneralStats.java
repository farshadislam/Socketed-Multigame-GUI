package leaderboard_matchmaking;

public abstract class GeneralStats {
    protected String playerID;
    protected int gamesPlayed;
    protected int wins;
    protected int losses;
    protected int ties;
    protected int connect4mmr;
    protected int checkersmmr;
    protected int tictactoemmr;
    protected Rank rank;

    // Constructor: initializes the fields.
    public GeneralStats(String playerID) {
        this.playerID = playerID;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.rank = Rank.BRONZE;    // Default starting rank.
    }

    /*
     * Recording game results.
     * If win is true, the game was won.
     * If tie is true, the game ended in a tie.
     * Otherwise, it was a loss.
     */
    public void recordResult(boolean win, boolean tie) {
        gamesPlayed++;
        if (win) {
            wins++;
            updateMMR(true);
        } else if (tie) {
            ties++;
            updateMMRTies();
        } else {
            losses++;
            updateMMR(false);
        }
        updateRank();
    }

    // Updates the player's rank based on the current MMR.
    protected void updateRank() {
    }

    // Default MMR update for a tie; child classes can override if needed.
    protected void updateMMRTies() {
        // Default: no change in MMR on a tie.
    }

    // Abstract method for updating MMR based on game result.
    protected abstract void updateMMR(boolean win);

    // Getter methods.
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public int getTies() { return ties; }
    public Rank getRank() { return rank; }

    @Override
    public String toString() {
        return "GeneralStats [playerID=" + playerID + ", gamesPlayed=" + gamesPlayed +
                ", wins=" + wins + ", losses=" + losses + ", ties=" + ties +
                ", rank=" + rank + "]";
    }
}


