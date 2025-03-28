public class Player {
    public int playerID;
    public String username;
    public String email;
    public String password;
    public Rank rank;
    public Connect4Stats connect4Stats;
    public TicTacToeStats ticTacToeStats;
    public CheckerStats checkerStats;

    public Player(String username) {
        this.username = username;
        this.connect4Stats = new Connect4Stats();
        this.ticTacToeStats = new TicTacToeStats();
        this.checkerStats = new CheckerStats();
    }

    public void updateEmail(String username, String email) {
        //
    }

    public void updatePassword(String username, String password) {
        //
    }

    public GeneralStats getStats(String gameType) {

        return null;
    }
}
