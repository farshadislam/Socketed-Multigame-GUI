package Authentication_test;

import org.junit.jupiter.api.*;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.*;

import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class CredentialsDatabaseTest {

    private CredentialsDatabase db;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        db = new CredentialsDatabase();

        player1 = new Player("Hamna", "hamna@gmail.com", "password1234");
        player1.setSymbol('B');

        player2 = new Player("Maham", "maham@gmail.com", "maham1234");
        player2.setSymbol('R');

        player3 = new Player("Wissal", "wissal@gmai.com", "wissal1234");
        player3.setSymbol('Y');
    }

    @Test
    void testAddNewPlayer() {
        boolean result = db.addNewPlayer("Hamna", player1);
        assertTrue(result);
        assertEquals(player1, db.findPlayerByUsername("Hamna"));
    }

    @Test
    void testAddDuplicatePlayerFails() {
        db.addNewPlayer("Maham", player2);
        boolean result = db.addNewPlayer("Maham", player2);
        assertFalse(result);
    }

    @Test
    void testUsernameLookup() {
        db.addNewPlayer("Wissal", player3);
        assertTrue(db.usernameLookup("Wissal"));
        assertFalse(db.usernameLookup("Alice"));
    }

    @Test
    void testDeleteExistingPlayer() {
        db.addNewPlayer("Hamna", player1);
        assertTrue(db.deleteExistingPlayer("Hamna"));
        assertFalse(db.usernameLookup("Hamna"));
    }

    @Test
    void testDeleteNonExistentPlayerFails() {
        assertFalse(db.deleteExistingPlayer("ghostUser"));
    }

//    @Test
//    void testFindPlayerByUsername() {
//        db.addNewPlayer("testUser", testPlayer);
//        Player result = db.findPlayerByUsername("testUser");
//        assertNotNull(result);
//        assertEquals("test@example.com", result.getEmail());
//    }
//
//    @Test
//    void testEmailTaken() {
//        db.addNewPlayer("testUser", testPlayer);
//        assertTrue(db.emailTaken("test@example.com"));
//        assertFalse(db.emailTaken("new@example.com"));
//    }
//
//    @Test
//    void testSaveAndLoadDatabase() throws IOException {
//        // Add player and save
//        db.addNewPlayer("testUser", testPlayer);
//        String tempFile = "tempDatabase.txt";
//        FileWriter writer = new FileWriter(tempFile);
//        db.saveDatabase();
//
//        // Create a new database and load from file
//        CredentialsDatabase newDb = new CredentialsDatabase();
//        newDb.loadDatabase(tempFile);
//        Player loaded = newDb.findPlayerByUsername("testUser");
//
//        assertNotNull(loaded);
//        assertEquals("test@example.com", loaded.getEmail());
//
//        // Cleanup
//        Files.deleteIfExists(Paths.get(tempFile));
//    }
}
