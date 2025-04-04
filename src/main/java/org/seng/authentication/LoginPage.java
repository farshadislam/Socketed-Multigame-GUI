package org.seng.authentication;

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
        PASSWORD_FORMAT_WRONG,
        VERIFICATION_CODE_SENT,
        USERNAME_NOT_FOUND,
        ERROR
    }

    /**
     * Implements login for a player
     * @param username Username of the player
     * @param password Password of the player
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

    /**
     * Implements sending a verification code to the email when creating a new account
     * @param username Username of the new player
     * @param email Email of the new player
     * @param password Password of the player
     * @return State indicating if the verification code has been sent, and if not what error has occurred
     */
    public State register(String username, String email, String password, char symbol){
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
        if(!verifyPasswordFormat(password)){
            return State.PASSWORD_FORMAT_WRONG;
        }

        Player newPlayer = new Player(username,email,password,symbol, null, 0, 0, 0);
        TemporaryPlayerStorage.addPlayer(username, newPlayer);

        String verificationCode = EmailVerificationService.generateVerificationCode();
        if(EmailVerificationService.sendVerificationEmailForNewAccount(username,verificationCode)){
            return State.VERIFICATION_CODE_SENT;
        }
        return State.ERROR;

    }

    /**
     * Verifies if the email format provided by the player is correct or not
     *
     * @param email
     * @return
     */
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

    private boolean verifyPasswordFormat(String password){
        return password.length() >= 8 && !password.matches(".*\\s.*");
    }

    public boolean verifyEmailCodeForRegister(String username, String code){
        Player newPlayer = TemporaryPlayerStorage.getPlayer(username);
        if(newPlayer == null){
            return false;
        }
        if (code.length() != 4 || !code.matches("\\d{4}")) {
            return false;
        }
        database.addNewPlayer(newPlayer.getUsername(), newPlayer);
        TemporaryPlayerStorage.removePlayer(username);

        //Stub
        if(newPlayer.getVerificationCode().equals(code)){
            return true;
        }

        return true;
    }

    public State forgotPassword(String username){
        if(!database.usernameLookup(username)){
            return State.USERNAME_NOT_FOUND; //username not found
        }
        EmailVerificationService.setDatabase(database);
        String code = EmailVerificationService.generateVerificationCode();
        if(EmailVerificationService.sendVerificationEmailForgotPassword(username,code)){
            return State.VERIFICATION_CODE_SENT;
        }
        return State.ERROR;
    }

    public boolean verifyEmailCodeForgotPassword(String username,String code){
        Player player = database.findPlayerByUsername(username);
        if(player == null){
            return false;
        }
        //stub
        if(player.getVerificationCode().equals(code)){
            return true;
        }
        return code.length() == 4 && code.matches("\\d{4}");
    }

    public boolean changePassword(String username, String password, String newPassword) {
        Player player = database.findPlayerByUsername(username);
        if(player == null){
            return false;
        }
        if (player.getPassword().equals(password)) {
            if (newPassword != null && !(newPassword.isEmpty()) && verifyPasswordFormat(newPassword)) {
                player.setPassword(newPassword);
            }
        }
        return false;
    }
}
