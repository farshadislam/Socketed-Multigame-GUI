package org.seng.authentication;

import java.io.*;
import java.util.HashMap;

import org.seng.leaderboard.*;
import org.seng.leaderboard.Rank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
                connect4Stats connect4stats = (connect4Stats) player.getStats(GameType.CONNECT4);
                checkersStats checkersstats = (checkersStats) player.getStats(GameType.CHECKERS);
                ticTacToeStats ticTacToestats = (ticTacToeStats) player.getStats(GameType.TICTACTOE);

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


    public void loadDatabase(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split and trim the line using a comma
                String[] words = line.split(",");

                // Creating the Player object
                Player player = new Player(words[0].trim(), words[1].trim(), words[2].trim());

                connect4Stats connect4 = player.getConnect4Stats();
                checkersStats checkers = player.getCheckersStats();
                ticTacToeStats ticTacToe = player.getTicTacToeStats();

                try {
                    // Setting fields for Connect4
                    connect4.setGamesPlayed(Integer.parseInt(words[3].trim()));
                    connect4.setWins(Integer.parseInt(words[4].trim()));
                    connect4.setLosses(Integer.parseInt(words[5].trim()));
                    connect4.setTies(Integer.parseInt(words[6].trim()));
                    connect4.setRank(Rank.valueOf(words[7].trim().toUpperCase()));
                    connect4.setMMR(Integer.parseInt(words[8].trim()));

                    // Setting fields for Checkers
                    checkers.setGamesPlayed(Integer.parseInt(words[9].trim()));
                    checkers.setWins(Integer.parseInt(words[10].trim()));
                    checkers.setLosses(Integer.parseInt(words[11].trim()));
                    checkers.setTies(Integer.parseInt(words[12].trim()));
                    checkers.setRank(Rank.valueOf(words[13].trim().toUpperCase()));
                    checkers.setMMR(Integer.parseInt(words[14].trim()));

                    // Setting fields for Tic-Tac-Toe
                    ticTacToe.setGamesPlayed(Integer.parseInt(words[15].trim()));
                    ticTacToe.setWins(Integer.parseInt(words[16].trim()));
                    ticTacToe.setLosses(Integer.parseInt(words[17].trim()));
                    ticTacToe.setTies(Integer.parseInt(words[18].trim()));
                    ticTacToe.setRank(Rank.valueOf(words[19].trim().toUpperCase()));
                    ticTacToe.setMMR(Integer.parseInt(words[20].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing integer values in line: " + line);
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    System.out.println("Error parsing rank in line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
            e.printStackTrace();
        }
    }



}
