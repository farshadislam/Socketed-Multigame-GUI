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
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("database.txt"));



            // Iterating over the HashMap
            for (String username : playerCredentials.keySet()) {

                Player player = playerCredentials.get(username);
                connect4Stats connect4stats = (connect4Stats) player.getStats("connect4");
                checkersStats checkersstats = (checkersStats) player.getStats("checkers");
                ticTacToeStats ticTacToestats = (ticTacToeStats) player.getStats("tictactoe");


                writer.write(player.getUsername() + ","
                                + player.getEmail() + ","
                                + player.getPassword() + ","
                                + player.getRank() + ","
                                + player.getStats("connect4") + ","
                                + player.getStats("tictactoe" + ",")
                                + player.getStats("checkers") );
                // Waiting for Stats classes to be finished to continue with the implementation
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadDatabase(String fileName){
        //(It will load all the information from the database.txt file into the hashmap
        //everytime main is run and main creates a new instance of Database class
        //→ take all the fields stored in database.txt file and create a new player
        //object and then store it into the hashmap)
    }


}
