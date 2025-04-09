//package org.seng.networking;
//
//import org.seng.networking.leaderboard_matchmaking.GameType;
//
///**
// * This class handles player sessions on networking side.
// */
//public class Player {
//
//    // === Networking Fields ===
//    private String username;         // player username (used everywhere)
//    private String password;         // basic password check for login
//    private boolean loggedIn;        // true if player is logged in rn
//    private Match currentMatch;      // match they’re currently in (if any)
//
//    private transient SocketGameHandler socketHandler; // used to send/receive data over sockets
//
//    // === Game Logic Fields ===
//    private int playerID;            // unique identifier
//    private char symbol;             // player’s symbol (e.g. X or O)
//    private int rank = 1000;         // default starting ELO
//    private int wins = 0;            // total wins
//    private int losses = 0;          // total losses
//
//    // === Networking Constructor ===
//    public Player(String username, String password) {
//        this.username = username;
//        this.password = password;
//        this.loggedIn = false;
//        this.currentMatch = null;
//    }
//
//    // === Networking Methods ===
//
//    /**
//     * This logs the player in if pw matches.
//     * @param password input pw
//     * @return true if pw correct, else false
//     */
//    public boolean login(String password) {
//        if (this.password.equals(password)) {
//            loggedIn = true;
//            System.out.println(username + " logged in.");
//            return true;
//        }
//        System.out.println("Login failed for " + username);
//        return false;
//    }
//
//    /**
//     * This logs the player out + resets match info.
//     */
//    public void logout() {
//        loggedIn = false;
//        currentMatch = null;
//        System.out.println(username + " logged out.");
//    }
//
//    /**
//     * This returns whether the player is logged in.
//     */
//    public boolean isLoggedIn() {
//        return loggedIn;
//    }
//
//    /**
//     * This connects this player to another in a match.
//     * Makes match object + stores it for both players.
//     */
//    public void joinMatch(Player opponent, GameType gameType) {
//        Match match = new Match(this, opponent, gameType);
//        this.currentMatch = match;
//        opponent.setCurrentMatch(match);
//        Match.addAvailableMatch(match);
//        System.out.println("Match joined: " + this.username + " vs " + opponent.getUsername());
//    }
//
//    /**
//     * This returns the match player is in rn.
//     */
//    public Match getCurrentMatch() {
//        return currentMatch;
//    }
//
//    /**
//     * This sets the player's current match.
//     */
//    public void setCurrentMatch(Match match) {
//        this.currentMatch = match;
//    }
//
//    /**
//     * This returns the player’s username.
//     */
//    public String getUsername() {
//        return username;
//    }
//
//    /**
//     * This checks if player is in a match + it’s ready.
//     */
//    public boolean isInMatch() {
//        return currentMatch != null && currentMatch.isReady();
//    }
//
//    /**
//     * This lets the player leave their match.
//     */
//    public void leaveMatch() {
//        currentMatch = null;
//        System.out.println(username + " left the match.");
//    }
//
//    /**
//     * This is like force quit — logs out + clears everything.
//     */
//    public void disconnect() {
//        logout();
//        System.out.println(username + " disconnected.");
//    }
//
//    /**
//     * This compares two players by username.
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof Player other) {
//            return this.username.equals(other.username);
//        }
//        return false;
//    }
//
//    /**
//     * This sets the socket handler (used in server networking).
//     */
//    public void setSocketHandler(SocketGameHandler handler) {
//        this.socketHandler = handler;
//    }
//
//    /**
//     * This gets the socket handler (used in server networking).
//     */
//    public SocketGameHandler getSocketHandler() {
//        return socketHandler;
//    }
//
//    // === Game Logic Methods ===
//
//    public String getName() {
//        return username;
//    }
//
//    public void setName(String username) {
//        this.username = username;
//    }
//
//    public int getPlayerID() {
//        return playerID;
//    }
//
//    public void setPlayerID(int playerID) {
//        this.playerID = playerID;
//    }
//
//    public char getSymbol() {
//        return symbol;
//    }
//
//    public void setSymbol(char symbol) {
//        this.symbol = symbol;
//    }
//
//    public int getRank() {
//        return rank;
//    }
//
//    public void setRank(int rank) {
//        this.rank = rank;
//    }
//
//    public int getWins() {
//        return wins;
//    }
//
//    public int getLosses() {
//        return losses;
//    }
//
//    public void incrementWins() {
//        wins++;
//    }
//
//    public void incrementLosses() {
//        losses++;
//    }
//
//    public void exitGame() {
//        System.out.println(username + " has left the game.");
//    }
//}
