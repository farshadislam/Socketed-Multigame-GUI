package org.seng.gamelogic.tictactoe;
import org.seng.gamelogic.AIBot;
import org.seng.gamelogic.tictactoe.TicTacToeBoard;
import org.seng.gamelogic.Player;
import org.seng.gamelogic.tictactoe.TicTacToeMove;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ExtendedAIBotTicTacToe extends AIBot {
    private TicTacToeBoard board; // BH: changed from boardRef to board to keep names simple/consistent
    private TicTacToeGame game;

    /** BH Review comments: Added TicTacToeBoard as an argument to the constructor. Do we need to do this for
     * the other games' ExtendedAIBot classes too? Also I don't think we need the name, accountID, rank arguments
     * for an AI bot. */
    // constructor
    public ExtendedAIBotTicTacToe(String name, int accountID, char symbol, int rank, TicTacToeGame game, TicTacToeBoard board) {
        super(name, accountID, symbol, rank);
        this.board = board;
        this.game = game;
        this.symbol = symbol; // either 'X' or 'O'
    }


    public boolean makeMove(Object board, Object move) {
        if (this.board != board) {
            return false;
        }

        // make the best move


        return true;
    }


    /** BH Review comments: maybe the nextMove() method is not needed...? */
    // AI decides best move given current state of the board
    public int[] findBestMove(TicTacToeBoard board) {

        // Priority 1: winning move

        // Priority 2: block human player's winning move

        // Priority 3: random

        // no moves left
        return null;
    }


    @Override
    public Object nextMove(Object board) {
        return board; // placeholder
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public char getSymbol() {
        return this.symbol;
    }
}
