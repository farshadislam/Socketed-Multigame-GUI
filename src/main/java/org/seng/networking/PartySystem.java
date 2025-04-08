package org.seng.networking;

import org.seng.networking.leaderboard_matchmaking.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * PartySystem lets players form parties, start games together,
 * and keeps everyone in sync and informed.
 */
public class PartySystem {

    // This initializes a Unique ID for the party
    private String partyID;
    // This initializes a list of all of the players in the party
    private List<String> partyMembers;
    // This holds the username of the host
    private String hostUsername;
    // This boolean is for whether a game is in progress
    private boolean isInGame;
    // This is initialized to determine the game type for the party
    private GameType gameType;

    /**
     * This reates a new party with a host and a game type.
     */
    public PartySystem(String hostUsername, GameType gameType) {
        this.hostUsername = hostUsername;
        this.partyID = generatePartyID();
        this.partyMembers = new ArrayList<>();
        this.partyMembers.add(hostUsername);
        this.isInGame = false;
        this.gameType = gameType;
    }

    // This generates a unique ID for the party using UUID
    private String generatePartyID() {
        return "PARTY-" + UUID.randomUUID().toString();
    }

    // This is a mock implementation for syncing with the server
    private void syncPartyState() {
        System.out.println("Party state synced with the server.");
    }

    /**
     * This adds a player to the party and notifes all
     */
    public boolean addPlayer(String playerUsername) {
        if (!partyMembers.contains(playerUsername)) {
            partyMembers.add(playerUsername);
            syncPartyState();

            // This lets everyone in the party know someone joined
            NetworkingManager.getInstance().broadcastToUsers(
                    partyMembers,
                    playerUsername + " has joined the party."
            );

            System.out.println(playerUsername + " joined the party.");
            return true;
        }

        System.out.println(playerUsername + " is already in the party.");
        return false;
    }

    /**
     * This removes a player and notifies the party.
     */
    public boolean removePlayer(String playerUsername) {
        if (partyMembers.contains(playerUsername) && !playerUsername.equals(hostUsername)) {
            partyMembers.remove(playerUsername);
            syncPartyState();

            // This lets everyone know someone left
            NetworkingManager.getInstance().broadcastToUsers(
                    partyMembers,
                    playerUsername + " has left the party."
            );

            System.out.println(playerUsername + " was removed from the party.");
            return true;
        }

        System.out.println("Cannot remove " + playerUsername + " — not in the party or is the host.");
        return false;
    }

    /**
     * This clears the party and resets the ID
     */
    public void disbandParty() {
        partyMembers.clear();
        partyID = generatePartyID(); // This resets  with a new ID
        System.out.println("Party disbanded. A new party can be created.");
    }

    /**
     * This starts a game if there’s more than one player.
     */
    public boolean startGame() {
        if (partyMembers.size() > 1 && !isInGame) {
            isInGame = true;
            syncPartyState();

            // This lets everyone know the game has started
            NetworkingManager.getInstance().broadcastToUsers(
                    partyMembers,
                    "[Game Started] Your match is beginning now!"
            );

            System.out.println("Game started! Players: " + partyMembers);
            return true;
        }

        System.out.println("Not enough players to start the game. Invite more players.");
        return false;
    }

    /**
     * This ends the current game and tells the party
     */
    public void endGame() {
        if (isInGame) {
            isInGame = false;
            syncPartyState();

            // Notify all members the game is over
            NetworkingManager.getInstance().broadcastToUsers(
                    partyMembers,
                    "[Game Ended] Thanks for playing! Returning to the party lobby."
            );

            System.out.println("Game ended. Returning to the lobby.");
        } else {
            System.out.println("No active game is running.");
        }
    }

    /**
     * This returns a snapshot of the party member list
     */
    public List<String> getPartyMembers() {
        return new ArrayList<>(partyMembers);
    }

    /**
     * This returns the username of the party host
     */
    public String getHostUsername() {
        return hostUsername;
    }

    /**
     * This returns true if the party is currently in a game
     */
    public boolean isInGame() {
        return isInGame;
    }

    /**
     * This returns the type of game selected by the party
     */
    public GameType getGameType() {
        return gameType;
    }
}
