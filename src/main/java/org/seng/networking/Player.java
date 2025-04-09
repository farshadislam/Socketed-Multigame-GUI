package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;

/**
 * This class handles player sessions on networking side.
 */

import org.seng.leaderboard.*;   // Assuming Match, GameType, Rank, etc. are part of this package
import java.util.Objects;

import org.seng.networking.SocketGameHandler;

public class Player {
    // Core player fields
    private String username;
    private String email;
    private String password;
    private char symbol;
    private connect4Stats Connect4Stats;
    private ticTacToeStats TicTacToeStats;
    private checkersStats CheckersStats;
    private String verificationCode;

    // Session, match, and game logic fields
    private boolean loggedIn = false;
    private Match currentMatch;
    private SocketGameHandler socketHandler;
    private int playerID;
    private int rank;
    private int wins;
    private int losses;

    /**
     * Constructor that also initialises stats for each game.
     * @param username The player's username.
     * @param email The player's email.
     * @param password The player's password.
     */
    public Player(String username, String email, String password) {
        // Store username and email in lower case
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.password = password;
        // Initialise game-specific stats using the username
        this.Connect4Stats = new connect4Stats(this.username);
        this.CheckersStats = new checkersStats(this.username);
        this.TicTacToeStats = new ticTacToeStats(this.username);
    }

    // === General Getters and Setters ===

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public connect4Stats getConnect4Stats() {
        return Connect4Stats;
    }

    public void setConnect4Stats(connect4Stats connect4Stats) {
        this.Connect4Stats = connect4Stats;
    }

    public ticTacToeStats getTicTacToeStats() {
        return TicTacToeStats;
    }

    public void setTicTacToeStats(ticTacToeStats ticTacToeStats) {
        this.TicTacToeStats = ticTacToeStats;
    }

    public checkersStats getCheckersStats() {
        return CheckersStats;
    }

    public void setCheckersStats(checkersStats checkersStats) {
        this.CheckersStats = checkersStats;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    // === Aggregate Game Statistics ===

    public int getTotalGamesPlayed() {
        return Connect4Stats.getGamesPlayed() +
                TicTacToeStats.getGamesPlayed() +
                CheckersStats.getGamesPlayed();
    }

    public int getTotalWins() {
        return Connect4Stats.get_wins() +
                TicTacToeStats.get_wins() +
                CheckersStats.get_wins();
    }

    public int getTotalLosses() {
        return Connect4Stats.get_losses() +
                TicTacToeStats.get_losses() +
                CheckersStats.get_losses();
    }

    public int getTotalTies() {
        return Connect4Stats.get_ties() +
                TicTacToeStats.get_ties() +
                CheckersStats.get_ties();
    }

    // === Session and Match Methods ===

    /**
     * Validates the password and logs the player in.
     * @param password The password to check.
     * @return True if the password matches and login is successful.
     */
    public boolean login(String password) {
        if (this.password.equals(password)) {
            loggedIn = true;
            System.out.println(username + " logged in.");
            return true;
        }
        System.out.println("Login failed for " + username);
        return false;
    }

    /**
     * Logs out the player and resets current match information.
     */
    public void logout() {
        loggedIn = false;
        currentMatch = null;
        System.out.println(username + " logged out.");
    }

    /**
     * Checks if the player is currently logged in.
     * @return True if logged in, false otherwise.
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Joins a match with an opponent using the specified game type.
     * Creates a new Match instance and assigns it to both players.
     * Also adds the match to the list of available matches.
     * @param opponent The opposing player.
     * @param gameType The game type for the match.
     */
    public void joinMatch(Player opponent, GameType gameType) {
        Match match = new Match(this, opponent, gameType);
        this.currentMatch = match;
        opponent.setCurrentMatch(match);
        Match.addAvailableMatch(match);
        System.out.println("Match joined: " + this.username + " vs " + opponent.getUsername());
    }

    /**
     * Returns the current match this player is in.
     */
    public Match getCurrentMatch() {
        return currentMatch;
    }

    /**
     * Sets the player's current match.
     * @param match The match to set as current.
     */
    public void setCurrentMatch(Match match) {
        this.currentMatch = match;
    }

    /**
     * Checks whether the player is currently in a match that is ready.
     * @return True if in a match and it is ready, false otherwise.
     */
    public boolean isInMatch() {
        return currentMatch != null && currentMatch.isReady();
    }

    /**
     * Allows the player to leave the current match.
     */
    public void leaveMatch() {
        currentMatch = null;
        System.out.println(username + " left the match.");
    }

    /**
     * Force quits the player session by logging out and clearing session details.
     */
    public void disconnect() {
        logout();
        System.out.println(username + " disconnected.");
    }

    // === Networking Methods ===

    /**
     * Sets the player's socket handler for networking.
     * @param handler The SocketGameHandler to set.
     */
    public void setSocketHandler(SocketGameHandler handler) {
        this.socketHandler = handler;
    }

    /**
     * Gets the player's socket handler.
     */
    public SocketGameHandler getSocketHandler() {
        return socketHandler;
    }

    // === Game Logic Methods ===

    /**
     * Returns the player's name.
     */
    public String getName() {
        return username;
    }

    /**
     * Sets the player's name.
     */
    public void setName(String username) {
        setUsername(username);
    }

    /**
     * Returns the player's unique ID.
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Sets the player's unique ID.
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Returns the player's rank.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets the player's rank.
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Returns the number of wins.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns the number of losses.
     */
    public int getLosses() {
        return losses;
    }

    /**
     * Increments the win count for the player.
     */
    public void incrementWins() {
        wins++;
    }

    /**
     * Increments the loss count for the player.
     */
    public void incrementLosses() {
        losses++;
    }

    // === Equality Methods ===

    /**
     * Overrides equals to compare players by their username.
     * @param obj Object to compare.
     * @return True if both players have the same username.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        return username.equals(other.username);
    }

    /**
     * Overrides hashCode to be consistent with equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
