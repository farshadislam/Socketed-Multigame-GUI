package org.seng.authentication;
import org.seng.leaderboard.*;
import org.seng.leaderboard.Rank;

import java.util.Objects;

public class Player {
    // create fields for all the methods
    private String username;
    private String email;
    private String password;
    private Rank rank;
    private char symbol;
    private connect4Stats Connect4Stats;
    private ticTacToeStats TicTacToeStats;
    private checkersStats CheckersStats;
    private String verificationCode;

    /**
     * Constructor that also sets initial wins for each game.
     * The 'rank' parameter is applied to each game’s rank as a default.
     * @param username username of the player
     * @param email email of the player
     * @param password password of the player
     */
    public Player(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;

        this.rank = Rank.BRONZE; //initializing the global rank Bronze
        this.Connect4Stats = new connect4Stats(username);
        this.CheckersStats = new checkersStats(username);
        this.TicTacToeStats = new ticTacToeStats(username);
    }

    /**
     * method for updating the email
     * @param username username of the player
     * @param newEmail new email they want to use
     * @return true if email is updated, false otherwise
     */
    public boolean updateEmail(String username, String newEmail) {
        if (this.username.equals(username)) {
            if (!(newEmail == null) && !newEmail.isEmpty()) {
                this.email = newEmail;
                return true;
            }
        }
        return false;
    }

    /**
     * method for updating the password
     * @param username username for the player
     * @param newPassword new password they want to use
     * @return true if password is updated, false otherwise
     */
    public boolean updatePassword(String username, String newPassword) {
        if (this.username.equals(username)) {
            if (!(newPassword == null) && !newPassword.isEmpty()) {
                this.password = newPassword;
                return true;
            }
        }
        return false;
    }

    /**
     * method for updating the username
     * @param username username for the player
     * @param newUsername new username they want to use
     * @return true if username is updated, false otherwise
     */
    public boolean updateUsername(String username, String newUsername) {
        if (this.username.equals(username)) {
            if (!(newUsername == null) && !newUsername.isEmpty()) {
                this.username = newUsername;
                return true;
            }
        }
        return false;
    }


    // setters and getters
    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public connect4Stats getConnect4Stats() {
        return this.Connect4Stats;
    }

    public void setConnect4Stats(connect4Stats connect4Stats) {
        this.Connect4Stats = connect4Stats;
    }

    public ticTacToeStats getTicTacToeStats() {
        return this.TicTacToeStats;
    }

    public void setTicTacToeStats(ticTacToeStats ticTacToeStats) {
        this.TicTacToeStats = ticTacToeStats;
    }

    public checkersStats getCheckersStats() {
        return this.CheckersStats;
    }

    public void setCheckersStats(checkersStats checkersStats) {
        this.CheckersStats = checkersStats;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the rank from that game’s stats
     * @param gameType the type of game
     * @return the rank
     */
    public Rank getRank(GameType gameType) {
        GeneralStats stats = getStats(gameType);
        return (stats != null) ? stats.getRank() : Rank.BRONZE;
    }

    public void setRank(Rank rank) {
        this.rank = rank; // If you keep a “global” rank
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }

    /**
     * method for getting the stats
     * @param gameType the type of game
     * @return the stats for the game
     */
    public GeneralStats getStats(GameType gameType) {
        return switch (gameType.name().toLowerCase()) {
            case "connect4" -> this.Connect4Stats;
            case "tictactoe" -> this.TicTacToeStats;
            case "checkers" -> this.CheckersStats;
            default -> null;
        };
    }


    /**
     * override method for equals
     * @param object the object being used for comparing
     * @return true if its equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
            // if both references point to the same object (itself), true
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
            // if one object is null or a different class (not a player), false
        }
        Player player = (Player) object;
        // cast object into Player object and compare
        return Objects.equals(username, player.username);
    }
}

