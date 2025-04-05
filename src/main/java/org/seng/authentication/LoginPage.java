package org.seng.authentication;

import java.util.ArrayList;

public class LoginPage {
    private CredentialsDatabase database;

    public LoginPage(CredentialsDatabase database){
        this.database = database;
    }

    public enum State{
        USERNAME_TAKEN,
        EMAIL_TAKEN,
        USERNAME_FORMAT_WRONG,
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
        if(username != null && database.usernameLookup(username.toLowerCase())){
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
    public State register(String username, String email, String password){
        if(username.isEmpty() || username.matches(".*\\s.*") || hasConsecutiveValidSpecialChars(username)){
            return State.USERNAME_FORMAT_WRONG;
        }

        if (!verifyEmailFormat(email)){
            return State.EMAIL_FORMAT_WRONG;
        }

        if(!verifyPasswordFormat(password)){
            return State.PASSWORD_FORMAT_WRONG;
        }

        if(database.usernameLookup(username)){
            return State.USERNAME_TAKEN;
        }

        if(database.emailTaken(email)){
            return State.EMAIL_TAKEN;
        }

        Player newPlayer = new Player(username.toLowerCase(),email.toLowerCase(),password);
        TemporaryPlayerStorage.addPlayer(username, newPlayer);

        String verificationCode = EmailVerificationService.generateVerificationCode();
        if(EmailVerificationService.sendVerificationEmailForNewAccount(username,verificationCode)){
            return State.VERIFICATION_CODE_SENT;
        }
        return State.ERROR;

    }

    /**
     * Verifies if the email format provided by the player is correct or not
     * @param email
     * @return
     */
    private boolean verifyEmailFormat(String email){
        if(!email.endsWith("@gmail.com") || email.indexOf("@") != email.lastIndexOf("@") || email.indexOf("@") <= 0){
            return false;
        }
        String usernameOfEmail = email.substring(0, email.indexOf("@"));

        if (!usernameOfEmail.isEmpty()) {
            char firstChar = usernameOfEmail.charAt(0);
            char lastChar = usernameOfEmail.charAt(usernameOfEmail.length() - 1);

            // Special characters to check
            if (isValidSpecialCharacter(firstChar)||isValidSpecialCharacter(lastChar)) {
                return false;
            }
        }

        for(int i = 0; i < usernameOfEmail.length(); i++){
            char character = usernameOfEmail.charAt(i);
            if(!(Character.isLetterOrDigit(character) || isValidSpecialCharacter(character))){
                return false;
            }
        }
        if(hasConsecutiveValidSpecialChars(usernameOfEmail)){
            return false;
        }
        return true;
    }

    private boolean hasConsecutiveValidSpecialChars(String username) {
        for (int i = 0; i < username.length() - 1; i++) {
            char currentChar = username.charAt(i);
            char nextChar = username.charAt(i + 1);

            if (isValidSpecialCharacter(currentChar) && isValidSpecialCharacter(nextChar)) {
                return true; // Consecutive special characters found
            }
        }
        return false; // No consecutive special characters
    }

    private boolean isValidSpecialCharacter(char c) {
        return c == '.' || c == '_' || c == '-';
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
        database.addNewPlayer(newPlayer.getUsername().toLowerCase(), newPlayer);
        TemporaryPlayerStorage.removePlayer(username.toLowerCase());

        //Stub
        if(newPlayer.getVerificationCode()!= null && newPlayer.getVerificationCode().equals(code)){
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
        if(player.getVerificationCode()!= null && player.getVerificationCode().equals(code)){
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
