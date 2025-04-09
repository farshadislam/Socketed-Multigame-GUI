package leaderboard_matchmaking.matchmaking;


import org.seng.leaderboard_matchmaking.matchmaking.QueueMatchMaker;
import org.seng.leaderboard_matchmaking.*;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.GeneralStats;
import org.seng.leaderboard_matchmaking.matchmaking.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;



public class QueueMatchMakerTest {

    private QueueMatchMaker matcher;

    @BeforeEach
    public void setUp() {
        matcher = new QueueMatchMaker();
    }

    // ======================================================
    // ==== 1. getPairKey Tests (6 Cases for 100% Coverage)
    // ======================================================

    @Test
    public void testGetPairKey_Pair0_Rank1() {
        assertEquals(1, invokeGetPairKey(1, 0));
    }

    @Test
    public void testGetPairKey_Pair1_Rank1() {
        assertEquals(8, invokeGetPairKey(1, 1));
    }

    @Test
    public void testGetPairKey_Pair0_Rank7() {
        assertEquals(7, invokeGetPairKey(7, 0));
    }

    @Test
    public void testGetPairKey_Pair1_Rank7() {
        assertEquals(14, invokeGetPairKey(7, 1));
    }

    @Test
    public void testGetPairKey_Pair0_Rank3() {
        assertEquals(3, invokeGetPairKey(3, 0));
    }

    @Test
    public void testGetPairKey_Pair1_Rank3() {
        assertEquals(10, invokeGetPairKey(3, 1));
    }

    private int invokeGetPairKey(int rank, int pairIndex) {
        try {
            var method = QueueMatchMaker.class.getDeclaredMethod("getPairKey", int.class, int.class);
            method.setAccessible(true);
            return (int) method.invoke(null, rank, pairIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ======================================================
    // ==== 2. Constructor Test (basic structure validation)
    // ======================================================

    @Test
    public void testConstructor_InitializesAllGameQueuesCorrectly() {
        for (GameType game : GameType.values()) {
            for (int rank = 1; rank <= 7; rank++) {
                for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
                    int key = rank + (pairIndex * 7);
                    Queue<Player>[] pair = (Queue<Player>[]) matcher.getQueuesPerGame().get(game).get(key);
                    assertNotNull(pair, "Queue for " + game + " Rank " + rank + " Pair " + pairIndex + " should exist.");
                    assertEquals(2, pair.length, "Each pair should have exactly 2 queues.");
                }
            }
        }
    }

    // ===============================
    // ==== 3. addPlayerToQueue() ====
    // ===============================
    

    @Test
    public void testAddPlayerToQueue_PrefersLighterQueueWithinPair() {
        // Queue A has 1 player, Queue B is empty
        Player p1 = createMockPlayer("dave", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1);
        pair[0].enqueue(p1); // Manually insert into Queue A

        // Add new player with same rank and game
        Player p2 = createMockPlayer("ella", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        matcher.addPlayerToQueue(p2, GameType.TICTACTOE);

        // Player should go into the lighter queue (Queue B)
        assertTrue(pair[1].contains(p2), "Player should be added to the lighter queue (Queue B).");
    }

    @Test
    public void testAddPlayerToQueue_TiesQueueSizeChoosesRandomly() {
        // Add a new player when both queue A and B are empty
        Player p1 = createMockPlayer("frank", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        matcher.addPlayerToQueue(p1, GameType.TICTACTOE);

        // Count how many queues the player appears in (should be exactly one)
        int count = 0;
        Queue<Player>[] pair0 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1);
        Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(8);

        if (pair0[0].contains(p1)) count++;
        if (pair0[1].contains(p1)) count++;
        if (pair1[0].contains(p1)) count++;
        if (pair1[1].contains(p1)) count++;

        assertEquals(1, count, "Player should appear in exactly one queue.");
    }

    @Test
    public void testAddPlayerToQueue_StartsPairHopper() throws InterruptedException {
        // Add a player and allow time for the hopping thread to begin
        Player p = createMockPlayer("gary", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        matcher.addPlayerToQueue(p, GameType.TICTACTOE);
        Thread.sleep(100); // Let the thread run briefly

        // Check that player still exists in some queue for that rank/game
        boolean exists = false;
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1 + 7 * pairIndex);
            if (pair[0].contains(p) || pair[1].contains(p)) exists = true;
        }

        assertTrue(exists, "Player should still be in matchmaking queues after being added.");
    }

    @Test
    public void testAddPlayerToQueue_DifferentGamesDifferentQueues() {
        // Create players for two different games
        Player player1 = createMockPlayer("hannah", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        Player player2 = createMockPlayer("ian", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);

        matcher.addPlayerToQueue(player1, GameType.TICTACTOE);
        matcher.addPlayerToQueue(player2, GameType.CONNECT4);

        boolean p1Found = false, p2Found = false;

        // Check TTT queues
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            Queue<Player>[] pairT = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1 + 7 * pairIndex);
            if (pairT[0].contains(player1) || pairT[1].contains(player1)) p1Found = true;

            Queue<Player>[] pairC = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(1 + 7 * pairIndex);
            if (pairC[0].contains(player2) || pairC[1].contains(player2)) p2Found = true;
        }

        assertTrue(p1Found && p2Found, "Each player should be in their respective game's queues.");
    }

    @Test
    public void testPlayerGoesToCorrectGameQueueStructure() {
        Player player = createMockPlayer("platinumC4", Rank.PLATINUM, Rank.SILVER, Rank.SILVER);
        matcher.addPlayerToQueue(player, GameType.CONNECT4);

        int rank = Rank.PLATINUM.ordinal() + 1;
        boolean found = false;

        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            int key = rank + pairIndex * 7;
            Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(key);
            if (pair[0].contains(player) || pair[1].contains(player)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Player should be placed in Connect4 queues for PLATINUM rank.");
    }

    @Test
    public void testPlayersWithDifferentRanksGoToDifferentQueueStructures() {
        Player p1 = createMockPlayer("silverTTT", Rank.SILVER, Rank.SILVER, Rank.SILVER);
        Player p2 = createMockPlayer("grandTTT", Rank.GRANDMASTER, Rank.GRANDMASTER, Rank.GRANDMASTER);

        matcher.addPlayerToQueue(p1, GameType.TICTACTOE);
        matcher.addPlayerToQueue(p2, GameType.TICTACTOE);

        int rank1 = Rank.SILVER.ordinal() + 1;
        int rank2 = Rank.GRANDMASTER.ordinal() + 1;

        boolean p1Found = false, p2Found = false;

        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(rank1 + 7 * pairIndex);
            Queue<Player>[] pair2 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(rank2 + 7 * pairIndex);

            if (pair1[0].contains(p1) || pair1[1].contains(p1)) p1Found = true;
            if (pair2[0].contains(p2) || pair2[1].contains(p2)) p2Found = true;
        }

        assertTrue(p1Found, "Player 1 should be in queue for SILVER.");
        assertTrue(p2Found, "Player 2 should be in queue for GRANDMASTER.");
    }

    @Test
    public void testEachGameMaintainsSeparateQueueMaps() {
        Player c4 = createMockPlayer("C4Master", Rank.MASTER, Rank.SILVER, Rank.SILVER);
        Player ttt = createMockPlayer("TTTBronze", Rank.SILVER, Rank.BRONZE, Rank.SILVER);
        Player chk = createMockPlayer("ChkGold", Rank.SILVER, Rank.SILVER, Rank.GOLD);

        matcher.addPlayerToQueue(c4, GameType.CONNECT4);
        matcher.addPlayerToQueue(ttt, GameType.TICTACTOE);
        matcher.addPlayerToQueue(chk, GameType.CHECKERS);

        int c4Rank = Rank.MASTER.ordinal() + 1;
        int tttRank = Rank.BRONZE.ordinal() + 1;
        int chkRank = Rank.GOLD.ordinal() + 1;

        boolean c4Found = false, tttFound = false, chkFound = false;

        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            int keyC4 = c4Rank + pairIndex * 7;
            int keyTTT = tttRank + pairIndex * 7;
            int keyChk = chkRank + pairIndex * 7;

            if (matcher.getQueuesPerGame().get(GameType.CONNECT4).get(keyC4)[0].contains(c4) ||
                    matcher.getQueuesPerGame().get(GameType.CONNECT4).get(keyC4)[1].contains(c4)) c4Found = true;

            if (matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(keyTTT)[0].contains(ttt) ||
                    matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(keyTTT)[1].contains(ttt)) tttFound = true;

            if (matcher.getQueuesPerGame().get(GameType.CHECKERS).get(keyChk)[0].contains(chk) ||
                    matcher.getQueuesPerGame().get(GameType.CHECKERS).get(keyChk)[1].contains(chk)) chkFound = true;
        }

        assertTrue(c4Found, "C4 player should be in correct queues.");
        assertTrue(tttFound, "TTT player should be in correct queues.");
        assertTrue(chkFound, "Checkers player should be in correct queues.");
    }

    // ===============================
    // ==== 3. removePlayer() ====
    // ===============================

    @Test
    public void testRemovePlayer_PlayerInOneQueue_ShouldBeRemoved() {
        Player p = createMockPlayer("remove1", Rank.SILVER, Rank.SILVER, Rank.SILVER);
        matcher.addPlayerToQueue(p, GameType.TICTACTOE);

        matcher.removePlayer(p, GameType.TICTACTOE);

        // Player should no longer be in any queue
        boolean stillExists = false;
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(2 + 7 * pairIndex);
            if (pair[0].contains(p) || pair[1].contains(p)) stillExists = true;
        }

        assertFalse(stillExists, "Player should be removed from all queues.");
    }

    @Test
    public void testRemovePlayer_PlayerWasNeverQueued_NoError() {
        Player p = createMockPlayer("notQueued", Rank.GOLD, Rank.GOLD, Rank.GOLD);

        // Try to remove without ever adding
        assertDoesNotThrow(() -> matcher.removePlayer(p, GameType.CONNECT4));
    }

    @Test
    public void testRemovePlayer_PlayerQueuedInPair0Only_RemovesFromCorrectPair() {
        Player p = createMockPlayer("pair0", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);

        // Manually add to Pair 0, Queue A
        Queue<Player>[] pair0 = matcher.getQueuesPerGame().get(GameType.CHECKERS).get(1);
        pair0[0].enqueue(p);

        matcher.removePlayer(p, GameType.CHECKERS);

        assertFalse(pair0[0].contains(p), "Player should be removed from Queue A of Pair 0.");
    }

    @Test
    public void testRemovePlayer_PlayerQueuedInPair1Only_RemovesFromCorrectPair() {
        Player p = createMockPlayer("pair1", Rank.DIAMOND, Rank.DIAMOND, Rank.DIAMOND);

        // Manually add to Pair 1, Queue B
        int key = 5 + 7; // Rank 5 = Diamond, pairIndex 1
        Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(key);
        pair1[1].enqueue(p);

        matcher.removePlayer(p, GameType.TICTACTOE);

        assertFalse(pair1[1].contains(p), "Player should be removed from Queue B of Pair 1.");
    }

    @Test
    public void testRemovePlayer_PlayerInBothQueues_PartiallyRemoved() {
        Player p = createMockPlayer("doubleAdd", Rank.MASTER, Rank.MASTER, Rank.MASTER);

        int key = 6; // Rank 6 = Master, pairIndex 0
        Queue<Player>[] pair0 = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(key);
        pair0[0].enqueue(p);
        pair0[1].enqueue(p);

        matcher.removePlayer(p, GameType.CONNECT4);

        assertFalse(pair0[0].contains(p), "Player should be removed from Queue A.");
        assertFalse(pair0[1].contains(p), "Player should be removed from Queue B.");
    }

    @Test
    public void testRemovePlayer_DoesNotAffectOtherGames() {
        Player p = createMockPlayer("multiGame", Rank.GOLD, Rank.GOLD, Rank.GOLD);

        matcher.addPlayerToQueue(p, GameType.CONNECT4);

        // Remove from unrelated game
        matcher.removePlayer(p, GameType.TICTACTOE);

        // Should still be in Connect4
        boolean stillExists = false;
        int rank = Rank.GOLD.ordinal() + 1;
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(rank + 7 * pairIndex);
            if (pair[0].contains(p) || pair[1].contains(p)) stillExists = true;
        }

        assertTrue(stillExists, "Player should not be removed from Connect4 queues.");
    }

    @Test
    public void testRemovePlayer_OnlyInOneGame_ShouldNotAffectOtherRanks() {
        Player p = createMockPlayer("checkersOnly", Rank.SILVER, Rank.SILVER, Rank.PLATINUM);

        matcher.addPlayerToQueue(p, GameType.CHECKERS);
        matcher.removePlayer(p, GameType.CHECKERS);

        // Check they aren't removed from other games (which they never joined)
        boolean foundElsewhere = false;
        for (GameType game : GameType.values()) {
            if (game == GameType.CHECKERS) continue;
            for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
                int key = Rank.SILVER.ordinal() + 1 + pairIndex * 7;
                Queue<Player>[] pair = matcher.getQueuesPerGame().get(game).get(key);
                if (pair[0].contains(p) || pair[1].contains(p)) foundElsewhere = true;
            }
        }

        assertFalse(foundElsewhere, "Player should not appear in other games' queues.");
    }

    @Test
    public void testRemovePlayer_FullyRemovesFromAllPairsAndQueues_WithBeforeAfterChecks() {
        Player p = createMockPlayer("fullRemove", Rank.GOLD, Rank.GOLD, Rank.GOLD);
        int rank = Rank.GOLD.ordinal() + 1;

        // Manually add player to all 4 queues (2 pairs × 2 queues)
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            int key = rank + 7 * pairIndex;
            Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(key);
            pair[0].enqueue(p); // Queue A
            pair[1].enqueue(p); // Queue B

            // Assert player is present before removal
            assertTrue(pair[0].contains(p), "Player should be in Queue A of Pair " + pairIndex + " before removal.");
            assertTrue(pair[1].contains(p), "Player should be in Queue B of Pair " + pairIndex + " before removal.");
        }

        // Now remove the player
        matcher.removePlayer(p, GameType.CONNECT4);

        // Assert player is no longer in any queue
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            int key = rank + 7 * pairIndex;
            Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(key);
            assertFalse(pair[0].contains(p), "Player should be removed from Queue A of Pair " + pairIndex);
            assertFalse(pair[1].contains(p), "Player should be removed from Queue B of Pair " + pairIndex);
        }
    }

    @Test
    public void testRemovePlayer_DoesNotRemoveFromWrongRankQueues_WithBeforeAfterChecks() {
        Player p = createMockPlayer("wrongRankCheck", Rank.PLATINUM, Rank.PLATINUM, Rank.PLATINUM);
        int correctRank = Rank.PLATINUM.ordinal() + 1;
        int wrongRank = Rank.BRONZE.ordinal() + 1;

        // Add player to the WRONG rank's queues (manually)
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            int key = wrongRank + 7 * pairIndex;
            Queue<Player>[] wrongPair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(key);
            wrongPair[0].enqueue(p);
            wrongPair[1].enqueue(p);

            // Assert player is present before removal
            assertTrue(wrongPair[0].contains(p), "Player should be in wrong-rank Queue A before removal.");
            assertTrue(wrongPair[1].contains(p), "Player should be in wrong-rank Queue B before removal.");
        }

        // Attempt to remove from correct rank (PLATINUM) — should not affect wrong rank queues
        matcher.removePlayer(p, GameType.TICTACTOE);

        // Player should still be in the wrong rank queues
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            int key = wrongRank + 7 * pairIndex;
            Queue<Player>[] wrongPair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(key);
            assertTrue(wrongPair[0].contains(p), "Player in wrong-rank Queue A should remain after removal attempt.");
            assertTrue(wrongPair[1].contains(p), "Player in wrong-rank Queue B should remain after removal attempt.");
        }
    }

    // ===============================
    // == 4. startMatchmakingLoop() ==
    // ===============================


    @Test
    public void testStartMatchmakingLoop_PlayersAreMatchedAndDequeued() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();

        Player p1 = createMockPlayer("alpha", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        Player p2 = createMockPlayer("beta", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);

        int rank = Rank.BRONZE.ordinal() + 1;
        int key = rank + 0; // Pair 0

        // Get the queues for TicTacToe, rank 1 (Bronze), pair 0
        Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(key);
        Queue<Player> queueA = pair[0];
        Queue<Player> queueB = pair[1];

        // Manually enqueue each player into opposite queues
        queueA.enqueue(p1);
        queueB.enqueue(p2);

        // Confirm they're both in queue before matchmaking starts
        assertTrue(queueA.contains(p1), "Player 1 should be in Queue A before matching.");
        assertTrue(queueB.contains(p2), "Player 2 should be in Queue B before matching.");

        // Start matchmaking loop (runs every 5 seconds)
        matcher.startMatchmakingLoop();

        // Wait 6 seconds to allow at least one matchmaking cycle to execute
        Thread.sleep(6000);

        // After matchmaking, both queues should be empty if matched
        assertFalse(queueA.contains(p1), "Player 1 should be removed from Queue A after matching.");
        assertFalse(queueB.contains(p2), "Player 2 should be removed from Queue B after matching.");
    }

    @Test
    public void testMatchmakingLoop_QueueAEmpty_NoMatch() {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player p = createMockPlayer("soloB", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1);
        pair[1].enqueue(p); // Only Queue B has a player

        matcher.startMatchmakingLoop();
        waitForMatchLoop();

        assertTrue(pair[1].contains(p), "Player should remain in Queue B as no match was possible.");
    }

    @Test
    public void testMatchmakingLoop_QueueBEmpty_NoMatch() {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player p = createMockPlayer("soloA", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1);
        pair[0].enqueue(p); // Only Queue A has a player

        matcher.startMatchmakingLoop();
        waitForMatchLoop();

        assertTrue(pair[0].contains(p), "Player should remain in Queue A as no match was possible.");
    }


    public void testMatchmakingLoop_NoPlayers_DoesNotThrow() {
        QueueMatchMaker matcher = new QueueMatchMaker();
        matcher.startMatchmakingLoop();
        waitForMatchLoop();

        // No assertion here — just making sure no exceptions are thrown
        assertTrue(true);
    }

    @Test
    public void testMatchmakingLoop_OnlyCorrectRankMatched() {
        QueueMatchMaker matcher = new QueueMatchMaker();

        Player bronzeA = createMockPlayer("bronzeA", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        Player bronzeB = createMockPlayer("bronzeB", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);

        Player gold = createMockPlayer("goldA", Rank.GOLD, Rank.GOLD, Rank.GOLD);

        // Queue Bronze pair
        Queue<Player>[] bronzePair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1);
        bronzePair[0].enqueue(bronzeA);
        bronzePair[1].enqueue(bronzeB);

        // Queue Gold player in A only (no match)
        Queue<Player>[] goldPair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(3);
        goldPair[0].enqueue(gold);

        matcher.startMatchmakingLoop();
        waitForMatchLoop();

        // Bronze players matched
        assertFalse(bronzePair[0].contains(bronzeA));
        assertFalse(bronzePair[1].contains(bronzeB));

        // Gold player should remain
        assertTrue(goldPair[0].contains(gold));
    }

    @Test
    public void testMatchmakingLoop_ProcessesBothPair0AndPair1() {
        QueueMatchMaker matcher = new QueueMatchMaker();

        Player p1 = createMockPlayer("p1", Rank.SILVER, Rank.SILVER, Rank.SILVER);
        Player p2 = createMockPlayer("p2", Rank.SILVER, Rank.SILVER, Rank.SILVER);
        Player p3 = createMockPlayer("p3", Rank.SILVER, Rank.SILVER, Rank.SILVER);
        Player p4 = createMockPlayer("p4", Rank.SILVER, Rank.SILVER, Rank.SILVER);

        int rank = Rank.SILVER.ordinal() + 1;

        Queue<Player>[] pair0 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(rank);     // key = 2
        Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(rank + 7); // key = 9

        pair0[0].enqueue(p1);
        pair0[1].enqueue(p2);

        pair1[0].enqueue(p3);
        pair1[1].enqueue(p4);

        matcher.startMatchmakingLoop();
        waitForMatchLoop();

        // All should be dequeued
        assertFalse(pair0[0].contains(p1));
        assertFalse(pair0[1].contains(p2));
        assertFalse(pair1[0].contains(p3));
        assertFalse(pair1[1].contains(p4));
    }

    // ==========================
    // == 5. startPairHopper() ==
    // ==========================

    @Test
    public void testStartPairHopper_PlayerHopsBetweenPairs() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();

        Player player = createMockPlayer("hopper", Rank.SILVER, Rank.SILVER, Rank.SILVER);
        int rank = Rank.SILVER.ordinal() + 1;

        // Place player in Pair 0, Queue A
        int pair0Key = rank + 0;
        Queue<Player>[] pair0 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pair0Key);
        pair0[0].enqueue(player);

        // Start pair hopper with short delay for testing
        matcher.startPairHopperGeneral(player, GameType.TICTACTOE, 0, 100); // 100ms hop interval

        // Wait a little more than one hop
        Thread.sleep(150); // Allow exactly one hop to happen

        // Now check that player is no longer in pair 0
        boolean stillInPair0 = pair0[0].contains(player) || pair0[1].contains(player);

        // And check that they're in pair 1
        int pair1Key = rank + 7;
        Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pair1Key);
        boolean nowInPair1 = pair1[0].contains(player) || pair1[1].contains(player);

        assertFalse(stillInPair0, "Player should have left pair 0.");
        assertTrue(nowInPair1, "Player should have hopped into pair 1.");
    }

    @Test
    public void testStartPairHopper_StopsIfPlayerRemovedMidway() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player player = createMockPlayer("exitEarly", Rank.DIAMOND, Rank.DIAMOND, Rank.DIAMOND);
        int rank = Rank.DIAMOND.ordinal() + 1;
        int pairKey = rank;

        // Place in Pair 0 Queue A
        matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pairKey)[0].enqueue(player);

        // Start hopper
        matcher.startPairHopperGeneral(player, GameType.TICTACTOE, 0, 100);

        // Wait for first cycle to start
        Thread.sleep(120);

        // Remove player entirely
        matcher.removePlayer(player, GameType.TICTACTOE);

        // Wait enough time to confirm hopper exits
        Thread.sleep(120);

        // Check all queues are empty
        for (int pairIndex = 0; pairIndex < 2; pairIndex++) {
            int key = rank + 7 * pairIndex;
            Queue<Player>[] pair = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(key);
            assertFalse(pair[0].contains(player));
            assertFalse(pair[1].contains(player));
        }
    }


    @Test
    public void testStartPairHopper_MultipleHopsOccur() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player player = createMockPlayer("multiHopper", Rank.GOLD, Rank.GOLD, Rank.GOLD);
        int rank = Rank.GOLD.ordinal() + 1;

        // Initially place in Pair 0 Queue A
        int pair0Key = rank;
        matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pair0Key)[0].enqueue(player);

        // Start hopper with short delay
        matcher.startPairHopperGeneral(player, GameType.TICTACTOE, 0, 100);

        // Wait for at least 2 hops
        Thread.sleep(250);

        // Check that player has moved at least once and possibly back
        int pair1Key = rank + 7;
        boolean inPair0 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pair0Key)[0].contains(player)
                || matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pair0Key)[1].contains(player);
        boolean inPair1 = matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pair1Key)[0].contains(player)
                || matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(pair1Key)[1].contains(player);

        assertTrue(inPair0 || inPair1, "Player should still be in some queue.");
    }




    @Test
    public void testStartPairHopper_QueuesEqualSize_PlayerAddedToRandomQueue() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player player = createMockPlayer("randomQueue", Rank.PLATINUM, Rank.PLATINUM, Rank.PLATINUM);
        int rank = Rank.PLATINUM.ordinal() + 1;

        // Place player in Pair 0 Queue A
        int pair0Key = rank;
        matcher.getQueuesPerGame().get(GameType.CONNECT4).get(pair0Key)[0].enqueue(player);

        // Fill both queues in Pair 1 with equal size (1 dummy each)
        int pair1Key = rank + 7;
        Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(pair1Key);
        pair1[0].enqueue(createMockPlayer("dummyA", Rank.PLATINUM, Rank.PLATINUM, Rank.PLATINUM));
        pair1[1].enqueue(createMockPlayer("dummyB", Rank.PLATINUM, Rank.PLATINUM, Rank.PLATINUM));

        // Start hopping
        matcher.startPairHopperGeneral(player, GameType.CONNECT4, 0, 100);

        Thread.sleep(150);

        // Player should now be added randomly to one of the equal-size queues
        boolean inA = pair1[0].contains(player);
        boolean inB = pair1[1].contains(player);

        assertTrue(inA ^ inB, "Player should be in exactly one of the two target queues.");
    }

    @Test
    public void testStartPairHopper_GoesToQueueAIfSmaller() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player player = createMockPlayer("goA", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE);
        int rank = Rank.BRONZE.ordinal() + 1;

        int pair0Key = rank;
        matcher.getQueuesPerGame().get(GameType.CHECKERS).get(pair0Key)[0].enqueue(player);

        // Prepare pair 1 for hopping
        int pair1Key = rank + 7;
        Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.CHECKERS).get(pair1Key);
        pair1[1].enqueue(createMockPlayer("fillerB", Rank.BRONZE, Rank.BRONZE, Rank.BRONZE)); // Only B has 1

        matcher.startPairHopperGeneral(player, GameType.CHECKERS, 0, 100);

        Thread.sleep(150);

        assertTrue(pair1[0].contains(player), "Player should have gone to Queue A since it's smaller.");
    }

    @Test
    public void testStartPairHopper_GoesToQueueBIfSmaller() throws InterruptedException {
        QueueMatchMaker matcher = new QueueMatchMaker();
        Player player = createMockPlayer("goB", Rank.SILVER, Rank.SILVER, Rank.SILVER);
        int rank = Rank.SILVER.ordinal() + 1;

        int pair0Key = rank;
        matcher.getQueuesPerGame().get(GameType.CONNECT4).get(pair0Key)[0].enqueue(player);

        // Pair 1 setup
        int pair1Key = rank + 7;
        Queue<Player>[] pair1 = matcher.getQueuesPerGame().get(GameType.CONNECT4).get(pair1Key);
        pair1[0].enqueue(createMockPlayer("fillerA", Rank.SILVER, Rank.SILVER, Rank.SILVER)); // A has 1, B has 0

        matcher.startPairHopperGeneral(player, GameType.CONNECT4, 0, 100);

        Thread.sleep(150);

        assertTrue(pair1[1].contains(player), "Player should have gone to Queue B since it's smaller.");
    }


    // ===========================
    // == 6. printQueueStatus() ==
    // ===========================

    @Test
    public void testPrintQueueStatus_EmptyQueues() {
        QueueMatchMaker matcher = new QueueMatchMaker();

        String output = captureOutput(() -> matcher.printQueueStatus(GameType.TICTACTOE));

        assertTrue(output.contains("Queue Status for TICTACTOE"));
        for (int rank = 1; rank <= 7; rank++) {
            assertTrue(output.contains("Rank " + rank + " - Pair 0 Queue A: 0 | Queue B: 0"));
            assertTrue(output.contains("Rank " + rank + " - Pair 1 Queue A: 0 | Queue B: 0"));
        }
    }


    @Test
    public void testPrintQueueStatus_QueuesWithPlayers() {
        QueueMatchMaker matcher = new QueueMatchMaker();

        Player p1 = createMockPlayer("one", Rank.BRONZE, Rank.SILVER, Rank.GOLD);
        Player p2 = createMockPlayer("two", Rank.BRONZE, Rank.SILVER, Rank.GOLD);

        // Place one player in Queue A of Pair 0 and one in Queue B of Pair 1 for rank 1
        matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(1)[0].enqueue(p1);  // Pair 0, Queue A
        matcher.getQueuesPerGame().get(GameType.TICTACTOE).get(8)[1].enqueue(p2);  // Pair 1, Queue B

        String output = captureOutput(() -> matcher.printQueueStatus(GameType.TICTACTOE));

        assertTrue(output.contains("Rank 1 - Pair 0 Queue A: 1 | Queue B: 0"));
        assertTrue(output.contains("Rank 1 - Pair 1 Queue A: 0 | Queue B: 1"));
    }


    @Test
    public void testPrintQueueStatus_HeaderPerGame() {
        QueueMatchMaker matcher = new QueueMatchMaker();

        String outputC4 = captureOutput(() -> matcher.printQueueStatus(GameType.CONNECT4));
        String outputChk = captureOutput(() -> matcher.printQueueStatus(GameType.CHECKERS));

        assertTrue(outputC4.startsWith("Queue Status for CONNECT4"));
        assertTrue(outputChk.startsWith("Queue Status for CHECKERS"));
    }



    // HELPER FUNCTIONS FOR TESTS


    private Player createMockPlayer(String username, Rank connect4Rank, Rank tttRank, Rank checkersRank) {
        Player player = new Player(username, username + "@test.com", "password123");

        // Configure Connect4 stats
        connect4Stats c4Stats = new connect4Stats(username);
        c4Stats.setRank(connect4Rank);
        c4Stats.setMMR(100);
        c4Stats.setGamesPlayed(10);
        c4Stats.setWins(5);
        c4Stats.setLosses(3);
        c4Stats.setTies(2);
        player.setConnect4Stats(c4Stats);

        // Configure TicTacToe stats
        ticTacToeStats tttStats = new ticTacToeStats(username);
        tttStats.setRank(tttRank);
        tttStats.setMMR(100);
        tttStats.setGamesPlayed(8);
        tttStats.setWins(4);
        tttStats.setLosses(2);
        tttStats.setTies(2);
        player.setTicTacToeStats(tttStats);

        // Configure Checkers stats
        checkersStats checkStats = new checkersStats(username);
        checkStats.setRank(checkersRank);
        checkStats.setMMR(120);
        checkStats.setGamesPlayed(12);
        checkStats.setWins(6);
        checkStats.setLosses(5);
        checkStats.setTies(1);
        player.setCheckersStats(checkStats);

        return player;
    }

    private void waitForMatchLoop() {
        try {
            Thread.sleep(6000); // Let matchmaking thread run once
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private String captureOutput(Runnable task) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try {
            task.run();
        } finally {
            System.setOut(originalOut);
        }
        return outContent.toString().trim();
    }

}
