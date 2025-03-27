public class Player {


    public String username;
    public String email;
    public String password;
    public Ranking rank;
    public connect4Stats Connect4Stats;
    public ticTacToeStats TicTacToeStats;
    public checkerStats CheckerStats;

    public Player(String username) {
        this.username = username;
    }

    public void updateEmail(String username, String email) {
        //
    }

    public void updatePassword(String username, String password) {
        //
    }
}
