package org.seng.networking;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.seng.authentication.Player;
import java.io.*;

/**
 * This class simulates the network that sends messages between players.
 */
public class NetworkingManager {

    // This makes sure there's only one NetworkingManager in the whole project
    private static NetworkingManager instance;

    // THis keeps track of which users are connected to the server
    private final Map<String, Object> connectedUsers;
    private NetworkingManager() {
        this.connectedUsers = new ConcurrentHashMap<>();
    }

    // This lets you get the only instance of this class
    public static NetworkingManager getInstance() {
        if (instance == null) {
            instance = new NetworkingManager();
        }
        return instance;
    }

    /**
     * This pretends someone just joined the server. We add them to our list.
     */
    public void connectPlayer(String username) {
        connectedUsers.put(username, new Object()); // Just storing something to pretend they're connected
        System.out.println("[Networking] " + username + " connected.");
    }

    /**
     * This pretends someone left the server. We remove them from our list.
     */
    public void disconnectPlayer(String username) {
        connectedUsers.remove(username);
        System.out.println("[Networking] " + username + " disconnected.");
    }

    /**
     * This sends a message to just one person.
     */
    public void sendMessageToUser(String username, String message) {
        if (connectedUsers.containsKey(username)) {
            System.out.println("[Networking → " + username + "]: " + message);
        } else {
            System.out.println("[Networking] Couldn't send to " + username + " — not connected.");
        }
    }

    /**
     * This sends a message to a bunch of users
     */
    public void broadcastToUsers(List<String> users, String message) {
        for (String user : users) {
            sendMessageToUser(user, message);
        }
    }

    /**
     * This lets both players know that they got matched up
     */
    public void notifyPlayersMatched(Player player1, Player player2, Match match) {
        String opponentForPlayer1;
        String opponentForPlayer2;

        if (player1.equals(match.getPlayer1())) {
            opponentForPlayer1 = player2.getUsername();
            opponentForPlayer2 = player1.getUsername();
        } else {
            opponentForPlayer1 = player1.getUsername();
            opponentForPlayer2 = player2.getUsername();
        }

        String msg1 = "[MATCH FOUND] You are matched! Match ID: " + match.getMatchID() +
                ", Game: " + match.getGameType().name() +
                "\nOpponent: " + opponentForPlayer1;

        String msg2 = "[MATCH FOUND] You are matched! Match ID: " + match.getMatchID() +
                ", Game: " + match.getGameType().name() +
                "\nOpponent: " + opponentForPlayer2;

        sendMessageToUser(player1.getUsername(), msg1);
        sendMessageToUser(player2.getUsername(), msg2);
    }

    /**
     * This gets a list of everyone who is currently
     */
    public List<String> getConnectedUsers() {
        return connectedUsers.keySet().stream().toList();
    }
}
