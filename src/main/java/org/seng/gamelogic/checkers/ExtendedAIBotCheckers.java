package org.seng.gamelogic.checkers;

import org.seng.gamelogic.checkers.CheckersBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ExtendedAIBotCheckers extends CheckersPlayer {
    private CheckersGame game;
    private CheckersBoard board;
    private Random random;

    public ExtendedAIBotCheckers(char symbol, CheckersGame game, CheckersBoard board) {
        super("AIBot", 0, symbol, 0);
        this.game = game;
        this.board = board;
        this.random = new Random();
    }


    public int[] nextMove(Object boardObj) {
        if (!(boardObj instanceof CheckersBoard))
            return null; // placeholder

        CheckersBoard board = (CheckersBoard) boardObj;

        List<int[]> validMoves = getAllValidMoves(board);
        if (validMoves.isEmpty()) {
            return null;
        }

        int[] move = validMoves.get(random.nextInt(validMoves.size()));
        return move;
    }

    public boolean makeMove(Object boardObj, Object moveObj) {
    if (!(boardObj instanceof CheckersBoard) || !(moveObj instanceof int[])){
        return false;
    }
    CheckersBoard board = (CheckersBoard) boardObj;
    int[] move = (int[]) moveObj;

    return board.makeMove(move[0], move[1], move[2], move[3]);
    }

    private List<int[]> getAllValidMoves(CheckersBoard board) {
        List<int[]> moves = new ArrayList<>();
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                CheckersBoard.Piece piece = board.getPieceAt(row, col);
                if (isAIPiece(piece)) {
                    addValidMovesForPiece(board, row, col, moves);
                }
            }
        }
        return moves;
    }


    private boolean isAIPiece(CheckersBoard.Piece piece) {
        return (piece == CheckersBoard.Piece.BLACK) || (piece == CheckersBoard.Piece.BLACK_KING);
    }
    private void addValidMovesForPiece(CheckersBoard board, int row, int col, List<int[]> moves) {
        int[][] directions = {{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            if (board.isValidMove(row, col, newRow, newCol)) {
                moves.add(new int[]{row, col, newRow, newCol});
            }
        }
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
