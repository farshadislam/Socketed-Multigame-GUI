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
    private HashMap<String, Player> playerCredentials;


    // Creating a constructor
    public CredentialsDatabase() {

        // Initializing the HashMap
        this.playerCredentials = new HashMap<>();

        // Loading the Database from the text file
        loadDatabase("database.txt");
    }

    public boolean usernameLookup(String username) {
        if (playerCredentials.containsKey(username.toLowerCase())) {
            return true;
        }
        return false;
    }

    public boolean addNewPlayer(String username, Player player) {
        if (!(playerCredentials.containsKey(username.toLowerCase()))) {
            playerCredentials.put(username.toLowerCase(), player);
            return true;
        }
        return false;
    }

    public boolean deleteExistingPlayer(String username) {
        if (playerCredentials.containsKey(username.toLowerCase())) {
            playerCredentials.remove(username.toLowerCase());
            return true;
        }
        return false;
    }

    public Player findPlayerByUsername(String username) {
        if (playerCredentials.containsKey(username.toLowerCase())) {
            Player value = playerCredentials.get(username.toLowerCase());
            return value;
        }
        return null;
    }

    public boolean emailTaken(String email){
        for (Player player : playerCredentials.values()) {
            if (player.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public void saveDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {

            // Iterating over the keys in HashMap
            for (String username : playerCredentials.keySet()) {

                Player player = playerCredentials.get(username);

                connect4Stats connect4 = player.getConnect4Stats();
                checkersStats checkers = player.getCheckersStats();
                ticTacToeStats ticTacToe = player.getTicTacToeStats();

                writer.write(player.getUsername() + ","
                        + player.getEmail() + ","
                        + player.getPassword() + ","
                        + player.getSymbol() + ","

                        // Stats of Tic Tac Toe
                        + ticTacToe.getGamesPlayed() + ","
                        + ticTacToe.getWins() + ","
                        + ticTacToe.getLosses() + ","
                        + ticTacToe.get_ties() + ","
                        + ticTacToe.getRank().name() + ","  // Store enum as name
                        + ticTacToe.getMMR() + ","

                        // Stats of Connect 4
                        + connect4.getGamesPlayed() + ","
                        + connect4.getWins() + ","
                        + connect4.getLosses() + ","
                        + connect4.get_ties() + ","
                        + connect4.getRank().name() + ","  // Store enum as name
                        + connect4.getMMR() + ","

                        // Stats for Checkers
                        + checkers.getGamesPlayed() + ","
                        + checkers.getWins() + ","
                        + checkers.getLosses() + ","
                        + checkers.get_ties() + ","
                        + checkers.getRank().name() + ","  // Store enum as name
                        + checkers.getMMR());
                writer.newLine();
                System.out.println("Success");
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

                player.setSymbol(words[3].trim().charAt(0));

                connect4Stats connect4 = player.getConnect4Stats();
                checkersStats checkers = player.getCheckersStats();
                ticTacToeStats ticTacToe = player.getTicTacToeStats();

                try {
                    // Setting fields for Connect4
                    connect4.setGamesPlayed(Integer.parseInt(words[4].trim()));
                    connect4.setWins(Integer.parseInt(words[5].trim()));
                    connect4.setLosses(Integer.parseInt(words[6].trim()));
                    connect4.setTies(Integer.parseInt(words[7].trim()));
                    connect4.setRank(Rank.valueOf(words[8].trim().toUpperCase()));
                    connect4.setMMR(Integer.parseInt(words[9].trim()));

                    // Setting fields for Checkers
                    checkers.setGamesPlayed(Integer.parseInt(words[10].trim()));
                    checkers.setWins(Integer.parseInt(words[11].trim()));
                    checkers.setLosses(Integer.parseInt(words[12].trim()));
                    checkers.setTies(Integer.parseInt(words[13].trim()));
                    checkers.setRank(Rank.valueOf(words[14].trim().toUpperCase()));
                    checkers.setMMR(Integer.parseInt(words[15].trim()));

                    // Setting fields for Tic-Tac-Toe
                    ticTacToe.setGamesPlayed(Integer.parseInt(words[16].trim()));
                    ticTacToe.setWins(Integer.parseInt(words[17].trim()));
                    ticTacToe.setLosses(Integer.parseInt(words[18].trim()));
                    ticTacToe.setTies(Integer.parseInt(words[19].trim()));
                    ticTacToe.setRank(Rank.valueOf(words[20].trim().toUpperCase()));
                    ticTacToe.setMMR(Integer.parseInt(words[21].trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing integer values in line: " + line);
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    System.out.println("Error parsing rank in line: " + line);
                    e.printStackTrace();
                }

                // Adding the Player to the playerCredentials database
                playerCredentials.put(player.getUsername(), player);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
            e.printStackTrace();
        }
    }

    public boolean updateKey(String oldUsername, String newUsername) {
        String oldKey = oldUsername.toLowerCase();
        String newKey = newUsername.toLowerCase();

        // Check if old username exists and new one doesn't
        if (!playerCredentials.containsKey(oldUsername)) return false;
        if (playerCredentials.containsKey(newUsername)) return false;

        // Get the player object
        Player player = playerCredentials.remove(oldKey);

        // Update the player's username field
        player.setUsername(newKey);

        // Re-insert with the new username as the key
        playerCredentials.put(newKey, player);

        return true;
    }



}
