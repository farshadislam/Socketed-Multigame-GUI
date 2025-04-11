import org.junit.jupiter.api.*;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialsDatabaseTest {

    private CredentialsDatabase db;
    private Player player1, player2;

    @BeforeEach
    public void setup() {
        db = new CredentialsDatabase();
        db.clearDatabase();

        player1 = new Player("UserOne", "one@example.com", "pass123");
        player1.setSymbol('X');

        player2 = new Player("UserTwo", "two@example.com", "pass456");
        player2.setSymbol('O');
    }

    @Test
    public void testAddNewPlayerNormal() {
        assertTrue(db.addNewPlayer("UserOne", player1));
        assertFalse(db.addNewPlayer("UserOne", player1)); // Duplicate username
    }

    @Test
    public void testAddNewPlayerCaseInsensitive() {
        db.addNewPlayer("userone", player1);
        assertFalse(db.addNewPlayer("USERONE", player1));
    }

    @Test
    public void testAddNewPlayerNullOrEmpty() {
        assertFalse(db.addNewPlayer(null, player1));
        assertFalse(db.addNewPlayer(null, player1));
    }

    @Test
    public void testUsernameLookupVariants() {
        db.addNewPlayer("UserOne", player1);
        assertTrue(db.usernameLookup("userone"));
        assertTrue(db.usernameLookup("USERONE"));
        assertFalse(db.usernameLookup("nonexistent"));
    }

    @Test
    public void testDeletePlayerTwice() {
        db.addNewPlayer("UserOne", player1);
        assertTrue(db.deleteExistingPlayer("UserOne"));
        assertFalse(db.deleteExistingPlayer("UserOne"));
    }

    @Test
    public void testFindPlayerWithDifferentCases() {
        db.addNewPlayer("UserOne", player1);
        assertNotNull(db.findPlayerByUsername("userone"));
        assertNull(db.findPlayerByUsername("nonexistent"));
    }

    @Test
    public void testEmailTakenCaseInsensitive() {
        db.addNewPlayer("UserOne", player1);
        Player newPlayer = new Player("DifferentUser", "ONE@example.com", "pass");
        assertTrue(db.emailTaken("ONE@EXAMPLE.com"));
        assertFalse(db.emailTaken("unused@example.com"));
    }

    @Test
    public void testEmailTakenWithNullEmail() {
        assertFalse(db.emailTaken(null));
    }

    @Test
    public void testClearDatabase() {
        db.addNewPlayer("UserOne", player1);
        db.clearDatabase();
        assertFalse(db.usernameLookup("UserOne"));
    }

    @Test
    public void testUpdateKeySuccess() {
        db.addNewPlayer("OldUser", player1);
        db.updateKey("OldUser", "NewUser");
        assertTrue(db.usernameLookup("NewUser"));
    }

    @Test
    public void testUpdateKeyFailsIfOldMissingOrNewExists() {
        db.addNewPlayer("UserOne", player1);
        db.addNewPlayer("UserTwo", player2);

        db.updateKey("nonexistent", "newuser"); // Should fail silently
        db.updateKey("UserOne", "UserTwo"); // Cannot override existing
        assertTrue(db.usernameLookup("UserOne"));
        assertTrue(db.usernameLookup("UserTwo"));
    }

    @Test
    public void testSaveAndLoadPreservesData() {
        db.addNewPlayer("UserOne", player1);
        db.saveDatabase();

        CredentialsDatabase newDb = new CredentialsDatabase();
        assertTrue(newDb.usernameLookup("UserOne"));
        assertEquals("UserOne", newDb.findPlayerByUsername("UserOne").getUsername());

        new File("output.txt").delete();
    }

    @Test
    public void testReAddDeletedPlayer() {
        db.addNewPlayer("UserOne", player1);
        db.deleteExistingPlayer("UserOne");
        assertTrue(db.addNewPlayer("UserOne", player1));
    }

    @Test
    public void testLoadDatabaseWithCorruptedLine() throws IOException {
        String corruptedLine = "user,bad@line,pass,X,1,1,1,1,BRONZE,100,1,1,1,1,BRONZE,100,1,1,1,1,BRONZE,100,TIC_TAC_TOE,op1,TIC_TAC_TOE,op2,TIC_TAC_TOE,op3"; // missing last 4 fields

        File file = new File("output.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(corruptedLine); writer.newLine(); // corrupted line
        writer.write("UserOne,one@example.com,password,X,1,1,1,1,BRONZE,100,1,1,1,1,BRONZE,100,1,1,1,1,BRONZE,100,TIC_TAC_TOE,op1,TIC_TAC_TOE,op2,TIC_TAC_TOE,op3,TIC_TAC_TOE,op4,TIC_TAC_TOE,op5"); // valid line
        writer.close();

        CredentialsDatabase corruptTestDb = new CredentialsDatabase();
        assertTrue(corruptTestDb.usernameLookup("userone"));

        file.delete();
    }

    @Test
    public void testMultiplePlayerPersistence() {
        db.addNewPlayer("UserOne", player1);
        db.addNewPlayer("UserTwo", player2);
        db.saveDatabase();

        CredentialsDatabase reloadedDb = new CredentialsDatabase();
        assertTrue(reloadedDb.usernameLookup("userone"));
        assertTrue(reloadedDb.usernameLookup("usertwo"));
        assertEquals("UserTwo", reloadedDb.findPlayerByUsername("UserTwo").getUsername());

        new File("output.txt").delete();
    }
}
