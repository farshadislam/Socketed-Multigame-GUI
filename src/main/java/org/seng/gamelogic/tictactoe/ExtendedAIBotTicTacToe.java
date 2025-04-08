package org.seng.gamelogic.tictactoe;
import org.seng.gamelogic.tictactoe.TicTacToeBoard.Mark;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ExtendedAIBotTicTacToe extends TicTacToePlayer {
    private TicTacToeBoard board; // BH: changed from boardRef to board to keep names simple/consistent
    private TicTacToeGame game;
    private Mark symbol;
    private Random random;


    public ExtendedAIBotTicTacToe(Mark symbol, TicTacToeGame game, TicTacToeBoard board) {
        super("AI Bot", "hi", "9"); // passes username, player ID, symbol as char, rank
        this.board = board;
        this.game = game;
        this.symbol = symbol; // either 'X' or 'O'
        this.random = new Random();
    }


    /**
     * @param board TheTicTacToeBoard for AIBot.
     * @param game TheTicTacToeGame that have row, column and symbol.
     * @return true if the move was successful, if not false.
     * */
    public boolean makeMove(TicTacToeBoard board, TicTacToeGame game) {
        if (this.board != board) {
    //        System.out.println("Board does not match");
            return false;
        }
        // ensure its not null, and matches AI symbol
        if (game == null || game.getCurrentMark() != this.symbol) {
            return false;
        }
        int[] move = findNextMove(board);
        if (move != null) {
            if (game.makeMove(move[0], move[1])) {
                return true;
            }
        }
        return false; // if AiBot failed to place a move

    }

    /**
     * @param board TheTicTacToeBoard to find a move
     * @return int array representing the next move, null if no moves are available
     * */
    // AI decides best move given current state of the board
    public int[] findNextMove(TicTacToeBoard board) {
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

        //pick a random spot
        return emptySpots.get(random.nextInt(emptySpots.size()));
    }



    /*
     * @param board TheTicTacToeBoard to generate a move
     * @return TicTacToeMove object for a next move, or null if no moves are available, or not AI's turn
     * */
//
//    public TicTacToeBoard nextMove(TicTacToeBoard board) {
//        Mark mark = game.getCurrentMark();
//        if (game.getCurrentMark() != charToMark(this.symbol)) {
//            return null; // not AI's turn
//        }
//        int[] move = findNextMove(board);
//        if (move == null) {
//            return null;
//        }
//        //convert the AI's mark to char for TicTacToeMove
//        return new TicTacToeMove(move[1], move[0], this.symbol);
//    }



    /**
     * convert a char symbol X or 0 to a Mark
     *
     * @param symbol The char symbol to convert
     * @return Mark.X, Mark.O, or Mark.EMPTY
     */
    private Mark charToMark(char symbol) {
        if (symbol == 'X') {
            return Mark.X;
        } else if (symbol == 'O') {
            return Mark.O;
        }
        return Mark.EMPTY;
    }


    private char markToChar(Mark mark) {
        if (mark == Mark.X) {
            return 'X';
        } else if (mark == Mark.O) {
            return 'O';
        }
        return ' ';
    }

    public void setSymbol(char symbol) {
        this.symbol = charToMark(symbol);
    }


    public char getSymbol() {
        return markToChar(this.symbol);
    }
}
