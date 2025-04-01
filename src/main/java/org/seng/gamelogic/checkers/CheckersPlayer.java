package main.java.org.seng.gamelogic.checkers;

import main.java.org.seng.gamelogic.HumanPlayer;
import main.java.org.seng.gamelogic.checkers.CheckersMove;
import main.java.org.seng.gamelogic.checkers.CheckersBoard;

public class CheckersPlayer extends HumanPlayer {

    // Constructor
    public CheckersPlayer(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);

    }

    @Override
    public boolean readyStart() {
        return true;
    }

    //just copy pasted this from TicTacToePlayer please review
    public boolean makeMove(CheckersBoard board, CheckersMove move) {
        return board.placeMove(move.getRow(), move.getCol(), this.symbol);
    }
}
