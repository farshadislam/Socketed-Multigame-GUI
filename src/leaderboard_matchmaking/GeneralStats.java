package leaderboard_matchmaking;

public abstract class GeneralStats {
    protected String playerID;
    protected int gamesPlayed;
    protected int wins;
    protected int losses;
    protected int ties;
    protected int mmr;
    protected Rank rank;

    public void stats (String playerID){
        this.playerID = playerID;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.mmr = 0;               // Default starting mmr
        this.rank = Rank.BRONZE;    // Default starting rank
    }

    /*
    Recording game results.
    If win is true, game was won
    If tie is true, game ended in a tie
    Otherwise, it was a loss
     */
    public void recordResult(boolean win, boolean tie){
        gamesPlayed++;
        if (win){
            wins++;
            updateMMR(true);
        }
        else if (tie) {
            ties++;
            updateMMRTies();
        }
        updateRank();
    }

    // Abstract class for updating player's rank based on current mmr
    protected void updateRank() {
        if (mmr <= 28){
            rank = Rank.BRONZE;
        }
        else if (29 <= mmr && mmr <= 57){
            rank = Rank.SILVER;
        }
        else if (58 <= mmr && mmr <= 85){
            rank = Rank.GOLD;
        }
        else if (86 <= mmr && mmr <= 114){
            rank = Rank.PLATINUM;
        }
        else if (115 <= mmr && mmr <= 142){
            rank = Rank.DIAMOND;
        }
        else if (143 <= mmr && mmr <= 171){
            rank = rank.MASTER;
        }
        else if (172 <= mmr && mmr <= 200){
            rank = rank.GRANDMASTER;
        }
    }

    // Default MMR update for a tie.
    protected void updateMMRTies() {
        // No change in mmr on a tie
    }

    // Abstract method for updating MMR based on game result
    protected void updateMMR(boolean win) {
    }
}

