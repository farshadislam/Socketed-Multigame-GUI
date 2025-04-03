package org.seng.authentication;

public class EmailVerificationService {
    private static CredentialsDatabase database;

    public static void setDatabase(CredentialsDatabase db) {
        database = db;
    }
    public static String generateVerificationCode() {
        int code = (int) (Math.random() * 1000000);
        return String.format("%04d", code);  // Ensure it's always 6 digits
    }
    public static boolean sendVerificationEmail(String username, String verificationCode) {
        if(database==null){
            return false;
        }
        if(!database.usernameLookup(username)){
            return false;
        }
        Player player = database.findPlayerByUsername(username);
        player.setVerificationCode(verificationCode);
        return true;
    }
}
