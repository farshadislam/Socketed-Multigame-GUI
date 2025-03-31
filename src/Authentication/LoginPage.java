package Authentication;

import java.util.ArrayList;

public class LoginPage {
    private CredentialsDatabase database;

    public LoginPage(CredentialsDatabase database){
        this.database = database;
    }

    private enum State{
        USERNAME_TAKEN,
        EMPTY_USERNAME,
        EMPTY_PASSWORD,
        EMAIL_FORMAT_WRONG,
        VERIFICATION_CODE_SENT,
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


    public State register(String username, String password, String email){
        if(username.isEmpty()){
            return State.EMPTY_USERNAME;
        }
        if(password.isEmpty()) {
            return State.EMPTY_PASSWORD;
        }
        if(database.usernameLookup(username)){
            return State.USERNAME_TAKEN;
        }
        if (!verifyEmailFormat(email)){
            return State.EMAIL_FORMAT_WRONG;
        }
        Player newPlayer = new Player(username);
        newPlayer.setPassword(password);
        newPlayer.setEmail(email);
        newPlayer.setRank(null);
        newPlayer.setCheckersStats(null);
        newPlayer.setConnect4Stats(null);
        newPlayer.setTicTacToeStats(null);
        //Yet to implement the code
        return State.VERIFICATION_CODE_SENT;

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

    public boolean verifyEmailCode(String email, String code){
        Player newPlayer = TemporaryPlayerStorage.getPlayer(username);
        if(newPlayer == null){
            return false;
        }
        if (code.length() != 4 || !code.matches("\\d{4}")) {
            return false;
        }
        database.addNewPlayer(newPlayer.getUsername(), newPlayer);
        TemporaryPlayerStorage.remove(newPlayer);
        return true;
    }
}
