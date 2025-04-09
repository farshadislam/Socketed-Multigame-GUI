package org.seng.authentication;
import org.seng.leaderboard_matchmaking.*;
import org.seng.leaderboard_matchmaking.Rank;

//import org.seng.leaderboard.matchmaking.Last5Matches;
import org.seng.leaderboard_matchmaking.GameType;
import org.seng.leaderboard_matchmaking.checkersStats;
import org.seng.leaderboard_matchmaking.connect4Stats;
import org.seng.leaderboard_matchmaking.ticTacToeStats;


import java.util.Objects;

public class Player {
    // create fields for all the methods
    private String username;
    private String email;
    private String password;
    private char symbol;
    private connect4Stats Connect4Stats;
    private ticTacToeStats TicTacToeStats;
    private checkersStats CheckersStats;
    private String verificationCode;

//    //last 5 matches field
//    private Last5Matches last5Matches;

    /**
     * Constructor that also sets initial wins for each game.
     * The 'rank' parameter is applied to each game’s rank as a default.
     * @param username username of the player
     * @param email email of the player
     * @param password password of the player
     */
    public Player(String username, String email, String password) {
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.password = password;
        this.Connect4Stats = new connect4Stats(username);
        this.CheckersStats = new checkersStats(username);
        this.TicTacToeStats = new ticTacToeStats(username);
        //initializing the constructor
//        this.last5Matches = new Last5Matches();
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
        return this.username.toLowerCase();
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getEmail() {
        return this.email.toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }


//    Adding totals from playerStats, since we are planning to remove it

    public int getTotalGamesPlayed() {
        connect4Stats Connect4Stats = this.Connect4Stats;
        ticTacToeStats TicTacToeStats = this.TicTacToeStats;
        checkersStats CheckersStats = this.CheckersStats;
        return Connect4Stats.getGamesPlayed() + TicTacToeStats.getGamesPlayed() + CheckersStats.getGamesPlayed();
    }

    public int getTotalWins() {
        connect4Stats Connect4Stats = this.Connect4Stats;
        ticTacToeStats TicTacToeStats = this.TicTacToeStats;
        checkersStats CheckersStats = this.CheckersStats;
        return Connect4Stats.get_wins() + TicTacToeStats.get_wins() + CheckersStats.get_wins();
    }

    public int getTotalLosses() {
        connect4Stats Connect4Stats = this.Connect4Stats;
        ticTacToeStats TicTacToeStats = this.TicTacToeStats;
        checkersStats CheckersStats = this.CheckersStats;
        return Connect4Stats.get_losses() + TicTacToeStats.get_losses() + CheckersStats.get_losses();
    }

    public int getTotalTies() {
        connect4Stats Connect4Stats = this.Connect4Stats;
        ticTacToeStats TicTacToeStats = this.TicTacToeStats;
        checkersStats CheckersStats = this.CheckersStats;
        return Connect4Stats.get_ties() + TicTacToeStats.get_ties() + CheckersStats.get_ties();
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
        return Objects.equals(username, player.username) && Objects.equals(email, player.email);
    }

//    public Last5Matches getLast5MatchesObject(){
//        return this.last5Matches;
//    }
//
//    public void updateLast5Matches(GameType gameType, Player player) {
//        last5Matches.update(gameType, player);
//    }
//
//    public List<List<Object>> getLast5Matches() {
//        return last5Matches.getMatchHistory();
//    }
//
//    public List<Object> getLastMatchAt(int index){
//        return last5Matches.getLastMatchAt(index);
//    }
//
//    public void clearLast5Matches(){
//        last5Matches.clearHistory();
//    }

}

