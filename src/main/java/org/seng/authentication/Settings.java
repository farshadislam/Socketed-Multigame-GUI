package org.seng.authentication;

public class Settings {

    // create fields for the methods
    Player player;

    CredentialsDatabase database;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CredentialsDatabase getDatabase() {
        return database;
    }

    public void setDatabase(CredentialsDatabase database) {
        this.database = database;
    }

    /**
     * constructor for settings class
     * @param player the player using the platform
     * @param database the database with all the player information
     */
    public Settings(Player player, CredentialsDatabase database){
        this.player = player;
        this.database = database;
    }

    /**
     * method for deleting account
     * @param password the password for the account
     * @return true if the account is deleted, false otherwise
     */
    public boolean deleteAccount(String password){
        if (player.getPassword().equals(password)){
            return database.deleteExistingPlayer(player.getUsername());
        }
        return false;
    }

    /**
     * method for changing username
     * @param newUsername new username they want to use
     * @return true if the username is changed, false otherwise
     */
    public boolean changeUsername(String newUsername){
        if (newUsername!=null && !(newUsername.isEmpty())){
            player.setUsername(newUsername);

        }
        return false;
    }

    /**
     * method for changing email
     * @param newEmail new email they want to use
     * @return true if the email is changed, false otherwise
     */
    public boolean changeEmail(String newEmail) {
        if (newEmail!=null && !(newEmail.isEmpty())){
            player.setEmail(newEmail);
        }
        return false;
    }

    /**
     * method for changing password
     * @param password current password being used
     * @param newPassword new password they want to use
     * @return true if the password was changed, false otherwise
     */
    public boolean changePassword(String password, String newPassword) {
        if (player.getPassword().equals(password)) {
            if (newPassword != null && !(newPassword.isEmpty())) {
                player.setPassword(newPassword);
            }
        }
        return false;
    }

    /**
     * method for logging out
     * @return the login page
     */
    public LoginPage logout(){
        database.saveDatabase();
        return new LoginPage(database);
    }

}
