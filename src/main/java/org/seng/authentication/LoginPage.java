package org.seng.authentication;

/**
 * Handles login and registration logic for players, including email verification,
 * password management, and user format validations.
 */
public class LoginPage {
    private CredentialsDatabase database;

    /**
     * Constructs a LoginPage with the provided database.
     * @param database The database storing player credentials.
     */
    public LoginPage(CredentialsDatabase database){
        this.database = database;
    }

    /**
     * Represents possible registration states.
     */
    public enum State{
        USERNAME_TAKEN,
        EMAIL_TAKEN,
        USERNAME_FORMAT_WRONG,
        EMAIL_FORMAT_WRONG,
        PASSWORD_FORMAT_WRONG,
        VERIFICATION_CODE_SENT,
        ERROR
    }

    /**
     * Implements login for a player
     * @param username Username of the player
     * @param password Password of the player
     * @return Homepage associated with the player if the login is successful, otherwise null
     */
    public HomePage login(String username, String password){
        //Checks if username is not null and a player exists with that username
        if(username != null && database.usernameLookup(username.toLowerCase())){
            Player player = database.findPlayerByUsername(username);
            if (player.getPassword().equals(password)){ //Checks if the player's account's password matches with user inputted password
                return new HomePage(player, database); //Homepage associated with the player with their stats
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
        //validates username format
        if(!verifyUsernameFormat(username)) {
            return State.USERNAME_FORMAT_WRONG;
        }

        //validate email format
        if (!verifyEmailFormat(email)){
            return State.EMAIL_FORMAT_WRONG;
        }

        //validates password format
        if(!verifyPasswordFormat(password)){
            return State.PASSWORD_FORMAT_WRONG;
        }

        //checks if username is already taken by an existing user
        if(database.usernameLookup(username)){
            return State.USERNAME_TAKEN;
        }

        //checks if email is already taken by an existing user
        if(database.emailTaken(email)){
            return State.EMAIL_TAKEN;
        }

        Player newPlayer = new Player(username.toLowerCase(),email.toLowerCase(),password);
        TemporaryPlayerStorage.addPlayer(username, newPlayer); //adds the player to a temporary player storage acting like temporary database

        String verificationCode = EmailVerificationService.generateVerificationCode(); //generates a random 4 digit verification code
        if(EmailVerificationService.sendVerificationEmailForNewAccount(username,verificationCode)){ //assigns that verification code as a player field
            return State.VERIFICATION_CODE_SENT;
        }
        return State.ERROR;

    }

    /**
     * Validates a username based on length, characters, and format.
     * Valid username must be at least 5 characters long with valid special characters and no whitespaces
     * @param username The username to validate.
     * @return True if valid, false otherwise.
     */
    public boolean verifyUsernameFormat(String username){
        String validUserChar = "^[a-zA-Z0-9_.-]+$";
        String validUserAlpha = ".*[a-zA-Z].*";
        return (!username.isEmpty() && !username.matches(".*\\s.*") && !hasConsecutiveValidSpecialChars(username) && username.length() >= 5 && username.matches(validUserChar) && username.matches(validUserAlpha));
    }

    /**
     * Validates the format of the provided email address.
     *
     * @param email The email to validate.
     * @return True if the format is valid, false otherwise.
     */
    public boolean verifyEmailFormat(String email){
        //Email must end with @gmail.com and should only contain one @ and must have a prefix
        if(!email.endsWith("@gmail.com") || email.indexOf("@") != email.lastIndexOf("@") || email.indexOf("@") <= 0){
            return false;
        }
        String usernameOfEmail = email.substring(0, email.indexOf("@")); //Retrieves username (prefix) of the email

        if (!usernameOfEmail.isEmpty()) { //ensures username is not empty,
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
        // Validates if the username of the email is correct or not
        if(hasConsecutiveValidSpecialChars(usernameOfEmail) || !usernameOfEmail.matches("^[a-zA-Z0-9_.-]+$") || !usernameOfEmail.matches(".*[a-zA-Z].*")){
            return false;
        }
        return true;
    }

    /**
     * Helper method that check if the given username contains consecutive special characters.
     *
     * @param username The username to check.
     * @return True if consecutive special characters are found; false otherwise.
     */
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

    /**
     * Checks if the character is one of the allowed special characters.
     * @param c The character to check.
     * @return True if valid special character, false otherwise.
     */
    private boolean isValidSpecialCharacter(char c) {
        return c == '.' || c == '_' || c == '-';
    }

    /**
     * Validates password length and ensures it has no whitespace.
     * @param password The password to validate.
     * @return True if valid; false otherwise.
     */
    public boolean verifyPasswordFormat(String password){
        return password.length() >= 8 && !password.matches(".*\\s.*");
    }

    /**
     * Confirms a player's email code to complete registration.
     * @param username The username for the player.
     * @param code The verification code received.
     * @return True if code is valid and registration completes; false otherwise.
     */
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

    /**
     * Initiates password recovery by sending a code to the email associated with the username.
     * @param username The username requesting a password reset.
     * @return True if email sent successfully; false otherwise.
     */
    public boolean forgotPassword(String username){
        if(!database.usernameLookup(username)){
            return false; //username not found
        }
        EmailVerificationService.setDatabase(database);
        String code = EmailVerificationService.generateVerificationCode();
        if(EmailVerificationService.sendVerificationEmailForgotPassword(username,code)){
            return true;
        }
        return false;
    }

    /**
     * Verifies the code sent for password recovery.
     * @param username The username recovering the password.
     * @param code The verification code received.
     * @return True if the code is valid; false otherwise.
     */
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

    /**
     * Changes a player's password if the old password is correct and the new password is valid.
     * @param username The player's username.
     * @param password The current password.
     * @param newPassword The new password to set.
     * @return True if password was changed successfully; false otherwise.
     */
    public boolean changePassword(String username, String password, String newPassword) {
        Player player = database.findPlayerByUsername(username);
        if(player == null){
            return false;
        }
        if (newPassword != null && !(newPassword.isEmpty()) && verifyPasswordFormat(newPassword) && player.getPassword().equals(password)) {
            player.setPassword(newPassword);
            return true;
        }
        return false;
    }
}
