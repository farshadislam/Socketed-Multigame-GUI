package org.seng.networking.test;

import org.junit.jupiter.api.Test;
import org.seng.networking.GameSessionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class GameSessionManagerTest {


        @Test
        void startSessionTest(){
            GameSessionManager manager = new GameSessionManager();
            Player player = new Player("Sean", "69420");
            player.setPlayerID(123);
            String sessionID = "session1";
            manager.startSession(sessionID,player);

            assertEquals("123", manager.getPlayerIDBySession(sessionID));
        }



        @Test
        void endSessionTest(){
            GameSessionManager manager = new GameSessionManager();
            Player player = new Player("Combs", "119");
            String sessionID = "session2";

            manager.startSession(sessionID, player);
            manager.endSession(sessionID);

            assertNull(manager.getPlayerIDBySession(sessionID));
        }
}
