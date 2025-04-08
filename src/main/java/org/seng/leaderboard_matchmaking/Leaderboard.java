package org.seng.leaderboard_matchmaking;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import org.seng.authentication.Player;

/**
 * Manages the leaderboard data for players across multiple games.
 * Supports loading from and saving to CSV, updating stats, and retrieving top players.
 */
public class Leaderboard {

    private Map<String, Player> players = new HashMap<>();

    /**
     * Gets the map of all players in the leaderboard.
     * @return Map of usernames to Player objects
     */
    public Map<String, Player> getPlayers() {
        return players;
    }

    // All index constants
    private static final int IDX_USERNAME = 0;
    private static final int IDX_EMAIL = 1;
    private static final int IDX_PASSWORD = 2;

    // TicTacToe
    private static final int IDX_TTT_GAMES = 3;
    private static final int IDX_TTT_WINS = 4;
    private static final int IDX_TTT_LOSSES = 5;
    private static final int IDX_TTT_TIES = 6;
    private static final int IDX_TTT_RANK = 7;
    private static final int IDX_TTT_MMR = 8;

    // Connect4
    private static final int IDX_C4_GAMES = 9;
    private static final int IDX_C4_WINS = 10;
    private static final int IDX_C4_LOSSES = 11;
    private static final int IDX_C4_TIES = 12;
    private static final int IDX_C4_RANK = 13;
    private static final int IDX_C4_MMR = 14;

    // Checkers
    private static final int IDX_CH_GAMES = 15;
    private static final int IDX_CH_WINS = 16;
    private static final int IDX_CH_LOSSES = 17;
    private static final int IDX_CH_TIES = 18;
    private static final int IDX_CH_RANK = 19;
    private static final int IDX_CH_MMR = 20;

    /**
     * Loads player data from a CSV file.
     *
     * @param filePath path to the CSV database file
     */
    public void readDatabase(String filePath) {
        players.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String username = data[IDX_USERNAME];

                Player player = players.get(username);
                updatePlayerFromData(player, data);
            }
        } catch (IOException e) {
            System.out.println("Error reading database: " + e.getMessage());
        }
    }

    /**
     * Populates a Player object with stats from parsed CSV data.
     *
     * @param player Player to update
     * @param data   array of values from CSV line
     */
    private void updatePlayerFromData(Player player, String[] data) {
        player.setEmail(data[IDX_EMAIL]);
        player.setPassword(data[IDX_PASSWORD]);

        // TicTacToe
        ticTacToeStats tttStats = player.getTicTacToeStats();
        tttStats.setGamesPlayed(Integer.parseInt(data[IDX_TTT_GAMES]));
        tttStats.setWins(Integer.parseInt(data[IDX_TTT_WINS]));
        tttStats.setLosses(Integer.parseInt(data[IDX_TTT_LOSSES]));
        tttStats.setTies(Integer.parseInt(data[IDX_TTT_TIES]));
        tttStats.setRank(Rank.valueOf(data[IDX_TTT_RANK].trim().toUpperCase()));
        tttStats.setMMR(Integer.parseInt(data[IDX_TTT_MMR]));

        // Connect4
        connect4Stats c4Stats = player.getConnect4Stats();
        c4Stats.setGamesPlayed(Integer.parseInt(data[IDX_C4_GAMES]));
        c4Stats.setWins(Integer.parseInt(data[IDX_C4_WINS]));
        c4Stats.setLosses(Integer.parseInt(data[IDX_C4_LOSSES]));
        c4Stats.setTies(Integer.parseInt(data[IDX_C4_TIES]));
        c4Stats.setRank(Rank.valueOf(data[IDX_C4_RANK].trim().toUpperCase()));
        c4Stats.setMMR(Integer.parseInt(data[IDX_C4_MMR]));

        // Checkers
        checkersStats chStats = player.getCheckersStats();
        chStats.setGamesPlayed(Integer.parseInt(data[IDX_CH_GAMES]));
        chStats.setWins(Integer.parseInt(data[IDX_CH_WINS]));
        chStats.setLosses(Integer.parseInt(data[IDX_CH_LOSSES]));
        chStats.setTies(Integer.parseInt(data[IDX_CH_TIES]));
        chStats.setRank(Rank.valueOf(data[IDX_CH_RANK].trim().toUpperCase()));
        chStats.setMMR(Integer.parseInt(data[IDX_CH_MMR]));
    }

    /**
     * Updates the stats of a player after a game session.
     *
     * @param username   username of the player
     * @param c4Wins     Connect4 wins
     * @param c4Losses   Connect4 losses
     * @param c4Ties     Connect4 ties
     * @param tttWins    TicTacToe wins
     * @param tttLosses  TicTacToe losses
     * @param tttTies    TicTacToe ties
     * @param chWins     Checkers wins
     * @param chLosses   Checkers losses
     * @param chTies     Checkers ties
     */
    public void updatePlayerStats(String username,
                                  int c4Wins, int c4Losses, int c4Ties,
                                  int tttWins, int tttLosses, int tttTies,
                                  int chWins, int chLosses, int chTies) {

        Player player = players.get(username);
        if (player == null) {
            System.out.println("Player " + username + " not found.");
            return;
        }

        connect4Stats c4 = player.getConnect4Stats();
        c4.setWins(c4.getWins() + c4Wins);
        c4.setLosses(c4.getLosses() + c4Losses);
        c4.setTies(c4.get_ties() + c4Ties);
        c4.setGamesPlayed(c4.getGamesPlayed() + c4Wins + c4Losses + c4Ties);

        ticTacToeStats ttt = player.getTicTacToeStats();
        ttt.setWins(ttt.getWins() + tttWins);
        ttt.setLosses(ttt.getLosses() + tttLosses);
        ttt.setTies(ttt.get_ties() + tttTies);
        ttt.setGamesPlayed(ttt.getGamesPlayed() + tttWins + tttLosses + tttTies);

        checkersStats ch = player.getCheckersStats();
        ch.setWins(ch.getWins() + chWins);
        ch.setLosses(ch.getLosses() + chLosses);
        ch.setTies(ch.get_ties() + chTies);
        ch.setGamesPlayed(ch.getGamesPlayed() + chWins + chLosses + chTies);
    }

    /**
     * Saves the current leaderboard state to a CSV file.
     *
     * @param filePath the path where the file should be saved
     */
    public void saveUpdatedLeaderboard(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Player player : players.values()) {
                writer.write(serializePlayer(player));
                writer.newLine();
            }
            System.out.println("Updated leaderboard saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    /**
     * Converts a player’s data to a CSV line.
     *
     * @param player the player to serialize
     * @return CSV-formatted string
     */
    private String serializePlayer(Player player) {
        return String.join(",",
                player.getUsername(),
                player.getEmail(),
                player.getPassword(),

                String.valueOf(player.getTicTacToeStats().getGamesPlayed()),
                String.valueOf(player.getTicTacToeStats().getWins()),
                String.valueOf(player.getTicTacToeStats().getLosses()),
                String.valueOf(player.getTicTacToeStats().get_ties()),
                player.getTicTacToeStats().getRank().name(),
                String.valueOf(player.getTicTacToeStats().getMMR()),

                String.valueOf(player.getConnect4Stats().getGamesPlayed()),
                String.valueOf(player.getConnect4Stats().getWins()),
                String.valueOf(player.getConnect4Stats().getLosses()),
                String.valueOf(player.getConnect4Stats().get_ties()),
                player.getConnect4Stats().getRank().name(),
                String.valueOf(player.getConnect4Stats().getMMR()),

                String.valueOf(player.getCheckersStats().getGamesPlayed()),
                String.valueOf(player.getCheckersStats().getWins()),
                String.valueOf(player.getCheckersStats().getLosses()),
                String.valueOf(player.getCheckersStats().get_ties()),
                player.getCheckersStats().getRank().name(),
                String.valueOf(player.getCheckersStats().getMMR())
        );
    }

    /**
     * Returns the top N players by total wins across all games.
     *
     * @param n number of players to return
     * @return list of top players
     */
    public List<Player> getTopPlayers(int n) {
        return players.values().stream()
                .sorted(Comparator.comparingInt(Player::getTotalWins).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Returns the top N players based on a specific game's win count.
     *
     * @param game name of the game ("connect4", "tictactoe", or "checkers")
     * @param n    number of players to return
     * @return list of top players for the specified game
     */
    public List<Player> getTopPlayersByGame(String game, int n) {
        Comparator<Player> comparator = switch (game.toLowerCase()) {
            case "connect4" -> Comparator.comparingInt(p -> p.getConnect4Stats().getWins());
            case "tictactoe" -> Comparator.comparingInt(p -> p.getTicTacToeStats().getWins());
            case "checkers" -> Comparator.comparingInt(p -> p.getCheckersStats().getWins());
            default -> throw new IllegalArgumentException("Invalid game: " + game);
        };

        return players.values().stream()
                .sorted(comparator.reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /**
     * Loads player data from file and prepares the leaderboard.
     * Currently only reads data — does not sort or write.
     *
     * @param inputFile the path to the input file
     * @param outputFile unused here but included for compatibility
     */
    public void generateLeaderboard(String inputFile, String outputFile) {
        readDatabase(inputFile);
    }
}
