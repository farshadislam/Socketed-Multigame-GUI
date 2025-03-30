package Authentication;

public class LoginPage {
    private CredentialsDatabase database;

    public LoginPage(CredentialsDatabase database){
        this.database = database;
    }

    /**
     * Implements login for a player
     * @param username The username of the player
     * @param password The password of the player
     * @return Homepage associated with the player
     */
    public HomePage login(String username, String password){
        if(database.usernameLookup(username)){
            Player player = database.findPlayerByUsername(username);
            if (player.getPassword().equals(password)){
                return new HomePage(player, database);
            }
        }
        return null;
    }
}
