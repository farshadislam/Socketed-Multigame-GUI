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
}
