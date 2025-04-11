import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.authentication.Settings;
import org.seng.leaderboard_matchmaking.checkersStats;
import org.seng.leaderboard_matchmaking.connect4Stats;
import org.seng.leaderboard_matchmaking.ticTacToeStats;

import static org.junit.jupiter.api.Assertions.*;
//import static org.seng.authentication.EmailVerificationService.database;

public class SettingsTest {
    private Player player; // create player object for testing
   private Settings settings;
    CredentialsDatabase database;

    // initialize the player and settings before each test
    @BeforeEach
    public void initializeFields(){
        player = new Player("newUser", "newUser@gmail.com", "passWORD");
        database = new CredentialsDatabase();
        settings = new Settings(player, database);
    }

    // get player
    @Test
    public void general1(){
        assertEquals(player, settings.getPlayer());
    }

    // set player
    @Test
    public void general2(){
        settings.setPlayer(player);  // Set the new player
        assertEquals(player, settings.getPlayer());
    }

    // get database
    @Test
    public void general3() {
        assertEquals(database, settings.getDatabase());
    }

    // set database
    @Test
    public void general4() {
        CredentialsDatabase newDatabase = new CredentialsDatabase();
        settings.setDatabase(newDatabase);
        assertEquals(newDatabase, settings.getDatabase());
    }

    // valid username
    @Test
    public void testChangeUsername1(){
        database.addNewPlayer("newUser", player);
        assertTrue(settings.changeUsername("superUser"));
        assertEquals("superuser", player.getUsername());
    }

    // username too short
    @Test
    public void testChangeUsername2(){
        assertFalse(settings.changeUsername("boo"));
    }

    // username already exists
    @Test
    public void testChangeUsername3(){
        Player p2 = new Player("java123", "java123@gmail.com", "pass12345");
        database.addNewPlayer("java123", p2);
        assertFalse(settings.changeUsername("java123"));
    }

    // null username
    @Test
    public void testChangeUsername4(){
        assertFalse(settings.changeUsername(null));
    }

    // whitespace username
    @Test
    public void testChangeUsername5(){
        assertFalse(settings.changeUsername(" "));
    }

    // empty username
    @Test
    public void testChangeUsername6(){
        assertFalse(settings.changeUsername(""));
    }

    // random character username
    @Test
    public void testChangeUsername7(){
        assertFalse(settings.changeUsername("^%&%$*$^"));
    }

    // consecutive valid characters username
    @Test
    public void testChangeUsername8(){
        assertFalse(settings.changeUsername("user__user"));
    }

    // doesn't contain alphabet username
    @Test
    public void testChangeUsername9(){
        assertFalse(settings.changeUsername("123456"));
    }

    // same user
    @Test
    public void testChangeUsername10(){
        database.addNewPlayer("newUser", player);
        assertFalse(settings.changeUsername("newUser"));
    }

    // special character
    @Test
    public void testChangeUsername11(){
        assertFalse(settings.changeUsername("newUser#123"));
    }

    // whitespace username
    @Test
    public void testChangeUsername12(){
        assertFalse(settings.changeUsername("new user"));
    }

    // valid email
    @Test
    public void testChangeEmail1(){
        assertTrue(settings.changeEmail("user12345@gmail.com"));
        assertEquals("user12345@gmail.com", player.getEmail());
    }

    // email user not gmail
    @Test
    public void testChangeEmail2(){
        assertFalse(settings.changeEmail("user@hotmail.com"));
        assertNotEquals("user@gmail.com", player.getEmail());
    }

    // empty email
    @Test
    public void testChangeEmail3(){
        assertFalse(settings.changeEmail(""));
    }

    // whitespace email
    @Test
    public void testChangeEmail5(){
        assertFalse(settings.changeEmail(" "));
    }

    // email with no @
    @Test
    public void testChangeEmail6(){
        assertFalse(settings.changeEmail("newUsergmail.com"));
    }

    // email with no user
    @Test
    public void testChangeEmail7(){
        assertFalse(settings.changeEmail("@gmail.com"));
    }

    // same email
    @Test
    public void testChangeEmail8(){
        database.addNewPlayer("newUser", player);
        assertFalse(settings.changeEmail("newUser@gmail.com"));
    }

    // fake domain email
    @Test
    public void testChangeEmail9(){
        assertFalse(settings.changeEmail("newUser@blahhhh.com"));
    }

    // email no domain
    @Test
    public void testChangeEmail10(){
        assertFalse(settings.changeEmail("newUser@"));
    }

    // email with space
    @Test
    public void testChangeEmail11(){
        assertFalse(settings.changeEmail("new user@gmail.com"));
    }

    // valid password
    @Test
    public void testChangePassword1(){
        assertTrue(settings.changePassword("SENG300!"));
        assertEquals("SENG300!", player.getPassword());
    }

    // password too short
    @Test
    public void testChangePassword2(){
        assertFalse(settings.changePassword("seng"));
    }

    // null password
    @Test
    public void testChangePassword3(){
        assertFalse(settings.changePassword(null));
    }

    // whitespace password
    @Test
    public void testChangePassword4(){
        assertFalse(settings.changePassword(" "));
    }

    // empty password
    @Test
    public void testChangePassword5(){
        assertFalse(settings.changePassword(""));
    }

//    // wrong password
//    @Test
//    public void testChangePassword6(){
//        assertTrue(settings.changePassword("heysoulsister"));
//    }

    // both empty password
    @Test
    public void testChangePassword7(){
        assertFalse(settings.changePassword(""));
    }


//    // player deleted
//    @Test
//    public void deleteAccount1(){
//        database.addNewPlayer(player.getUsername(), player);
//        assertTrue(settings.deleteAccount("passWORD"));
//    }
//
//    // player not deleted (wrong password)
//    @Test
//    public void deleteAccount2(){
//        database.addNewPlayer("newUser", player);
//        assertFalse(settings.deleteAccount("password"));
//    }
//
//    // player not exists
//    @Test
//    public void deleteAccount3(){
//        assertFalse(settings.deleteAccount("password"));
//    }
//
//    // player already deleted
//    @Test
//    public void deleteAccount4(){
//        database.addNewPlayer(player.getUsername(), player);
//        settings.deleteAccount("passWORD");
//        assertFalse(settings.deleteAccount("passWORD"));
//    }

    // setter and getter for TicTacToeStats
    @Test
    public void testTicTacToeStats() {
        ticTacToeStats ticTacToe = new ticTacToeStats("newUser");
        player.setTicTacToeStats(ticTacToe);
        assertEquals(ticTacToe, player.getTicTacToeStats());
    }

    // setter and getter for CheckersStats
    @Test
    public void testCheckersStats() {
        checkersStats checkers = new checkersStats("newUser");
        player.setCheckersStats(checkers);
        assertEquals(checkers, player.getCheckersStats());
    }

    // setter and getter for CheckersStats
    @Test
    public void testConnect4Stats() {
        connect4Stats connect4 = new connect4Stats("newUser");
        player.setConnect4Stats(connect4);
        assertEquals(connect4, player.getConnect4Stats());
    }

    // valid email code
    @Test
    void testVerifyEmailCodeForNewEmail1() {
        String oldEmail = "oldEmail@example.com";
        String newEmail = "newEmail@example.com";
        String verificationCode = "1234";

        player.setEmail(oldEmail);
        player.setVerificationCode("1234");

        boolean result = settings.verifyEmailCodeForNewEmail(newEmail, verificationCode);
        assertTrue(result);
        assertEquals(newEmail.toLowerCase(), player.getEmail());
    }

    // invalid email code
    @Test
    void testVerifyEmailCodeForNewEmail2() {
        String oldEmail = "oldEmail@example.com";
        String newEmail = "newEmail@example.com";
        String verificationCode = "abcd";

        player.setEmail(oldEmail);
        player.setVerificationCode("1234");

        boolean result = settings.verifyEmailCodeForNewEmail(newEmail, verificationCode);
        assertFalse(result);
        assertEquals(oldEmail.toLowerCase(), player.getEmail());
    }

    // invalid email code too short
    @Test
    void testVerifyEmailCodeForNewEmail3() {
        String oldEmail = "oldEmail@example.com";
        String newEmail = "newEmail@example.com";
        String verificationCode = "12";

        player.setEmail(oldEmail);
        player.setVerificationCode("1234");

        boolean result = settings.verifyEmailCodeForNewEmail(newEmail, verificationCode);
        assertFalse(result);
        assertEquals(oldEmail.toLowerCase(), player.getEmail());
    }

    // deleting an account
    @Test
    public void testDeleteAccount(){
        database.addNewPlayer(player.getUsername(), player);
        assertTrue(settings.deleteAccount());
        assertFalse(database.usernameLookup(player.getUsername()));
    }

    // verifying the code for new emails
    @Test
    public void testVerifyCodeForNewEmail(){
        player.setVerificationCode("1234");
        assertTrue(settings.verifyEmailCodeForNewEmail("updated@email.com", "1234"));
        assertEquals("updated@email.com", player.getEmail());
    }

}
