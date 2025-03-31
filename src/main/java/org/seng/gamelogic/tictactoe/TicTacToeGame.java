package gamelogic.tictactoe;
import gamelogic.tictactoe.TicTacToeBoard;
import gamelogic.tictactoe.TicTacToeBoard.Mark;

public class TicTacToeGame {

    private TicTacToeBoard board;
    private Mark currPlayer;
    private String status; // Can be "In Progress", "X Wins", "O Wins", "Draw"

    public TicTacToeGame() {
        board = new TicTacToeBoard();
        currPlayer = Mark.X; // X starts
        status = "In Progress";
    }

    public boolean makeMove(int row, int col) {
        if (status.equals("In Progress") && board.validMove(row, col)) {
            board.makeMove(row, col, currPlayer);

            if (checkWinner(currPlayer)) {
                status = currPlayer + " Wins";
            } else if (isBoardFull()) {
                status = "Draw";
            } else {
                switchTurn();
            }
            return true;
        }
        return false;
    }

    public boolean checkWinner(Mark mark) {
        for (int i = 0; i < TicTacToeBoard.SIZE; i++) {
            //check if there is a winner in rows and columns
            if ((board.getMark(i, 0) == mark && board.getMark(i, 1) == mark && board.getMark(i, 2) == mark) ||
                    (board.getMark(0, i) == mark && board.getMark(1, i) == mark && board.getMark(2, i) == mark)) {
                return true;
            }
        }

        //check if there is a winner in a diagonal
        return (board.getMark(0, 0) == mark && board.getMark(1, 1) == mark && board.getMark(2, 2) == mark) ||
                (board.getMark(0, 2) == mark && board.getMark(1, 1) == mark && board.getMark(2, 0) == mark);
    }

    public boolean boardFull() {
        //checks for a draw
        for (int row = 0; row < TicTacToeBoard.SIZE; row++) {
            for (int col = 0; col < TicTacToeBoard.SIZE; col++) {
                if (board.getMark(row, col) == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public void switchTurn() {
    }

    public TicTacToeBoard getBoard() {
        return board;
    }

    public Mark getCurrentPlayer() {
        return currPlayer;
    }

    public String getStatus() {
        return status;
    }

    public void resetGame() {
        board.resetBoard();
        currPlayer = Mark.X;
        status = "In Progress";
    }
}
