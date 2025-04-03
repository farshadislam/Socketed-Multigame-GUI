import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.Player;

public class SettingsTest {
    private Player player; // create player object for testing

    @BeforeEach
    public void initializePlayer(){
        player = new Player("user", "user@gmail.com", "passWORD");
    }

    @Test
    public void testChangeUsername(){

    }
}
