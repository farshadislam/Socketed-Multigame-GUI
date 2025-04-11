//package org.seng.authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.*;

import java.io.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class CredentialsDatabaseTest {
    private CredentialsDatabase db;
    private Player player1, player2;

    @BeforeEach
    public void setUp() {
        db = new CredentialsDatabase();
        db.clearDatabase();

        player1 = new Player("Alice", "alice@example.com", "password123");
        player1.setSymbol('X');

        player2 = new Player("Bob", "bob@example.com", "secure456");
        player2.setSymbol('O');
    }

    @Test
    public void testAddNewPlayer() {
        assertTrue(db.addNewPlayer("Alice", player1));
        assertEquals(player1, db.findPlayerByUsername("Alice"));
    }

    @Test
    public void testUsernameLookup() {
        db.addNewPlayer("Alice", player1);
        assertTrue(db.usernameLookup("Alice"));
        assertFalse(db.usernameLookup("Unknown"));
    }

    @Test
    public void testDeleteExistingPlayer() {
        db.addNewPlayer("Bob", player2);
        assertTrue(db.deleteExistingPlayer("Bob"));
        assertFalse(db.deleteExistingPlayer("Bob")); // already deleted
    }

    @Test
    public void testFindPlayerByUsername() {
        db.addNewPlayer("Alice", player1);
        Player found = db.findPlayerByUsername("alice");
        assertNotNull(found);
        assertEquals("alice", found.getUsername());
        assertNull(db.findPlayerByUsername("charlie"));
    }

    @Test
    public void testEmailTaken() {
        db.addNewPlayer("Bob", player2);
        assertTrue(db.emailTaken("bob@example.com"));
        assertFalse(db.emailTaken("not@used.com"));
    }

    @Test
    public void testClearDatabase() {
        db.addNewPlayer("Alice", player1);
        db.clearDatabase();
        assertFalse(db.usernameLookup("Alice"));
    }

    @Test
    public void testUpdateKey() {
        db.addNewPlayer("Alice", player1);
        db.updateKey("Alice", "AliceNew");

        //assertTrue(db.usernameLookup("Alice"));
        assertTrue(db.usernameLookup("AliceNew"));

        Player updated = db.findPlayerByUsername("AliceNew");
        assertEquals("alicenew", updated.getUsername());
    }

    @Test
    public void testUpdateKeyFailsIfOldNotExist() {
        db.updateKey("NonExistent", "NewUsername");
        assertFalse(db.usernameLookup("NewUsername"));
    }

    @Test
    public void testUpdateKeyFailsIfNewExists() {
        db.addNewPlayer("Alice", player1);
        db.addNewPlayer("Bob", player2);
        db.updateKey("Alice", "Bob"); // should fail

        assertTrue(db.usernameLookup("Alice"));
        assertTrue(db.usernameLookup("Bob"));
    }

    @Test
    public void testSaveAndLoadDatabase() throws IOException {
        db.addNewPlayer("Alice", player1);

        // Save to temp file
        File tempFile = File.createTempFile("test", ".txt");
        db.saveDatabase(tempFile.getAbsolutePath());

        // Create new DB and load
        CredentialsDatabase loadedDb = new CredentialsDatabase();
        loadedDb.clearDatabase();
        loadedDb.loadDatabase(tempFile.getAbsolutePath());

        assertTrue(loadedDb.usernameLookup("Alice"));
        assertEquals("Alice", loadedDb.findPlayerByUsername("Alice").getUsername());

        // Clean up temp file
        tempFile.delete();
    }

    @Test
    public void testLoadDatabaseWithCorruptLine() throws IOException {
        File corruptFile = File.createTempFile("corrupt", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(corruptFile))) {
            writer.write("Incomplete,line,only,has,few,fields\n");
        }

        CredentialsDatabase dbCorrupt = new CredentialsDatabase();
        dbCorrupt.clearDatabase();
        dbCorrupt.loadDatabase(corruptFile.getAbsolutePath());

        // Nothing should be loaded
        assertEquals(0, dbCorrupt.getPlayerCredentials().size());

        corruptFile.delete();
    }
}
