package org.seng.gamelogic.tictactoe;
import org.seng.gamelogic.AIBot;
import org.seng.gamelogic.tictactoe.TicTacToeBoard;
import org.seng.gamelogic.tictactoe.TicTacToeBoard.Mark;
import org.seng.gamelogic.Player;
import org.seng.gamelogic.tictactoe.TicTacToeMove;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ExtendedAIBotTicTacToe extends AIBot {
    private TicTacToeBoard board; // BH: changed from boardRef to board to keep names simple/consistent
    private TicTacToeGame game;
    private Mark symbol;

    /** BH Review comments: Added TicTacToeBoard as an argument to the constructor. Do we need to do this for
     * the other games' ExtendedAIBot classes too? Also I don't think we need the name, accountID, rank arguments
     * for an AI bot. */
    // constructor
    public ExtendedAIBotTicTacToe(Mark symbol, TicTacToeGame game, TicTacToeBoard board) {
        super("AIBot", 0, 0, 0);
        this.board = board;
        this.game = game;
        this.symbol = symbol; // either 'X' or 'O'
    }


    /**
     * @param
     * */
    public boolean makeMove(TicTacToeBoard board, TicTacToeMove move) {
        if (this.board != board) {
    //        System.out.println("Board does not match");
            return false;
        }
      //  Mark moveSymbol = chartoMove(move.getSymbol());

        if (move == null || move.getSymbol() != marktoChar(this.symbol)) {
            return false;
        }
        if (board.makeMove(move.getRow(), move.getCol(), this.symbol)) {
            return true;
        }
        return false; // if AiBot failed to place a move

    }


    /** BH Review comments: maybe the nextMove() method is not needed...? */
    // AI decides best move given current state of the board
    public int[] findBestMove(TicTacToeBoard board) {
        //collect empty spots on the board
        List<int[]> emptySpots = new ArrayList<>();
        for (int r = 0; r < TicTacToeBoard.SIZE; r++) {
            for (int c = 0; c < TicTacToeBoard.SIZE; c++) {
                if (board.validMove(r, c)) {
                    emptySpots.add(new int[]{r, c});
                }
            }
        }
        //checking if there is any available moves
        if (emptySpots.isEmpty()) {
            return null;
        }

        return emptySpots.get(new Random().nextInt(emptySpots.size()));
    }


    @Override
    public Object nextMove(Object board) {
        return board; // placeholder
    }

    @Override
    public void setSymbol(char symbol) {
        this.symbol = chartoMark(symbol);
    }

    @Override
    public char getSymbol() {
        return marktoChar(this.symbol);
    }


    /**
     * convert a char symbol X or 0 to a Mark
     *
     * @param symbol The char symbol to convert
     * @return Mark.X, Mark.O, or Mark.EMPTY
     */
    private Mark chartoMark(char symbol) {
        if (symbol == 'X') {
            return Mark.X;
        } else if (symbol == 'O') {
            return Mark.O;
        }
        return null;
    }

    /**
     * converts a Mark to a char symbol
     *
     * @param mark The Mark to convert
     * @return X, O, or ' '
     */
    private char marktoChar(Mark mark) {
        if (mark == Mark.X) {
            return 'X';
        } else if (mark == Mark.O) {
            return 'O';
        }
        return ' ';
    }
}
