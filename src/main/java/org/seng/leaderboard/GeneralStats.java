package org.seng.leaderboard;

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

    public GeneralStats(String playerID) {
        this.playerID = playerID;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.rank = Rank.BRONZE;    // Default starting rank.
    }

    public abstract int getMMR();

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

    protected void updateRank() {
    }

    protected void updateMMRTies() {
    }

    protected abstract void updateMMR(boolean win);

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


