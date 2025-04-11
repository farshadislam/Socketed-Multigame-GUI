////package main.java.org.seng.networking;
////import main.java.org.seng.objects.Player;
////import org.seng.networking.Player;
//
//package org.seng.networking;
//import java.util.*;
//
//
//public class Leaderboard {
//
//    /**
//     * to count wins
//     * stores PlayerID(String) and maps it to number of wins(Integer)
//     */
//    private final Map<String, Integer> playerWins;
//    public List<String> rankedPlayers;
//
//    /**
//     * initializes an empty hashmap, when Leaderboard object is created.
//     * Stores win count
//     */
//    public Leaderboard(){
//        this.playerWins = new HashMap<>();
//        this.rankedPlayers = new ArrayList<>();
//    }
//
//    /**
//     * we want to also add a method that adds a player to the leaderboard
//     *
//     */
//    public void addPlayer(Player player){
//        String playerID = String.valueOf(player.getPlayerID());
//        playerWins.putIfAbsent(playerID, 0);        //if player doesn't exist on the map, give them zero wins
//    }
//
//    /**p
//     *
//     * @param winner
//     * @param loser
//     */
//    public void logGameResults(Player winner, Player loser){
//        String winnerID = String.valueOf(winner.getPlayerID());
//        playerWins.put(winnerID, playerWins.getOrDefault(winnerID, 0)+1);           //gets the number of wins, and sets its default to 0 if player doest't exist on the map. Adds one to give them the win
//        System.out.println("Leaderboard Updated: Player:  " + winnerID + " Wins: " + playerWins.get(winnerID));
//    }
//
//
//    public void updatePlayer( Player player){
//        String playerID = String.valueOf(player.getPlayerID());
//        int wins = player.getWins();
//
//        playerWins.put(playerID,wins);
//    }
//
//
//
//    public String getTopPlayers(){
//        String TopPlayerId = null;
//        int maxWins = -1;
//
//        for (Map.Entry<String,Integer> entry: playerWins.entrySet()){
//            String playerID = entry.getKey();
//            int wins = entry.getValue();
//
//            if (wins > maxWins){
//                maxWins = wins;
//                TopPlayerId = playerID;
//            }
//        }
//        return TopPlayerId;
//    }
//
//    public List<String> getRankedPlayers() {
//        return rankedPlayers;
//    }
//
//
//    public void updateLeaderboardRank(){
//        rankedPlayers = new ArrayList<>(playerWins.keySet());
//
//
//        //where the ranking happens, built in java method that sorts a list
//        Collections.sort(rankedPlayers, new Comparator<String>(){
//
//
//            //creating a new instance of comparator
//            //p1 and p2 are the two players being compared, both p1 and p2 are player ids
//            public int compare(String p1, String p2){
//                //compares like: p2-p1. if p2 is greater than p1, than its positive, if p1 is greater than p1, then it is negative
//                return Integer.compare(playerWins.get(p2), playerWins.get(p1));
//            }
//        });
//        System.out.println("\n=== Leaderboard Rankings ===");
//        for (int i = 0; i < rankedPlayers.size(); i++) {
//            String playerId = rankedPlayers.get(i);
//            int wins = playerWins.get(playerId);
//            System.out.println((i + 1) + ". " + playerId + " - Wins: " + wins);
//
//        }
//    }
//}
//
//
//
//
