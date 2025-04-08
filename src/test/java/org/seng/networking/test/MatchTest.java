//package test.java.org.seng.networking.test;
//
//import main.java.org.seng.networking.Match;
//import main.java.org.seng.networking.Player;
//import main.java.org.seng.networking.leaderboard_matchmaking.GameType;
//import main.java.org.seng.gamelogic.connectfour.ConnectFourBoard;
//
//import main.java.org.seng.gamelogic.tictactoe.TicTacToeMove;
//import main.java.org.seng.gamelogic.connectfour.ConnectFourMove;
//import main.java.org.seng.gamelogic.checkers.CheckersMove;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class MatchTest {
//
//    private Player player1;
//    private Player player2;
//
//    @BeforeEach
//    void setup() {
//        player1 = new Player("Alice", "1"); // Both arguments must be Strings
//        player2 = new Player("Bob", "2");
//    }
//
//
//    @Test
//    void testMatchCreation() {
//        Match match = new Match(player1, player2, GameType.TICTACTOE);
//
//        assertNotNull(match.getMatchID());
//        assertEquals(player1, match.getPlayer1());
//        assertEquals(player2, match.getPlayer2());
//        assertEquals(GameType.TICTACTOE, match.getGameType());
//        assertTrue(match.isReady());
//    }
//
//    @Test
//    void testMarkAsNotReady() {
//        Match match = new Match(player1, player2, GameType.TICTACTOE);
//        match.markAsNotReady();
//        assertFalse(match.isReady());
//    }
//
//    @Test
//    void testAddAndGetAvailableMatch() {
//        Match match = new Match(player1, player2, GameType.CONNECT4);
//        Match.addAvailableMatch(match);
//        Match retrieved = Match.getAvailableMatch();
//        assertEquals(match, retrieved);
//    }
//
//    @Test
//    void testMakeTicTacToeMoveValid() {
//        Match match = new Match(player1, player2, GameType.TICTACTOE);
//        TicTacToeMove move = new TicTacToeMove(0, 0, 'X');
//
//        boolean result = match.makeTicTacToeMove(move);
//        assertTrue(result);
//    }
//
//    @Test
//    void testMakeTicTacToeMoveInvalidType() {
//        Match match = new Match(player1, player2, GameType.CHECKERS);
//        TicTacToeMove move = new TicTacToeMove(0, 0, 'X');
//
//        boolean result = match.makeTicTacToeMove(move);
//        assertFalse(result);
//    }
//
////    @Test
////    void testMakeConnectFourMoveValid() {
////        Match match = new Match(player1, player2, GameType.CONNECT4);
////        ConnectFourMove move = new ConnectFourMove(0, ConnectFourBoard.Chip.RED);
////
////        boolean result = match.makeConnectMove(move);
////        assertTrue(result);
////    }
//
////    @Test
////    void testMakeConnectFourMoveWrongType(){
////        Match match = new Match(player1, player2, GameType.TICTACTOE);
////        ConnectFourMove move = new ConnectFourMove(0, ConnectFourBoard.Chip.RED);
////
////        boolean result = match.makeConnectMove(move);
////        assertFalse(result);
////    }
//
////    @Test
////    void testMakeCheckersMoveValid() {
////        Match match = new Match(player1, player2, GameType.CHECKERS);
////        // Assuming board is initialized and this is a valid starting move
////        CheckersMove move = new CheckersMove(2, 1, 3, 0);
////
////        boolean result = match.makeCheckersMove(move);
////        assertTrue(result); // May need mocking if board state makes this fail
////    }
//
////    @Test
////    void testMakeCheckersMoveWrongType() {
////        Match match = new Match(player1, player2, GameType.CONNECT4);
////        CheckersMove move = new CheckersMove(2, 1, 3, 0);
////
////        boolean result = match.makeCheckersMove(move);
////        assertFalse(result);
////    }
//
//    @Test
//    void testCheckGameOver_TicTacToeImmediatelyAfterStart() {
//        Match match = new Match(player1, player2, GameType.TICTACTOE);
//        assertFalse(match.checkGameOver());
//    }
//
//    @Test
//    void testCheckGameOver_UnsupportedType() {
//        Match match = new Match(player1, player2, null);
//        assertFalse(match.checkGameOver());
//    }
//}
