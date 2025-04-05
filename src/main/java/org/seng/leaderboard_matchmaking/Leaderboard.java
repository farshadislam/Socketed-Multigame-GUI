package org.seng.leaderboard_matchmaking;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Leaderboard {
    private List<Player> players = new ArrayList<>();

    public void readDatabase(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 21) {
                    continue;
                }
                String username = data[0];
                String email = data[1];
                String password = data[2];
                int c4GamesPlayed = Integer.parseInt(data[3]);
                int c4Wins = Integer.parseInt(data[4]);
                int c4Losses = Integer.parseInt(data[5]);
                int c4Ties = Integer.parseInt(data[6]);
                String c4Rank = data[7];
                int c4MMR = Integer.parseInt(data[8]);
                int tttGamesPlayed = Integer.parseInt(data[9]);
                int tttWins = Integer.parseInt(data[10]);
                int tttLosses = Integer.parseInt(data[11]);
                int tttTies = Integer.parseInt(data[12]);
                String tttRank = data[13];
                int tttMMR = Integer.parseInt(data[14]);
                int chGamesPlayed = Integer.parseInt(data[15]);
                int chWins = Integer.parseInt(data[16]);
                int chLosses = Integer.parseInt(data[17]);
                int chTies = Integer.parseInt(data[18]);
                String chRank = data[19];
                int chMMR = Integer.parseInt(data[20]);
                Player existingPlayer = findPlayer(username);
                if (existingPlayer != null) {
                    existingPlayer.setEmail(email);
                    existingPlayer.setPassword(password);
                    existingPlayer.getConnect4Stats().setGamesPlayed(c4GamesPlayed);
                    existingPlayer.getConnect4Stats().setWins(c4Wins);
                    existingPlayer.getConnect4Stats().setLosses(c4Losses);
                    existingPlayer.getConnect4Stats().setTies(c4Ties);
                    existingPlayer.getConnect4Stats().setRank(Rank.valueOf(c4Rank));
                    existingPlayer.getConnect4Stats().setMMR(c4MMR);
                    existingPlayer.getTicTacToeStats().setGamesPlayed(tttGamesPlayed);
                    existingPlayer.getTicTacToeStats().setWins(tttWins);
                    existingPlayer.getTicTacToeStats().setLosses(tttLosses);
                    existingPlayer.getTicTacToeStats().setTies(tttTies);
                    existingPlayer.getTicTacToeStats().setRank(Rank.valueOf(tttRank));
                    existingPlayer.getTicTacToeStats().setMMR(tttMMR);
                    existingPlayer.getCheckersStats().setGamesPlayed(chGamesPlayed);
                    existingPlayer.getCheckersStats().setWins(chWins);
                    existingPlayer.getCheckersStats().setLosses(chLosses);
                    existingPlayer.getCheckersStats().setTies(chTies);
                    existingPlayer.getCheckersStats().setRank(Rank.valueOf(chRank));
                    existingPlayer.getCheckersStats().setMMR(chMMR);
                } else {
                    Player newPlayer = new Player(username, "BRONZE", c4Wins, tttWins, chWins);
                    newPlayer.setEmail(email);
                    newPlayer.setPassword(password);
                    newPlayer.getConnect4Stats().setGamesPlayed(c4GamesPlayed);
                    newPlayer.getConnect4Stats().setWins(c4Wins);
                    newPlayer.getConnect4Stats().setLosses(c4Losses);
                    newPlayer.getConnect4Stats().setTies(c4Ties);
                    newPlayer.getConnect4Stats().setRank(Rank.valueOf(c4Rank));
                    newPlayer.getConnect4Stats().setMMR(c4MMR);
                    newPlayer.getTicTacToeStats().setGamesPlayed(tttGamesPlayed);
                    newPlayer.getTicTacToeStats().setWins(tttWins);
                    newPlayer.getTicTacToeStats().setLosses(tttLosses);
                    newPlayer.getTicTacToeStats().setTies(tttTies);
                    newPlayer.getTicTacToeStats().setRank(Rank.valueOf(tttRank));
                    newPlayer.getTicTacToeStats().setMMR(tttMMR);
                    newPlayer.getCheckersStats().setGamesPlayed(chGamesPlayed);
                    newPlayer.getCheckersStats().setWins(chWins);
                    newPlayer.getCheckersStats().setLosses(chLosses);
                    newPlayer.getCheckersStats().setTies(chTies);
                    newPlayer.getCheckersStats().setRank(Rank.valueOf(chRank));
                    newPlayer.getCheckersStats().setMMR(chMMR);
                    players.add(newPlayer);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading database: " + e.getMessage());
        }
    }

    private Player findPlayer(String username) {
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public void updatePlayerStats(String username, int c4Wins, int c4Losses, int c4Ties, int tttWins, int tttLosses, int tttTies, int chWins, int chLosses, int chTies) {
        Player player = findPlayer(username);
        if (player != null) {
            player.setConnect4Wins(player.getConnect4Wins() + c4Wins);
            player.getConnect4Stats().setLosses(player.getConnect4Stats().get_losses() + c4Losses);
            player.getConnect4Stats().setTies(player.getConnect4Stats().get_ties() + c4Ties);
            player.setConnect4GamesPlayed(player.getConnect4GamesPlayed() + (c4Wins + c4Losses + c4Ties));
            player.setTicTacToeWins(player.getTicTacToeWins() + tttWins);
            player.getTicTacToeStats().setLosses(player.getTicTacToeStats().get_losses() + tttLosses);
            player.getTicTacToeStats().setTies(player.getTicTacToeStats().get_ties() + tttTies);
            player.setTicTacToeGamesPlayed(player.getTicTacToeGamesPlayed() + (tttWins + tttLosses + tttTies));
            player.setCheckersWins(player.getCheckersWins() + chWins);
            player.getCheckersStats().setLosses(player.getCheckersStats().get_losses() + chLosses);
            player.getCheckersStats().setTies(player.getCheckersStats().get_ties() + chTies);
            player.setCheckersGamesPlayed(player.getCheckersGamesPlayed() + (chWins + chLosses + chTies));
        } else {
            System.out.println("Player " + username + " not found.");
        }
    }

    public void saveUpdatedLeaderboard(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Player player : players) {
                String line = String.join(",",
                        (player.getUsername() != null ? player.getUsername() : ""),
                        (player.getEmail() != null ? player.getEmail() : ""),
                        (player.getPassword() != null ? player.getPassword() : ""),
                        String.valueOf(player.getConnect4Stats().getGamesPlayed()),
                        String.valueOf(player.getConnect4Stats().getWins()),
                        String.valueOf(player.getConnect4Stats().get_losses()),
                        String.valueOf(player.getConnect4Stats().get_ties()),
                        player.getConnect4Stats().getRank().name(),
                        String.valueOf(player.getConnect4Stats().getMMR()),
                        String.valueOf(player.getTicTacToeStats().getGamesPlayed()),
                        String.valueOf(player.getTicTacToeStats().getWins()),
                        String.valueOf(player.getTicTacToeStats().get_losses()),
                        String.valueOf(player.getTicTacToeStats().get_ties()),
                        player.getTicTacToeStats().getRank().name(),
                        String.valueOf(player.getTicTacToeStats().getMMR()),
                        String.valueOf(player.getCheckersStats().getGamesPlayed()),
                        String.valueOf(player.getCheckersStats().getWins()),
                        String.valueOf(player.getCheckersStats().get_losses()),
                        String.valueOf(player.getCheckersStats().get_ties()),
                        player.getCheckersStats().getRank().name(),
                        String.valueOf(player.getCheckersStats().getMMR())
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Updated leaderboard saved (Connect4 + TicTacToe + Checkers).");
        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    public void sortLeaderboard() {
        players.sort(Comparator.comparingInt(Player::getConnect4Wins).reversed());
    }

    public List<Player> getTopPlayers(int n) {
        return players.stream()
                .sorted(Comparator.comparingInt(Player::getTotalWins).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Player> getTopPlayersByGame(String game, int n) {
        Comparator<Player> comparator;
        switch (game.toLowerCase()) {
            case "connect4":
                comparator = Comparator.comparingInt(Player::getConnect4Wins);
                break;
            case "tictactoe":
                comparator = Comparator.comparingInt(Player::getTicTacToeWins);
                break;
            case "checkers":
                comparator = Comparator.comparingInt(Player::getCheckersWins);
                break;
            default:
                throw new IllegalArgumentException("Invalid game: " + game);
        }
        return players.stream()
                .sorted(comparator.reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public void writeLeaderboard(String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Player player : players) {
                String line = String.join(",",
                        (player.getUsername() != null ? player.getUsername() : ""),
                        (player.getEmail() != null ? player.getEmail() : ""),
                        (player.getPassword() != null ? player.getPassword() : ""),
                        String.valueOf(player.getConnect4Stats().getGamesPlayed()),
                        String.valueOf(player.getConnect4Stats().getWins()),
                        String.valueOf(player.getConnect4Stats().get_losses()),
                        String.valueOf(player.getConnect4Stats().get_ties()),
                        player.getConnect4Stats().getRank().name(),
                        String.valueOf(player.getConnect4Stats().getMMR()),
                        String.valueOf(player.getTicTacToeStats().getGamesPlayed()),
                        String.valueOf(player.getTicTacToeStats().getWins()),
                        String.valueOf(player.getTicTacToeStats().get_losses()),
                        String.valueOf(player.getTicTacToeStats().get_ties()),
                        player.getTicTacToeStats().getRank().name(),
                        String.valueOf(player.getTicTacToeStats().getMMR()),
                        String.valueOf(player.getCheckersStats().getGamesPlayed()),
                        String.valueOf(player.getCheckersStats().getWins()),
                        String.valueOf(player.getCheckersStats().get_losses()),
                        String.valueOf(player.getCheckersStats().get_ties()),
                        player.getCheckersStats().getRank().name(),
                        String.valueOf(player.getCheckersStats().getMMR())
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Leaderboard (Connect4 + TicTacToe + Checkers) saved to " + outputFile);
        } catch (IOException e) {
            System.out.println("Error writing leaderboard: " + e.getMessage());
        }
    }

    public void generateLeaderboard(String inputFile, String outputFile) {
        readDatabase(inputFile);
        sortLeaderboard();
        writeLeaderboard(outputFile);
    }
}
