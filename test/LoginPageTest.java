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

    @Test
    public void registerTest12(){
        LoginPage.State result = loginPage.register("player1","player.@gmail.com","player11");
        assertEquals(LoginPage.State.EMAIL_FORMAT_WRONG, result);
    }

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
    @Test
    public void registerTest16(){
        LoginPage.State result = loginPage.register("player.1","player@gmail.com","player11");
        assertEquals(LoginPage.State.VERIFICATION_CODE_SENT, result);
    }

}
