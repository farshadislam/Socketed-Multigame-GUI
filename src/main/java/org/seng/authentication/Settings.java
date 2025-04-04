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
            if(!database.usernameLookup(newUsername))
                player.setUsername(newUsername);
                return true;
        }
        return false;
    }

    /**
     * method for changing email
     * @param newEmail new email they want to use
     * @return true if the email is changed, false otherwise
     */
    public boolean changeEmail(String newEmail) {
        if (verifyEmailFormat(newEmail)){
            return true;
        }
        return false;
    }

    private boolean verifyEmailFormat(String email){
        if(!email.endsWith("@gmail.com") || email.indexOf("@") != email.lastIndexOf("@") || email.indexOf("@") <= 0){
            return false;
        }
        String usernameOfEmail = email.substring(0, email.indexOf("@"));
        for(int i = 0; i < usernameOfEmail.length(); i++){
            char character = usernameOfEmail.charAt(i);
            if(!(Character.isLetterOrDigit(character) || character == '.' || character == '_' || character == '-')){
                return false;
            }
        }
        return true;
    }

    public boolean verifyEmailCodeForNewEmail(String email, String code){
        //stub
        if((player.getVerificationCode().equals(code))||(code.length() == 4 && code.matches("\\d{4}"))){
            player.setEmail(email);
            return true;
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
            if (newPassword != null && !(newPassword.isEmpty()) && verifyPasswordFormat(newPassword)) {
                player.setPassword(newPassword);
            }
        }
        return false;
    }

    private boolean verifyPasswordFormat(String password){
        return password.length() >= 8 && !password.matches(".*\\s.*");
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
