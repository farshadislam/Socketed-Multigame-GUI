import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.*;

import static org.junit.jupiter.api.Assertions.*;

public class TemporaryPlayerStorageTest {
    private Player tempuser;

    @BeforeEach
    public void setUp() {
        tempuser = new Player("tempUser", "temp@example.com", "passWordTemp");
        TemporaryPlayerStorage.removePlayer("tempUser");
    }

    @Test
    public void testAddPlayer() {
        TemporaryPlayerStorage.addPlayer("tempUser", tempuser);
        assertTrue(TemporaryPlayerStorage.findUsername("tempUser"));
    }

    @Test
    public void testFindUsername(){
        TemporaryPlayerStorage.addPlayer("tempUser",tempuser);
        assertTrue(TemporaryPlayerStorage.findUsername("tempUser"));
        assertTrue(TemporaryPlayerStorage.findUsername("TempUser"));
        assertFalse(TemporaryPlayerStorage.findUsername("UnknownUser"));
    }

    @Test
    public void testGetPlayer(){
        TemporaryPlayerStorage.addPlayer("tempUser",tempuser);
        Player takenPlayer = TemporaryPlayerStorage.getPlayer("tempUser");
        assertNotNull(takenPlayer);
        assertEquals("tempuser", takenPlayer.getUsername());
    }

    @Test
    public void testRemovePlayer(){
        TemporaryPlayerStorage.addPlayer("tempUser",tempuser);
        TemporaryPlayerStorage.removePlayer("tempUser");
        assertFalse(TemporaryPlayerStorage.findUsername("tempUser"));
    }

}
