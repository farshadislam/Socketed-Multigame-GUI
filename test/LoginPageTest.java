import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoginPageTest {
    private Player player;
    private CredentialsDatabase database;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        database = new CredentialsDatabase(); // creates a fresh, clean database each time
        loginPage = new LoginPage(database);
        TemporaryPlayerStorage.clear();
    }

    //Correct username and password
    @Test
    public void loginTest1(){
        Player player1 = new Player("player1","player1@gmail.com","player11");
        database.addNewPlayer("player1",player1);
        Player player2 = new Player("player2","player2@gmail.com","player22");
        database.addNewPlayer("player2",player2);
        HomePage result = loginPage.login("player1","player11");

        assertNotNull(result);
        assertEquals(player1, result.getPlayer());
    }

    //Wrong password, correct username
    @Test
    public void loginTest2(){
        Player player1 = new Player("player1","player1@gmail.com","player11");
        database.addNewPlayer("player1",player1);
        HomePage result = loginPage.login("player1","player112");
        assertNull(result);
    }

    //Wrong username (username does not exist), correct password
    @Test
    public void loginTest3(){
        Player player1 = new Player("player1","player1@gmail.com","player11");
        database.addNewPlayer("player1",player1);
        HomePage result = loginPage.login("player10","player11");
        assertNull(result);
    }

    //Wrong username (username exists), correct password
    @Test
    public void loginTest4(){
        Player player1 = new Player("player1","player1@gmail.com","player11");
        database.addNewPlayer("player1",player1);
        Player player2 = new Player("player2","player2@gmail.com","player22");
        database.addNewPlayer("player2",player2);
        HomePage result = loginPage.login("player2","player11");
        assertNull(result);

    }

    //Empty database
    @Test
    public void loginTest5(){
        HomePage result = loginPage.login("player","player00");
        assertNull(result);
    }

    //Unverified new player logging in
    @Test
    public void loginTest6(){
        loginPage.register("player1","player1@gmail.com","player11");
        HomePage result = loginPage.login("player1","player11");
        assertNull(result);
    }

    //Empty username
    @Test
    public void registerTest1(){
        LoginPage.State result = loginPage.register("","player1@gmail.com","player11");
        assertEquals(LoginPage.State.USERNAME_FORMAT_WRONG, result);
    }

    //Empty username with whitespaces - space
    @Test
    public void registerTest2(){
        LoginPage.State result = loginPage.register(" ","player1@gmail.com","player11");
        assertEquals(LoginPage.State.USERNAME_FORMAT_WRONG, result);
    }

    //Empty username with whitespaces - /n
    @Test
    public void registerTest3(){
        LoginPage.State result = loginPage.register("\n","player1@gmail.com","player11");
        assertEquals(LoginPage.State.USERNAME_FORMAT_WRONG, result);
    }

    //Wrong email format - NO @
    @Test
    public void registerTest4(){
        LoginPage.State result = loginPage.register("player1","player1gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    //Wrong email format - EMPTY USERNAME
    @Test
    public void registerTest5(){
        LoginPage.State result = loginPage.register("player1","@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    //Wrong email format - SPACE AS USERNAME
    @Test
    public void registerTest6(){
        LoginPage.State result = loginPage.register("player1"," @gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    //Wrong email format - WHITESPACE CHARACTER AS USERNAME
    @Test
    public void registerTest7(){
        LoginPage.State result = loginPage.register("player1","\n@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    //Wrong email format - USERNAME WITH SPACE
    @Test
    public void registerTest8(){
        LoginPage.State result = loginPage.register("player1","pla yer@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    //Wrong email format - TWO @s
    @Test
    public void registerTest9(){
        LoginPage.State result = loginPage.register("player1","player@gmail@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    //Wrong email format - WHITESPACE IN DOMAIN
    @Test
    public void registerTest10(){
        LoginPage.State result = loginPage.register("player1","player@gmail. com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    //Wrong email format - DOES NOT END WITH @GMAIL.COM
    @Test
    public void registerTest11(){
        LoginPage.State result = loginPage.register("player1","player@gmail.comm","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    // . before email
    @Test
    public void registerTest12(){
        LoginPage.State result = loginPage.register("player1","player.@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    // wrong email format - character before email
    @Test
    public void registerTest13(){
        LoginPage.State result = loginPage.register("player1",".player@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

    // Correct email format - allows special characters '.', '-', '_'
    @Test
    public void registerTest15(){
        LoginPage.State result = loginPage.register("player1","pl.a-ye_r@gmail.com","player11");
        assertEquals(LoginPage.State.VERIFICATION_CODE_SENT, result);
    }

    // verification code
    @Test
    public void registerTest16(){
        LoginPage.State result = loginPage.register("player.1","player@gmail.com","player11");
        assertEquals(LoginPage.State.VERIFICATION_CODE_SENT, result);
    }

    // password short
    @Test
    public void registerTest17(){
        LoginPage.State result = loginPage.register("player1","player@gmail.com","play");
        assertEquals(LoginPage.State.PASSWORD_FORMAT_WRONG, result);
    }

    // username taken
    @Test
    public void registerTest18(){
        Player p2 = new Player("player1", "player1@gmail.com", "player11");
        database.addNewPlayer("player1", p2);
        LoginPage.State result = loginPage.register("player1", "newemail@gmail.com", "newpassword");
        assertEquals(LoginPage.State.USERNAME_TAKEN, result);
    }

    // email taken
    @Test
    public void registerTest19(){
        Player player1 = new Player("player1", "player1@gmail.com", "player11");
        database.addNewPlayer("player1", player1);
        LoginPage.State result = loginPage.register("player2", "player1@gmail.com", "player22");
        assertEquals(LoginPage.State.EMAIL_TAKEN, result);
    }

    // valid username
    @Test
    public void registerTest20(){
        LoginPage.State result = loginPage.register("thisisuser","player1@gmail.com","player11");
        assertEquals(LoginPage.State.VERIFICATION_CODE_SENT, result);
    }

    // short username
    @Test
    public void registerTest21(){
        LoginPage.State result = loginPage.register("usr","player1@gmail.com","player11");
        assertEquals(LoginPage.State.USERNAME_FORMAT_WRONG, result);
    }

    // username no alphabet
    @Test
    public void registerTest22(){
        LoginPage.State result = loginPage.register("12345","player1@gmail.com","player11");
        assertEquals(LoginPage.State.USERNAME_FORMAT_WRONG, result);
    }

    // username consecutive special characters
    @Test
    public void registerTest23(){
        LoginPage.State result = loginPage.register("user...name","player1@gmail.com","player11");
        assertEquals(LoginPage.State.USERNAME_FORMAT_WRONG, result);
    }

    //Wrong email format - invalid character
    @Test
    public void registerTest24(){
        LoginPage.State result = loginPage.register("player1","player$#@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }


    // forgot password for nonexistent username
    @Test
    public void forgotPasswordTest1(){
        boolean result = loginPage.forgotPassword("player123");
        assertFalse(result);
    }

    // forgot password email sent
    @Test
    public void forgotPasswordTest2(){
        Player player1 = new Player("player1", "player1@gmail.com", "password");
        database.addNewPlayer("player1", player1);
        boolean result = loginPage.forgotPassword("player1");
        assertTrue(result);
    }

    // valid verification code
    @Test
    public void verifyEmailCodeForgotPasswordTest1(){
        Player player1 = new Player("player1", "player1@gmail.com", "password");
        database.addNewPlayer("player1", player1); // Simulate verified registration
        loginPage.forgotPassword("player1");
        boolean result = loginPage.verifyEmailCodeForgotPassword("player1", "1234");
        assertTrue(result);
    }

    // invalid verification code
    @Test
    public void verifyEmailCodeForgotPasswordTest2(){
        loginPage.register("player1", "player1@gmail.com", "password");
        loginPage.forgotPassword("player1");
        boolean result = loginPage.verifyEmailCodeForgotPassword("player1", "wrongcode");
        assertFalse(result);
    }

    // change password
    @Test
    public void changePasswordTest1(){
        Player player1 = new Player("player1", "player1@gmail.com", "password");
        database.addNewPlayer("player1", player1);
        boolean result = loginPage.changePassword("player1", "password", "newpassword");
        assertTrue(result);
    }

    // change password invalid
    @Test
    public void changePasswordTest2(){
       loginPage.register("player1", "player1@gmail.com", "oldpassword");
        boolean result = loginPage.changePassword("player1", "oldpassword", "short");
        assertFalse(result);
    }

    // change password incorrect current password
    @Test
    public void changePasswordTest3(){
        loginPage.register("player1", "player1@gmail.com", "oldpassword");
        boolean result = loginPage.changePassword("player1", "wrongpassword", "newpassword");
        assertFalse(result);
    }

    //Accepting any 4 digit code
    @Test
    public void verifyEmailCodeForRegisterTest1(){
        loginPage.register("varisha","useless101@gmail.com","ilovehamna");
        boolean result = loginPage.verifyEmailCodeForRegister("varisha", "1234");
        assertTrue(result);
    }

    //Player is null
    @Test
    public void verifyEmailCodeForRegisterTest2(){
        boolean result = loginPage.verifyEmailCodeForRegister("player1", "1234");
        assertFalse(result);
    }

    //Entering the correct verification code
    @Test
    public void verifyEmailCodeForRegisterTest3() {
        loginPage.register("varisha", "useless101@gmail.com", "ilovehamna");
        Player player = TemporaryPlayerStorage.getPlayer("varisha");
        String code = player.getVerificationCode();
        boolean result = loginPage.verifyEmailCodeForRegister("varisha", code);
        assertTrue(result);
    }


}
