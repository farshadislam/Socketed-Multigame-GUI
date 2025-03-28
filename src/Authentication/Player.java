package Authentication;


import leaderboard_matchmaking.GeneralStats;
import leaderboard_matchmaking.checkersStats;
import leaderboard_matchmaking.connect4Stats;
import leaderboard_matchmaking.ticTacToeStats;

import java.util.Objects;

public class Player {
    // create fields
    private String username;
    private String email;
    private String password;
    private Rank rank;
    public connect4Stats Connect4Stats;
    public ticTacToeStats TicTacToeStats;
    public checkersStats CheckersStats;

    // create constructor
    public Player(String username) {
        this.username = username;
        this.Connect4Stats = new connect4Stats();
        this.TicTacToeStats = new ticTacToeStats();
        this.CheckersStats = new checkersStats();
    }

    public boolean updateEmail(String username, String newEmail) {
        if (this.username.equals(username)){
            this.email = newEmail;
            return true;
        }
        return false;
    }
    public boolean updatePassword(String username, String newPassword) {
        if (this.username.equals(username)){
            this.password = newPassword;
            return true;
        }
        return false;
    }
    public boolean updateUsername(String username, String newUsername) {
        if (this.username.equals(username)){
            this.username = newUsername;
            return true;
        }
        return false;
    }

    public connect4Stats getConnect4Stats() {
        return Connect4Stats;
    }

    public void setConnect4Stats(connect4Stats connect4Stats) {
        Connect4Stats = connect4Stats;
    }

    public ticTacToeStats getTicTacToeStats() {
        return TicTacToeStats;
    }

    public void setTicTacToeStats(ticTacToeStats ticTacToeStats) {
        TicTacToeStats = ticTacToeStats;
    }

    public checkersStats getCheckersStats() {
        return CheckersStats;
    }

    public void setCheckersStats(checkersStats checkersStats) {
        CheckersStats = checkersStats;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public GeneralStats getStats(String gameType) {
        switch (gameType.toLowerCase()){
            case "connect4":
                return connect4Stats;
            case  "tictactoe":
                return ticTacToeStats;
            case "checkers":
                return checkersStats;
        }

        return null;
    }

    @Override
    // compare the usernames instead of the reference


    @Override
    public boolean equals(Object obj) {
        if (this==obj){
            return true;
            // if both reference point to the same object, automatically true
        }
        Player player = (Player)obj;
        // cast obj into Player object
        return Objects.equals(username, player.username);
    }
}
