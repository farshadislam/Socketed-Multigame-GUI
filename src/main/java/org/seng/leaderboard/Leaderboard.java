//package org.seng.leaderboard;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class Leaderboard {
//    private List<GeneralStats> playersStats; // List to hold all game stats objects
//
//    public Leaderboard(List<GeneralStats> playersStats) {
//        this.playersStats = playersStats;
//    }
//
//    // Function to get top N players sorted by the specified criteria
//    public List<GeneralStats> getTopPlayers(String by, int topN) {
//        List<GeneralStats> sortedPlayers = new ArrayList<>(playersStats);  // Copy list to sort
//
//        // Sorting logic based on the specified criteria
//        switch (by.toLowerCase()) {
//            case "wins":
//                sortedPlayers.sort(Comparator.comparingInt(GeneralStats::getWins)
//                        .reversed()
//                        .thenComparingInt(GeneralStats::getGamesPlayed)
//                        .reversed());
//                break;
//            case "mmr":
//                sortedPlayers.sort(Comparator.comparingInt(GeneralStats::getMMR)
//                        .reversed()
//                        .thenComparingInt(GeneralStats::getWins)
//                        .reversed()
//                        .thenComparingInt(GeneralStats::getGamesPlayed)
//                        .reversed());
//                break;
//            case "rank":
//                sortedPlayers.sort(Comparator.comparing(GeneralStats::getRank)
//                        .reversed()
//                        .thenComparingInt(GeneralStats::getMMR)
//                        .reversed());
//                break;
//            case "gamesplayed":
//                sortedPlayers.sort(Comparator.comparingInt(GeneralStats::getGamesPlayed)
//                        .reversed()
//                        .thenComparingInt(GeneralStats::getMMR)
//                        .reversed());
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid sorting criteria: " + by);
//        }
//
//        // Return the top N players (handling cases where there are fewer than N players)
//        return sortedPlayers.subList(0, Math.min(topN, sortedPlayers.size()));
//    }
//}