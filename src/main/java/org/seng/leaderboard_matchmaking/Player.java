package org.seng.leaderboard_matchmaking;

import java.util.Objects;

public class Player {
    private String username;
    private String email;
    private String password;
    private Rank rank;
    private connect4Stats Connect4Stats;
    private ticTacToeStats TicTacToeStats;
    private checkersStats CheckersStats;

    /**
     * Constructor that also sets initial wins for each game.
     * The 'rank' parameter is applied to each game’s rank as a default.
     */
    public Player(String username, String rank, int connect4Wins, int tictactoeWins, int checkersWins) {
        this.username = username;
        // Fall back on "BRONZE" if rank is null or blank
        if (rank == null || rank.trim().isEmpty()) {
            rank = "BRONZE";
        }
        // Initialize stats objects
        this.Connect4Stats = new connect4Stats(username);
        this.TicTacToeStats = new ticTacToeStats(username);
        this.CheckersStats = new checkersStats(username);
        // Set initial wins
        this.Connect4Stats.setWins(connect4Wins);
        this.TicTacToeStats.setWins(tictactoeWins);
        this.CheckersStats.setWins(checkersWins);
        // Optionally set the same rank to each game’s stats:
        this.Connect4Stats.setRank(Rank.valueOf(rank));
        this.TicTacToeStats.setRank(Rank.valueOf(rank));
        this.CheckersStats.setRank(Rank.valueOf(rank));
    }

    // Basic getters/setters and update methods:

    public boolean updateEmail(String username, String newEmail) {
        if (this.username.equals(username)) {
            if (newEmail != null && !newEmail.isEmpty()) {
                this.email = newEmail;
                return true;
            }
        }
        return false;
    }

    public boolean updatePassword(String username, String newPassword) {
        if (this.username.equals(username)) {
            if (newPassword != null && !newPassword.isEmpty()) {
                this.password = newPassword;
                return true;
            }
        }
        return false;
    }

    public boolean updateUsername(String username, String newUsername) {
        if (this.username.equals(username)) {
            if (newUsername != null && !newUsername.isEmpty()) {
                this.username = newUsername;
                return true;
            }
        }
        return false;
    }

    public connect4Stats getConnect4Stats() {
        return Connect4Stats;
    }

    public void setConnect4Stats(connect4Stats connect4Stats) {
        Connect4Stats = connect4Stats;
    }

    public ticTacToeStats getTicTacToeStats() {
        return TicTacToeStats;
    }

    public void setTicTacToeStats(ticTacToeStats ticTacToeStats) {
        TicTacToeStats = ticTacToeStats;
    }

    public checkersStats getCheckersStats() {
        return CheckersStats;
    }

    public void setCheckersStats(checkersStats checkersStats) {
        CheckersStats = checkersStats;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rank getRank(GameType game) {
        // Return the rank from that game’s stats
        GeneralStats stats = getStats(game.name().toLowerCase());
        return (stats != null) ? stats.getRank() : Rank.BRONZE;
    }

    public void setRank(Rank rank) {
        this.rank = rank; // If you keep a “global” rank
    }

    public GeneralStats getStats(String gameType) {
        return switch (gameType.toLowerCase()) {
            case "connect4" -> Connect4Stats;
            case "tictactoe" -> TicTacToeStats;
            case "checkers" -> CheckersStats;
            default -> null;
        };
    }

    // Total statistics

    public int getTotalLosses() {
        return Connect4Stats.get_losses() + TicTacToeStats.get_losses() + CheckersStats.get_losses();
    }

    public int getTotalGamesPlayed() {
        return Connect4Stats.getGamesPlayed() + TicTacToeStats.getGamesPlayed() + CheckersStats.getGamesPlayed();
    }

    public int getTotalTies() {
        return Connect4Stats.get_ties() + TicTacToeStats.get_ties() + CheckersStats.get_ties();
    }

    public int getTotalWins() {
        return Connect4Stats.getWins() + TicTacToeStats.getWins() + CheckersStats.getWins();
    }

    // Connect4 methods:
    public int getConnect4Wins()         { return Connect4Stats.get_wins(); }
    public int getConnect4Losses()       { return Connect4Stats.get_losses(); }
    public int getConnect4GamesPlayed()  { return Connect4Stats.getGamesPlayed(); }
    public void setConnect4Wins(int w)   { Connect4Stats.setWins(w); }
    public void setConnect4Losses(int l) { Connect4Stats.setLosses(l); }
    public void setConnect4GamesPlayed(int gp) { Connect4Stats.setGamesPlayed(gp); }
    public void setConnect4Ties(int t)   { Connect4Stats.setTies(t); }

    // TicTacToe methods:
    public int getTicTacToeWins()        { return TicTacToeStats.get_wins(); }
    public int getTicTacToeLosses()      { return TicTacToeStats.get_losses(); }
    public int getTicTacToeGamesPlayed() { return TicTacToeStats.getGamesPlayed(); }
    public void setTicTacToeWins(int w)  { TicTacToeStats.setWins(w); }
    public void setTicTacToeLosses(int l){ TicTacToeStats.setLosses(l); }
    public void setTicTacToeGamesPlayed(int gp) { TicTacToeStats.setGamesPlayed(gp); }
    public void setTicTacToeTies(int t)  { TicTacToeStats.setTies(t); }

    // Checkers methods:
    public int getCheckersWins()         { return CheckersStats.get_wins(); }
    public int getCheckersLosses()       { return CheckersStats.get_losses(); }
    public int getCheckersGamesPlayed()  { return CheckersStats.getGamesPlayed(); }
    public void setCheckersWins(int w)   { CheckersStats.setWins(w); }
    public void setCheckersLosses(int l) { CheckersStats.setLosses(l); }
    public void setCheckersGamesPlayed(int gp) { CheckersStats.setGamesPlayed(gp); }
    public void setCheckersTies(int t)   { CheckersStats.setTies(t); }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Player player = (Player) object;
        return Objects.equals(username, player.username);
    }

    /**
     * Override toString() to produce a 21-column CSV row matching readDatabase().
     * Format:
     * username, email, password,
     * [TicTacToe] gamesPlayed, wins, losses, ties, rank, MMR,
     * [Connect4] gamesPlayed, wins, losses, ties, rank, MMR,
     * [Checkers] gamesPlayed, wins, losses, ties, rank, MMR
     */
    @Override
    public String toString() {
        return String.join(",",
                // Basic info
                (username != null ? username : ""),
                (email != null ? email : ""),
                (password != null ? password : ""),

                // TicTacToe
                String.valueOf(TicTacToeStats.getGamesPlayed()),
                String.valueOf(TicTacToeStats.getWins()),
                String.valueOf(TicTacToeStats.get_losses()),
                String.valueOf(TicTacToeStats.get_ties()),
                TicTacToeStats.getRank().name(),
                String.valueOf(TicTacToeStats.getMMR()),

                // Connect4
                String.valueOf(Connect4Stats.getGamesPlayed()),
                String.valueOf(Connect4Stats.getWins()),
                String.valueOf(Connect4Stats.get_losses()),
                String.valueOf(Connect4Stats.get_ties()),
                Connect4Stats.getRank().name(),
                String.valueOf(Connect4Stats.getMMR()),

                // Checkers
                String.valueOf(CheckersStats.getGamesPlayed()),
                String.valueOf(CheckersStats.getWins()),
                String.valueOf(CheckersStats.get_losses()),
                String.valueOf(CheckersStats.get_ties()),
                CheckersStats.getRank().name(),
                String.valueOf(CheckersStats.getMMR())
        );
    }
}
