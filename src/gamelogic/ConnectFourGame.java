package gamelogic;

import java.util.List;

public class ConnectFourGame {

    public Board board;
    public Player[] players;  // 2 players
    public Player currentPlayer;
    public int gameID;
    public String status;
    public List<String> chatLog;

    public ConnectFourGame(Board board, Player[] players, int gameID) {
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.status = "In Progress"; // placeholder for status
        this.chatLog = new ArrayList<>();
        this.currentPlayer = players[0]; // first player starts, this may be changed to implement a RNG decision?
    }

    public bool connectPlayers() {

    }

    public void startGame() {

    }

    public void exitGame() {

    }
}

