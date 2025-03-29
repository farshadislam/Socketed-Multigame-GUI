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
    private connect4Stats Connect4Stats;
    private ticTacToeStats TicTacToeStats;
    private checkersStats CheckersStats;

    // create constructor
    public Player(String username) {
        this.username = username;
        this.Connect4Stats = new connect4Stats(username);
        this.TicTacToeStats = new ticTacToeStats(username);
        this.CheckersStats = new checkersStats(username);
    }

    public boolean updateEmail(String username, String newEmail) {
        if (this.username.equals(username)){
            if (!(newEmail==null) && !newEmail.isEmpty()){
                this.email = newEmail;
                return true;
            }
        }
        return false;
    }
    public boolean updatePassword(String username, String newPassword) {
        if (this.username.equals(username)){
            if (!(newPassword==null) && !newPassword.isEmpty()) {
                this.password = newPassword;
                return true;
            }
        }
        return false;
    }
    public boolean updateUsername(String username, String newUsername) {
        if (this.username.equals(username)){
            if (!(newUsername==null) && !newUsername.isEmpty()) {
                this.username = newUsername;
                return true;
            }
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
        return switch (gameType.toLowerCase()) {
            case "connect4" -> Connect4Stats;
            case "tictactoe" -> TicTacToeStats;
            case "checkers" -> CheckersStats;
            default -> null;
        };

    }

    @Override
    public boolean equals(Object object) {
        if (this == object){
            return true;
            // if both references point to the same object (itself), true
        }
        if (object == null || getClass() != object.getClass()){
            return false;
            // if one object is null or a different class (not a player), false
        }
        Player player = (Player) object;
        // cast object into Player object and compare
        return Objects.equals(username, player.username);
    }
}
