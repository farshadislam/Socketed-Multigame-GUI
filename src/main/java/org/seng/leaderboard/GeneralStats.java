package org.seng.leaderboard;


/**
 * An abstract base class that holds universal game statistics for a player.
 * It includes fields for player identifier, games played, wins, losses, ties,
 * and separate MMR values for different games (Connect4, Checkers, TicTacToe),
 * as well as the player's overall rank.
 */
public abstract class GeneralStats {

    // Unique identifier for the player.
    protected String playerID;

    // Counters for games played, wins, losses, and ties.
    protected int gamesPlayed;
    protected int wins;
    protected int losses;
    protected int ties;

    // MMR values for specific games.
    protected int connect4mmr;
    protected int checkersmmr;
    protected int tictactoemmr;

    // The player's current rank.
    protected Rank rank;

    /**
     * Constructs a new GeneralStats object with initial values.
     *
     * @param playerID The unique identifier for the player.
     */
    public GeneralStats(String playerID) {
        this.playerID = playerID;
        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;
        this.ties = 0;
        this.rank = Rank.BRONZE;    // Default starting rank.
    }

    /**
     * Records a win for the player. Increments wins and games played,
     * then updates the MMR and rank accordingly.
     */
    public void win() {
        wins++;
        gamesPlayed++;
        updateMMR(true);
        updateRank();
    }

    /**
     * Records a loss for the player. Increments losses and games played,
     * then updates the MMR and rank accordingly.
     */
    public void lose() {
        losses++;
        gamesPlayed++;
        updateMMR(false);
        updateRank();
    }

    /**
     * Records a tie for the player. Increments ties and games played,
     * then updates the MMR (if applicable) and rank accordingly.
     */
    public void tie() {
        ties++;
        gamesPlayed++;
        updateMMRTies();
        updateRank();
    }

    /**
     * Abstract method to retrieve the current MMR for the player.
     * This should return the game-specific MMR value defined by subclasses.
     *
     * @return The player's current MMR.
     */
    public abstract int getMMR();

    /**
     * Retrieves the current rank of the player.
     *
     * @return The player's current Rank.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Updates the player's rank based on the current MMR.
     * This method is empty by default and can be overridden in subclasses to implement
     * game-specific ranking logic.
     */
    protected void updateRank() {
    }

    /**
     * Updates the player's MMR in the case of a tie.
     * This method is empty by default and can be overridden in subclasses if tie results
     * should affect MMR.
     */
    protected void updateMMRTies() {
    }

    /**
     * Abstract method for updating the player's MMR based on a game result.
     * Subclasses must provide their own implementation for how MMR is updated
     * when a game is won or lost.
     *
     * @param win true if the game was won; false otherwise.
     */
    protected abstract void updateMMR(boolean win);

    /**
     * Returns the total number of wins.
     *
     * @return The win count.
     */
    public int get_wins() {
        return wins;
    }

    /**
     * Returns the total number of losses.
     *
     * @return The loss count.
     */
    public int get_losses() {
        return losses;
    }

    /**
     * Returns the total number of ties.
     *
     * @return The tie count.
     */
    public int get_ties() {
        return ties;
    }

    /**
     * Returns the player's unique identifier.
     *
     * @return The playerID.
     */
    public String getPlayerId() {
        return playerID;
    }

    /**
     * Returns the total number of games played.
     *
     * @return The number of games played.
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * Records the result of a game. Depending on the outcome (win, tie, or loss),
     * it increments the corresponding counter and updates MMR and rank.
     *
     * @param win true if the game was won; false otherwise.
     * @param tie true if the game ended in a tie.
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

    /**
     * Returns a string representation of the player's general statistics.
     *
     * @return A string containing playerID, games played, wins, losses, ties, and rank.
     */
    @Override
    public String toString() {
        return "GeneralStats [playerID=" + playerID + ", gamesPlayed=" + gamesPlayed +
                ", wins=" + wins + ", losses=" + losses + ", ties=" + ties +
                ", rank=" + rank + "]";
    }

    // Methods added by the Authentication Team

    public void setGamesPlayed(int num_of_gamesPlayed) {
        this.gamesPlayed = num_of_gamesPlayed;
    }

    public void setWins(int num_of_wins) {
        this.wins = num_of_wins;
    }

    public void setLosses(int num_of_losses) {
        this.losses = num_of_losses;
    }

    public void setTies(int num_of_ties) {
        this.ties = num_of_ties;
    }

    public void setRank(Rank player_rank) {
        this.rank = player_rank;
    }

    public abstract void setMMR (int game_specific_MMR);

}


