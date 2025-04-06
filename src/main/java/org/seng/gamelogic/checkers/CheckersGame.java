package org.seng.gamelogic.checkers;

import org.seng.gamelogic.Player;
import org.seng.gamelogic.connectfour.ConnectFourBoard;

import java.util.ArrayList;
import java.util.Scanner;

public class CheckersGame {

    private CheckersBoard board;
    private boolean isRedTurn;
    private Scanner scanner;
    public CheckersPlayer[] players;  // 2 players
    public CheckersPlayer currentPlayer;
    public int gameID;
    public String status;
    public ArrayList<String> chatLog;

    public CheckersGame(CheckersBoard board, CheckersPlayer[] players, int gameID) {
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.status = "In Progress"; // placeholder for status
        this.chatLog = new ArrayList<>();
        this.currentPlayer = players[0]; // first player starts, this may be changed to implement a RNG decision?
        isRedTurn = true;
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to Console Checkers!");
        while (true) {
            board.printBoard();
            initializePlayerSymbols();
            System.out.println((isRedTurn ? "Red" : "Black") + "'s turn");

            System.out.print("Enter move (fromRow fromCol toRow toCol): ");
            int fromRow = scanner.nextInt();
            int fromCol = scanner.nextInt();
            int toRow = scanner.nextInt();
            int toCol = scanner.nextInt();

            if (isValidTurn(fromRow, fromCol)) {
                if (board.isValidMove(fromRow, fromCol, toRow, toCol)) {
                    board.makeMove(fromRow, fromCol, toRow, toCol);
                    if (checkWinCondition()) {
                        board.printBoard();
                        System.out.println((isRedTurn ? "Red" : "Black") + " wins!");
                        break;
                    }
                    isRedTurn = !isRedTurn;
                } else {
                    System.out.println("Invalid move. Try again.");
                }
            } else {
                System.out.println("That's not your piece. Try again.");
            }
        }

        scanner.close();
    }

    private boolean isValidTurn(int row, int col) {
        CheckersBoard.Piece piece = board.getPieceAt(row, col);
        if (isRedTurn) {
            return piece == CheckersBoard.Piece.RED || piece == CheckersBoard.Piece.RED_KING;
        } else {
            return piece == CheckersBoard.Piece.BLACK || piece == CheckersBoard.Piece.BLACK_KING;
        }
    }

    private boolean checkWinCondition() {
        boolean redExists = false;
        boolean blackExists = false;

        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                CheckersBoard.Piece piece = board.getPieceAt(row, col);
                if (piece == CheckersBoard.Piece.RED || piece == CheckersBoard.Piece.RED_KING) {
                    redExists = true;
                }
                if (piece == CheckersBoard.Piece.BLACK || piece == CheckersBoard.Piece.BLACK_KING) {
                    blackExists = true;
                }
            }
        }

        return isRedTurn ? !blackExists : !redExists;
    }

    public static void main(String[] args) {
        CheckersGame game = new CheckersGame();
        game.start();
    }

    private void initializePlayerSymbols() {
        players[0].symbol = 'r';
        players[1].symbol = 'b';
    }

}
