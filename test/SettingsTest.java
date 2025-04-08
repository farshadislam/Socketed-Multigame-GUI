import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.authentication.Settings;

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

    // valid username
    @Test
    public void testChangeUsername1(){
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

    //doesn't contain alphabet username
    @Test
    public void testChangeUsername9(){
        assertFalse(settings.changeUsername("123456"));
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

//    // null email
//    @Test
//    public void testChangeEmail4(){
//        assertNull(settings.changeEmail(null));
//    }

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

    // email with user that doesn't have alphabet

    // email with user that has consecutive valid character

    // valid password
    @Test
    public void testChangePassword1(){
        assertTrue(settings.changePassword("passWORD", "SENG300!"));
        assertEquals("SENG300!", player.getPassword());
    }

    // password too short
    @Test
    public void testChangePassword2(){
        assertFalse(settings.changePassword("passWORD", "seng"));
    }

    // null password
    @Test
    public void testChangePassword3(){
        assertFalse(settings.changePassword("passWORD", null));
    }

    // whitespace password
    @Test
    public void testChangePassword4(){
        assertFalse(settings.changePassword("passWORD", " "));
    }

    // empty password
    @Test
    public void testChangePassword5(){
        assertFalse(settings.changePassword("passWORD", ""));
    }

    // player deleted
    @Test
    public void deleteAccount1(){
        database.addNewPlayer(player.getUsername(), player);
        assertTrue(settings.deleteAccount("passWORD"));
    }

    // player not deleted (wrong password)
    @Test
    public void deleteAccount2(){
        assertFalse(settings.deleteAccount("password"));
    }


}
