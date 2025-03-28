public class Player {
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

    public boolean updateEmail(String username, String newEmail) {
        if (this.username.equals(username)){
            this.email = newEmail;
            return true;
        }
        return false;
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


    public boolean updatePassword(String username, String newPassword) {
        if (this.username.equals(username)){
            this.email = newPassword;
            return true;
        }
        return false;
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

}
