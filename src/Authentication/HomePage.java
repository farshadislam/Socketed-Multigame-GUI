package Authentication;

public class HomePage {
    // defining instance variables
    private Player player;
    private CredentialsDatabase database;
    private Settings accountSettings;

    // creating the constructor to initialize the player and database in the HomePage class
    public HomePage(Player player, CredentialsDatabase database) {
        this.player = player;
        this.database = database;
        this.accountSettings = new Settings(player, database);
    }

    // created method to view logged-in player's record
    public PlayerStats viewYourOwnRecords(){
        PlayerStats playerStats = new PlayerStats(player);
        return playerStats;
    }

    // created method to view another player's records
    public PlayerStats viewOtherPlayerRecords(Player otherPlayer){
        PlayerStats playerStats = new PlayerStats(otherPlayer);
        return playerStats;
    }

    // created method to view account settings
    public Settings viewSettings(){
        return accountSettings;
    }

    // created method to play Connect4
    public void playConnect4(){
        System.out.println("Starting Connect4 game");
    }

    // created method to play TicTacToe
    public void playTicTacToe(){
        System.out.println("Starting TicTacToe game");
    }

    // created method to play Checkers
    public void playCheckers(){
        System.out.println("Starting Checkers game");
    }
}
