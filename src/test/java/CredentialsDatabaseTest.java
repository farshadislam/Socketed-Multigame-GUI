//package org.seng.authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.CredentialsDatabase;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
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

        assertFalse(db.usernameLookup("Alice"));
        assertTrue(db.usernameLookup("AliceNew"));

        Player updated = db.findPlayerByUsername("AliceNew");
        assertEquals("alicenew", updated.getUsername());
    }

    @Test
    public void testUpdateKeyFailsIfOldNotExist() {
        db.addNewPlayer("Alice", player1);
        db.updateKey("Alice", "AliceNew");
        assertFalse(db.usernameLookup("Alice"));
    }

    @Test
    public void testUpdateKeyTrueIfKeyIsChanged() {
        db.addNewPlayer("Bob",player2);
        db.updateKey("Bob", "BobNew");
        assertTrue(db.usernameLookup("BobNew"));
    }

    @Test
    public void testSaveAndLoadDatabase() throws IOException {
        db.addNewPlayer("Alice", player1);

        // Save to temp file
        File tempFile = File.createTempFile("database_test", ".txt");
        db.saveDatabase(tempFile.getAbsolutePath());

        // Create new DB and load
        CredentialsDatabase loadedDb = new CredentialsDatabase();
        loadedDb.clearDatabase();
        loadedDb.loadDatabase(tempFile.getAbsolutePath());

        assertTrue(loadedDb.usernameLookup("Alice"));
        assertEquals("alice", loadedDb.findPlayerByUsername("Alice").getUsername());

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

    @Test
    public void testSaveDatabaseIOException() {
        // Try to write to a directory instead of a file to trigger IOException
        String directoryPath = "."; // current directory

        db.addNewPlayer("TestPlayer", new Player("TestPlayer", "test@example.com", "1234"));

        // We expect this to fail because it's trying to write to a directory
        db.saveDatabase(directoryPath); // should trigger IOException, caught and printed
    }

    @Test
    public void testLoadDatabaseIOException() {
        db.loadDatabase("nonexistent_file.csv"); // should throw IOException
        // Watch for console message: "Error reading file: nonexistent_file.csv"
    }

    @Test
    public void testLoadDatabaseNumberFormatException() {
        String filename = "invalid_numbers.csv";
        File file = new File(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write invalid numeric value ("notANumber" where an integer is expected)
            writer.write("testUser,email@example.com,password,X,"
                    + "notANumber,5,3,2,BRONZE,100,"
                    + "5,2,3,1,SILVER,150,"
                    + "10,6,4,0,GOLD,200,"
                    + "TICTACTOE,Alice,CONNECT4,Bob,CHECKERS,Charlie,TICTACTOE,David,CHECKERS,Eve");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Run the method (this will catch and print NumberFormatException internally)
        db.loadDatabase(filename);

        // Clean up the file
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testLoadDatabaseIllegalArgumentException() {
        String filename = "invalid_enum.csv";
        File file = new File(filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Invalid Rank ("NOT_A_RANK") and invalid GameType ("UNKNOWN_GAME")
            writer.write("testUser,email@example.com,password,X,"
                    + "10,5,3,2,NOT_A_RANK,100,"      // invalid connect4 rank
                    + "5,2,3,1,SILVER,150,"            // valid checkers
                    + "10,6,4,0,GOLD,200,"             // valid tictactoe
                    + "UNKNOWN_GAME,Alice,CONNECT4,Bob,CHECKERS,Charlie,TICTACTOE,David,CHECKERS,Eve"); // invalid game type
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Run the method (this will print "Error parsing rank or game type in line:" to console)
        db.loadDatabase(filename);

        // Clean up the test file
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testAddNewPlayerReturnsFalseIfUsernameExists() {
        CredentialsDatabase db = new CredentialsDatabase();

        // Create a Player and add it
        Player player1 = new Player("Alice", "alice@example.com", "password123");
        db.addNewPlayer("Alice", player1);  // First time: should succeed

        // Try to add another player with the same username (case-insensitive)
        Player player2 = new Player("ALICE", "alice2@example.com", "newpassword");
        boolean result = db.addNewPlayer("ALICE", player2);  // Should fail (false)

        assertFalse(result, "Should return false if username already exists (case-insensitive)");
    }

    @Test
    public void testUpdateKeyFailsWhenOldUsernameDoesNotExist() {
        CredentialsDatabase db = new CredentialsDatabase();

        // Make sure database is empty or does not contain "ghost"
        db.updateKey("ghost", "newUser");

        // The new username should not exist either
        assertFalse(db.usernameLookup("newUser"), "New username should not be added if old one doesn't exist");
    }

    @Test
    public void testUpdateKeyFailsWhenNewUsernameAlreadyExists() {
        CredentialsDatabase db = new CredentialsDatabase();

        Player oldPlayer = new Player("Alice", "alice@example.com", "pass123");
        Player conflictingPlayer = new Player("Bob", "bob@example.com", "pass456");

        db.addNewPlayer("Alice", oldPlayer);
        db.addNewPlayer("Bob", conflictingPlayer);

        db.updateKey("Alice", "Bob");  // Bob already exists, should not overwrite

        assertTrue(db.usernameLookup("Alice"), "Old username should remain unchanged");
        //assertEquals("Alice", db.usernameLookup("alice").getUsername());

        assertTrue(db.usernameLookup("Bob"), "New username already exists and should be untouched");
        //assertEquals("Bob", db.getPlayer("bob").getUsername());
    }

    @Test
    public void testLoadDatabaseSkipsEmptyAndWhitespaceLines() throws IOException {
        Path tempFile = Files.createTempFile("empty_lines_test", ".csv");

        Files.write(tempFile, String.join(System.lineSeparator(),
                "",                             // empty line
                "   ",                          // whitespace-only line
                "John,john@example.com,pass123,X," + // valid line with minimum mock fields
                        "0,0,0,0,BRONZE,100," +
                        "0,0,0,0,BRONZE,100," +
                        "0,0,0,0,BRONZE,100," +
                        "TIC_TAC_TOE,WIN," +
                        "TIC_TAC_TOE,LOSS," +
                        "TIC_TAC_TOE,TIE," +
                        "CONNECT4,WIN," +
                        "CHECKERS,LOSS"
        ).getBytes());

        db.loadDatabase(tempFile.toString());

        Files.deleteIfExists(tempFile);
    }






}
