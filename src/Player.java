public class Player {
    // create fields
    private String username;
    private String email;
    private String password;
    private Rank rank;
    public Connect4Stats connect4Stats;
    public TicTacToeStats ticTacToeStats;
    public CheckerStats checkerStats;

    // create constructor
    public Player(String username) {
        this.username = username;
        this.connect4Stats = new Connect4Stats();
        this.ticTacToeStats = new TicTacToeStats();
        this.checkerStats = new CheckerStats();
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

    public Connect4Stats getConnect4Stats() {
        return connect4Stats;
    }

    public void setConnect4Stats(Connect4Stats connect4Stats) {
        this.connect4Stats = connect4Stats;
    }

    public TicTacToeStats getTicTacToeStats() {
        return ticTacToeStats;
    }

    public void setTicTacToeStats(TicTacToeStats ticTacToeStats) {
        this.ticTacToeStats = ticTacToeStats;
    }

    public CheckerStats getCheckerStats() {
        return checkerStats;
    }

    public void setCheckerStats(CheckerStats checkerStats) {
        this.checkerStats = checkerStats;
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
                return checkerStats;
        }

        return null;
    }

    @Override
    // compare the usernames instead of the reference


    @Override
    public boolean equals(Object obj) {
        Player player = (Player)obj;
        return Object.equals(username, player.username);
    }
}
