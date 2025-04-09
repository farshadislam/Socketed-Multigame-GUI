package org.seng.authentication;

import org.seng.leaderboard_matchmaking.*;
import org.seng.networking.Match;
import org.seng.networking.SocketGameHandler;
import java.util.Objects;
import org.seng.networking.leaderboard_matchmaking.GameType;

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
    private Last5Matches last5Matches;

    // Networking/session/matchmaking fields
    private boolean loggedIn = false;
    private Match currentMatch;
    private SocketGameHandler socketHandler;
    private int playerID;
    private int rank;
    private int wins;
    private int losses;

    public Player(String username, String email, String password) {
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.password = password;
        this.Connect4Stats = new connect4Stats(username);
        this.CheckersStats = new checkersStats(username);
        this.TicTacToeStats = new ticTacToeStats(username);
        this.last5Matches = new Last5Matches();
    }

    // === General Getters and Setters ===

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

    public String getUsername() {
        return username.toLowerCase();
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getEmail() {
        return email.toLowerCase();
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Last5Matches getLast5MatchesObject() {
        return this.last5Matches;
    }

    // === Aggregated Stats ===

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

    // === Login/Session ===

    public boolean login(String password) {
        if (this.password.equals(password)) {
            loggedIn = true;
            System.out.println(username + " logged in.");
            return true;
        }
        System.out.println("Login failed for " + username);
        return false;
    }

    public void logout() {
        loggedIn = false;
        currentMatch = null;
        System.out.println(username + " logged out.");
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void disconnect() {
        logout();
        System.out.println(username + " disconnected.");
    }

    // === Matchmaking ===

    public void joinMatch(Player opponent, GameType gameType) {
        Match match = new Match(this, opponent, gameType);
        this.currentMatch = match;
        opponent.setCurrentMatch(match);
        Match.addAvailableMatch(match);
        System.out.println("Match joined: " + this.username + " vs " + opponent.getUsername());
    }

    public void leaveMatch() {
        currentMatch = null;
        System.out.println(username + " left the match.");
    }

    public boolean isInMatch() {
        return currentMatch != null && currentMatch.isReady();
    }

    public Match getCurrentMatch() {
        return currentMatch;
    }

    public void setCurrentMatch(Match match) {
        this.currentMatch = match;
    }

    // === Networking ===

    public void setSocketHandler(SocketGameHandler handler) {
        this.socketHandler = handler;
    }

    public SocketGameHandler getSocketHandler() {
        return socketHandler;
    }

    // === Game Logic Utilities ===

    public String getName() {
        return username;
    }

    public void setName(String username) {
        setUsername(username);
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void incrementWins() {
        wins++;
    }

    public void incrementLosses() {
        losses++;
    }

    // === Equality Check ===

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Player player = (Player) object;
        return Objects.equals(username, player.username) &&
                Objects.equals(email, player.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
