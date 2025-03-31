package gamelogic.tictactoe;

public class TicTacToeBoard {

    public static final int SIZE = 3;

    public enum Mark {
        EMPTY, X, O
    }

    private Mark[][] grid;

    public TicTacToeBoard() {
        grid = new Mark[SIZE][SIZE];
        initializeBoard();
    }

}
