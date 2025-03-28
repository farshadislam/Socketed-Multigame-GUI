public class Settings {

    // create fields
    Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    CredentialsDatabase database;

    // create constructor
    public Settings(Player player, CredentialsDatabase database){
        this.player = player;
        this.database = database;
    }

    public boolean deleteAccount(String password){
        if (player.getPassword().equals(password)){
            return database.deleteExistingPlayer(player.getUsername());
        }
        return false;
    }

    public boolean changeUsername(String newUsername){
        if (newUsername!=null && !(newUsername.isEmpty())){
            // for loop for values in database to confirm username is not taken
            // replace username

        }
        return false;
    }

    public boolean changeEmail(String newEmail) {
        // if email is not null and not empty
        // for loop to ensure email is not associated with another acc in database
        //update email
        return false;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        // if password is not empty and not null
        // for loop if password already does not exist in the database
        // update password
        return false;
    }

    public boolean logout(){
        return false;
    }
}
