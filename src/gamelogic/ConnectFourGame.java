package gamelogic;

public class ConnectFourGame {

    private Board board;
    private Player[] players;  // 2 players
    private Player currentPlayer;
    private int gameID;
    private String status;

    public ConnectFourGame(Board board, Player[] players, int gameID) {
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.status = "In Progress" // placeholder for status
    }

    public void startGame() {

    }

    public void exitGame() {

    }
}

