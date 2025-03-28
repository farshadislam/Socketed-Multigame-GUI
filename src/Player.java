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
        this.rank = Rank.BRONZE;
        this.ticTacToeStats = new TicTacToeStats();
        this.checkerStats = new CheckerStats();
    }

    public void updateEmail(String username, String email) {
        //
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
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


    public void updatePassword(String username, String password) {
        //
    }

    public GeneralStats getStats(String gameType) {

        return null;
    }
}
