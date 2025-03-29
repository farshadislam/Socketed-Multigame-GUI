package Authentication;

public class Settings {

    // create fields
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
            player.setUsername(newUsername);

        }
        return false;
    }

    public boolean changeEmail(String newEmail) {
        if (newEmail!=null && !(newEmail.isEmpty())){
            player.setEmail(newEmail);
        }
        return false;
    }

    public boolean changePassword(String password, String newPassword) {
        if (player.getPassword().equals(password)) {
            if (newPassword != null && !(newPassword.isEmpty())) {
                player.setPassword(newPassword);
            }
        }
        return false;
    }

    public boolean logout(){
        database.saveDatabase();
        return true;
    }
}
