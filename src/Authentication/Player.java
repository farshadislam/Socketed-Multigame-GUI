package Authentication;
import leaderboard_matchmaking.GeneralStats;
import leaderboard_matchmaking.checkersStats;
import leaderboard_matchmaking.connect4Stats;
import leaderboard_matchmaking.ticTacToeStats;
import leaderboard_matchmaking.Rank;
import leaderboard_matchmaking.Leaderboard;
import java.util.Objects;

public class Player {

    // create fields for all the methods
    private String username;
    private String email;
    private String password;
    private Rank rank;
    private connect4Stats Connect4Stats;
    private ticTacToeStats TicTacToeStats;
    private checkersStats CheckersStats;

    /**
     * constructor for player class
     * @param username username of the player
     */
    public Player(String username) {
        this.username = username;
        this.rank = Rank.BRONZE; // default rank
        this.Connect4Stats = new connect4Stats(username);
        this.TicTacToeStats = new ticTacToeStats(username);
        this.CheckersStats = new checkersStats(username);
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
     *  method for updating the password
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

    /**
     * method for getting the rank
     * @param game the type of game
     * @return the rank
     */
    public Rank getRank(GameType game) {
        GeneralStats stats = getStats(game.name().toLowerCase());
        return (stats != null) ? stats.getRank() : Rank.BRONZE;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    /**
     * method for getting the stats
     * @param gameType the type of game
     * @return the stats for the game
     */
    public GeneralStats getStats(String gameType) {
        return switch (gameType.toLowerCase()) {
            // change
            case "connect4" -> Connect4Stats;
            case "tictactoe" -> TicTacToeStats;
            case "checkers" -> CheckersStats;
            default -> null;
        };

    }

    /**
     * method for getting the total wins
     * @return the total wins of all 3 games
     */
    public int getTotalWins(){
        return Connect4Stats.getWins() + TicTacToeStats.getWins() + CheckersStats.getWins();
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
