import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.authentication.Player;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player; // create player object for testing

    // initialize a player before each unit test
    @BeforeEach
    public void initializePlayer(){
        player = new Player("newUser", "newUser@gmail.com", "passWORD");
    }

    // all values match
    @Test
    public void testGeneral1(){
        assertEquals("newuser", player.getUsername());
        assertEquals("newuser@gmail.com", player.getEmail());
        assertEquals("passWORD", player.getPassword());
    }

    // wrong username
    @Test
    public void testGeneral2(){
        assertNotEquals("oldUser", player.getUsername());
        assertEquals("newuser@gmail.com", player.getEmail());
        assertEquals("passWORD", player.getPassword());
    }

    // wrong email
    @Test
    public void testGeneral3(){
        assertEquals("newuser", player.getUsername());
        assertNotEquals("olduser@gmail.com", player.getEmail());
        assertEquals("passWORD", player.getPassword());
    }

    // wrong password
    @Test
    public void testGeneral4(){
        assertEquals("newuser", player.getUsername());
        assertEquals("newuser@gmail.com", player.getEmail());
        assertNotEquals("pass1234567", player.getPassword());
    }

    // empty username
    @Test
    public void testGeneral5(){
        Player p2 = new Player("", "newUser@gmail.com", "passWORD");
        assertEquals("", p2.getUsername());
    }

    // whitespace username
    @Test
    public void testGeneral7(){
        Player p2 = new Player(" ", "newUser@gmail.com", "passWORD");
        assertEquals(" ", p2.getUsername());
    }

    // invalid password
    @Test
    public void testGeneral8(){
        Player p2 = new Player("newUser", "newUser@gmail.com", "buzz");
        assertEquals("buzz", p2.getPassword());
    }

    // invalid email
    @Test
    public void testGeneral9(){
        Player p2 = new Player("newUser", "@gmail.com", "buzz");
        assertEquals("@gmail.com", p2.getEmail());
    }

    // same username, different emails and passwords
    @Test
    public void testEquals1(){
        Player p2 = new Player("newUser", "java123@gmail.com", "password123");
        assertNotEquals(player, p2);
    }

    // same username, same emails, different passwords
    @Test
    public void testEquals2(){
        Player p2 = new Player("newUser", "newUser@gmail.com", "password123");
        assertEquals(player, p2);
    }

    // same username, same emails and passwords
    @Test
    public void testEquals3(){
        Player p2 = new Player("newUser", "newUser@gmail.com", "passWORD");
        assertEquals(player, p2);
    }

    // one username valid, other empty
    @Test
    public void testEquals5(){
        Player p3 = new Player("", "oldUser@gmail.com", "password");
        assertNotEquals(player, p3);
    }

    // one username valid, other whitespace
    @Test
    public void testEquals6(){
        Player p3 = new Player(" ", "oldUser@gmail.com", "password");
        assertNotEquals(player, p3);
    }

    // different classes being compared
    @Test
    public void testEquals7() {
        int num = 5;
        assertNotEquals(player, num);
    }

    // same player
    @Test
    public void testEquals8(){
        assertEquals(player, player);
    }

    // total games played all games
    @Test
    public void testTotalGamesPlayed1(){
        player.getCheckersStats().setGamesPlayed(4);
        player.getConnect4Stats().setGamesPlayed(2);
        player.getTicTacToeStats().setGamesPlayed(6);

        assertEquals(12, player.getTotalGamesPlayed());
    }

    // total games played all games
    @Test
    public void testTotalGamesPlayed2(){
        player.getCheckersStats().setGamesPlayed(4);
        player.getConnect4Stats().setGamesPlayed(2);
        player.getTicTacToeStats().setGamesPlayed(6);

        assertNotEquals(0, player.getTotalGamesPlayed());
    }

    // total games played only checkers
    @Test
    public void testTotalGamesPlayed3(){
        player.getCheckersStats().setGamesPlayed(4);

        assertEquals(4, player.getTotalGamesPlayed());
    }

    // total games played only tictactoe
    @Test
    public void testTotalGamesPlayed4(){
        player.getCheckersStats().setGamesPlayed(2);

        assertEquals(2, player.getTotalGamesPlayed());
    }

    // total games played only connect4
    @Test
    public void testTotalGamesPlayed5(){
        player.getCheckersStats().setGamesPlayed(6);
        assertEquals(6, player.getTotalGamesPlayed());
    }

    // total games won
    @Test
    public void testWins1(){
        player.getCheckersStats().setWins(4);
        player.getConnect4Stats().setWins(2);
        player.getTicTacToeStats().setWins(6);

        assertEquals(12, player.getTotalWins());
    }

    // total games won
    @Test
    public void testWins2(){
        player.getCheckersStats().setWins(4);
        player.getConnect4Stats().setWins(2);
        player.getTicTacToeStats().setWins(6);

        assertNotEquals(0, player.getTotalWins());
    }

    // total games won only checkers
    @Test
    public void testWins3(){
        player.getCheckersStats().setWins(4);
        assertEquals(4, player.getTotalWins());
    }

    // total games won only tictactoe
    @Test
    public void testWins4(){
        player.getTicTacToeStats().setWins(2);
        assertEquals(2, player.getTotalWins());
    }

    // total games won only connect4
    @Test
    public void testWins5(){
        player.getConnect4Stats().setWins(6);
        assertEquals(6, player.getTotalWins());
    }

    // total games lost all games
    @Test
    public void testTotalLosses1(){
        player.getCheckersStats().setLosses(4);
        player.getConnect4Stats().setLosses(2);
        player.getTicTacToeStats().setLosses(6);

        assertEquals(12, player.getTotalLosses());
    }

    // total games lost all games
    @Test
    public void testTotalLosses2(){
        player.getCheckersStats().setLosses(4);
        player.getConnect4Stats().setLosses(2);
        player.getTicTacToeStats().setLosses(6);

        assertNotEquals(0, player.getTotalLosses());
    }

    // total games lost checkers
    @Test
    public void testTotalLosses3(){
        player.getCheckersStats().setLosses(4);

        assertEquals(4, player.getTotalLosses());
    }

    // total games lost tictactoe
    @Test
    public void testTotalLosses4(){
        player.getTicTacToeStats().setLosses(2);

        assertEquals(2, player.getTotalLosses());
    }

    // total games lost connect4
    @Test
    public void testTotalLosses5(){
        player.getConnect4Stats().setLosses(6);

        assertEquals(6, player.getTotalLosses());
    }

    // total games tied all games
    @Test
    public void testTotalTied1(){
        player.getCheckersStats().setTies(4);
        player.getConnect4Stats().setTies(2);
        player.getTicTacToeStats().setTies(6);

        assertEquals(12, player.getTotalTies());
    }

    // total games tied all games
    @Test
    public void testTotalTied2(){
        player.getCheckersStats().setTies(4);
        player.getConnect4Stats().setTies(2);
        player.getTicTacToeStats().setTies(6);

        assertNotEquals(0, player.getTotalTies());
    }

    // total games tied checkers
    @Test
    public void testTotalTies3(){
        player.getCheckersStats().setTies(4);

        assertEquals(4, player.getTotalTies());
    }

    // total games tied tictactoe
    @Test
    public void testTotalTied4(){
        player.getTicTacToeStats().setTies(2);

        assertEquals(2, player.getTotalTies());
    }

    // total games tied connect4
    @Test
    public void testTotalTies5(){
        player.getConnect4Stats().setTies(6);

        assertEquals(6, player.getTotalTies());
    }

    // verification code
    @Test
    public void testVerificationCode1() {
        String code = "12345";
        player.setVerificationCode(code);
        assertEquals(code, player.getVerificationCode());
    }

}

