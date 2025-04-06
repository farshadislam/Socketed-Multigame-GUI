package org.seng.gamelogic.checkers;

import org.seng.gamelogic.Player;
import org.seng.gamelogic.checkers.CheckersMove;
import org.seng.gamelogic.checkers.CheckersBoard;

public class CheckersPlayer extends Player {

    // Constructor
    public CheckersPlayer(String username, int playerID, char symbol, int rank) {
        super(username, playerID, symbol, rank);

    }

    public boolean readyStart() {
        return true;
    }


    public boolean makeMove(CheckersBoard board, CheckersMove move) {
        int rowStart = move.getRowStart();
        int rowEnd = move.getRowEnd();
        int colStart = move.getColStart();
        int colEnd = move.getColEnd();
        boolean movePiece = board.makeMove(rowStart, colStart, rowEnd, colEnd);
        return movePiece;
    }
}
