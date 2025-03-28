import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CredentialsDatabase {

    // Creating a HashMap field that will store the information of the player
    HashMap<String, Player> playerCredentials;

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
            for (String key : playerCredentials.keySet()) {


            }




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadDatabase(){
        //(It will load all the information from the database.txt file into the hashmap
        //everytime main is run and main creates a new instance of Database class
        //→ take all the fields stored in database.txt file and create a new player
        //object and then store it into the hashmap)
    }





}
