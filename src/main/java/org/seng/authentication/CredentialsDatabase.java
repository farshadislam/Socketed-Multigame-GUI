package org.seng.authentication;

import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.seng.leaderboard.GeneralStats;
import org.seng.leaderboard.checkersStats;
import org.seng.leaderboard.connect4Stats;
import org.seng.leaderboard.ticTacToeStats;

public class CredentialsDatabase {

    // Creating a HashMap field that will store the information of the player
    HashMap<String, Player> playerCredentials;
    private static final String FILE_PATH = "database.txt";

    // Creating a constructor
    public CredentialsDatabase() {

        // Initializing the HashMap
        this.playerCredentials = new HashMap<>();

        // Loading the Database from the text file
    }

    public boolean usernameLookup(String username) {
        if (playerCredentials.containsKey(username)) {
            return true;
        }
        return false;
    }

    public boolean addNewPlayer(String username, Player player) {
        if (!(playerCredentials.containsKey(username))) {
            playerCredentials.put(username, player);
            return true;
        }
        return false;
    }

    public boolean deleteExistingPlayer(String username) {
        if (playerCredentials.containsKey(username)) {
            playerCredentials.remove(username);
            return true;
        }
        return false;
    }

    public Player findPlayerByUsername(String username) {
        if (playerCredentials.containsKey(username)) {
            Player value = playerCredentials.get(username);
            return value;
        }
        return null;
    }

    public void saveDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt"))) {

            // Iterating over the HashMap
            for (String username : playerCredentials.keySet()) {

                Player player = playerCredentials.get(username);
                connect4Stats connect4stats = (connect4Stats) player.getStats("connect4");
                checkersStats checkersstats = (checkersStats) player.getStats("checkers");
                ticTacToeStats ticTacToestats = (ticTacToeStats) player.getStats("tictactoe");

                writer.write(player.getUsername() + ","
                        + player.getEmail() + ","
                        + player.getPassword() + ","
                        // Stats of Tic Tac Toe
                        + ticTacToestats.getGamesPlayed() + ","
                        + ticTacToestats.getWins() + ","
                        + ticTacToestats.getLosses() + ","
                        + ticTacToestats.get_ties() + ","
                        + ticTacToestats.getRank().name() + ","  // Store enum as name
                        + ticTacToestats.getMMR() + ","
                        // Stats of Connect 4
                        + connect4stats.getGamesPlayed() + ","
                        + connect4stats.getWins() + ","
                        + connect4stats.getLosses() + ","
                        + connect4stats.get_ties() + ","
                        + connect4stats.getRank().name() + ","  // Store enum as name
                        + connect4stats.getMMR() + ","
                        // Stats for Checkers
                        + checkersstats.getGamesPlayed() + ","
                        + checkersstats.getWins() + ","
                        + checkersstats.getLosses() + ","
                        + checkersstats.get_ties() + ","
                        + checkersstats.getRank().name() + ","  // Store enum as name
                        + checkersstats.getMMR());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadDatabase(String fileName){


    }


}
