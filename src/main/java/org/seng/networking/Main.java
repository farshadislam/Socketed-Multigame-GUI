//package main.java.org.seng.networking;
//
//import main.java.org.seng.networking.leaderboard_matchmaking.GameType;
//
//
//public class Main {
//    public static void main(String[] args) {
//        main.java.org.seng.networking.NetworkingManager net = main.java.org.seng.networking.NetworkingManager.getInstance();
//
//        // This simulates players connecting
//        net.connectPlayer("Alice");
//        net.connectPlayer("Bob");
//        net.connectPlayer("Charlie");
//
//        // This creates party and add members
//        main.java.org.seng.networking.PartySystem party = new PartySystem("Alice", GameType.CONNECT4);
//        party.addPlayer("Bob");
//
//        // This tries to start the game
//        boolean started = party.startGame();
//        if (started) {
//            // This Simulates match being created
//            main.java.org.seng.networking.Matchmaking mm = new Matchmaking();
//            Match match = mm.joinMatch("Alice", "Bob", GameType.CONNECT4);
//
//            // This notifies both users they’ve been matched
//            net.notifyPlayersMatched("Alice", "Bob", match);
//
//            // This simulates the game ending
//            party.endGame();
//
//            // This notifies match end
//            net.notifyMatchEnd("Alice");
//            net.notifyMatchEnd("Bob");
//        }
//
//        // This disconnects players
//        net.disconnectPlayer("Alice");
//        net.disconnectPlayer("Bob");
//        net.disconnectPlayer("Charlie");
//    }
//}
