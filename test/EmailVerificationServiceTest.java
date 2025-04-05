import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.*;

import static org.junit.jupiter.api.Assertions.*;

public class EmailVerificationServiceTest {
    private CredentialsDatabase database;
    private Player player;

    @BeforeEach
    public void setUp() {
        database = new CredentialsDatabase();
        player = new Player("testUser", "test@gmail.com", "password123");
        database.addNewPlayer("testUser", player);
        EmailVerificationService.setDatabase(database);
    }

    @Test
    public void testGenerateVerificationCode() {
        String code = EmailVerificationService.generateVerificationCode();
        assertNotNull(code);
        assertTrue(code.matches("\\d{4}"), "Code should be 4-digit number");
    }

    @Test
    public void testSendVerificationEmailForgotPassword_ValidUser(){
        String code = "1234";
        boolean result = EmailVerificationService.sendVerificationEmailForgotPassword("testUser", code);
        assertTrue(result);
        assertEquals(code, player.getVerificationCode());
    }

    @Test
    public void testSendVerificationEmailForgotPassword_InvalidUser(){
        String code = "1234";
        boolean result = EmailVerificationService.sendVerificationEmailForgotPassword("testUser", code);
        assertFalse(result);
    }

    @Test
    public void testSendVerificationEmailForgotPassword_Null(){
        EmailVerificationService.setDatabase(null);
        boolean result = EmailVerificationService.sendVerificationEmailForgotPassword("testUser", "0000");
        assertFalse(result);
    }
    @Test
    public void testSendVerificationEmailForNewAccount_ValidUser(){
        String code = "1234";
        boolean result = EmailVerificationService.sendVerificationEmailForNewAccount("newtUser", code);
        assertTrue(result);

        Player newUser = TemporaryPlayerStorage.getPlayer("newtUser");
    }

    @Test
    public void testSendVerificationEmailForNewAccount_InvalidUser(){
        boolean result = EmailVerificationService.sendVerificationEmailForNewAccount("invalidtUser", "0000");
        assertFalse(result);

    }
}
