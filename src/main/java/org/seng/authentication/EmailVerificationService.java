package org.seng.authentication;

/**
 * The {@code EmailVerificationService} class handles generation and assignment of
 * email verification codes for both new account registrations and password resets.
 *
 * It interacts with either the {@link TemporaryPlayerStorage} or the {@link CredentialsDatabase}
 * depending on whether the user is registering or recovering their account.
 */

public class EmailVerificationService {
    /**
     * A reference to the main credentials database used for account lookup during
     * password recovery operations.
     */
    private static CredentialsDatabase database;

    /**
     * Sets the credentials database used by this service.
     *
     * @param db the credentials database to set
     */
    public static void setDatabase(CredentialsDatabase db) {
        database = db;
    }

    /**
     * Generates a 4-digit numeric verification code, zero-padded to ensure it is exactly 4 digits.
     *
     * @return a 4-digit verification code as a {@code String}
     */
    public static String generateVerificationCode() {
        int code = (int) (Math.random() * 10000);
        return String.format("%04d", code);  // Ensure it's always 6 digits
    }

    /**
     * Simulates sending a verification code for password reset.
     * The verification code is assigned to the player associated with the username.
     *
     * @param username the username of the player resetting the password
     * @param verificationCode the generated verification code
     * @return {@code true} if the email was 'sent' and the code assigned, {@code false} otherwise
     */
    public static boolean sendVerificationEmailForgotPassword(String username, String verificationCode) {
        if(database==null || !database.usernameLookup(username)){
            return false;
        }
        Player player = database.findPlayerByUsername(username);
        player.setVerificationCode(verificationCode);
        return true;
    }

    /**
     * Simulates sending a verification code for new account registration.
     * The verification code is assigned to the unverified player in temporary storage.
     *
     * @param username the username of the unverified player
     * @param verificationCode the generated verification code
     * @return {@code true} if the player exists in temporary storage and the code was assigned, {@code false} otherwise
     */
    public static boolean sendVerificationEmailForNewAccount(String username, String verificationCode) {
        if(TemporaryPlayerStorage.findUsername(username)){
            Player newPlayer = TemporaryPlayerStorage.getPlayer(username);
            newPlayer.setVerificationCode(verificationCode);
            return true;
        }
        return false;
    }
}
