package org.seng.authentication;

public class EmailVerificationService {
    private static CredentialsDatabase database;
    public EmailVerificationService(CredentialsDatabase database){
        this.database = database;
    }

    public static String generateVerificationCode() {
        int code = (int) (Math.random() * 1000000);
        return String.format("%04d", code);  // Ensure it's always 6 digits
    }
    public static void sendVerificationEmail(String username, String verificationCode) {
        Player player = database.findPlayerByUsername(username);
        player.setVerificationCode(verificationCode);
    }
}
