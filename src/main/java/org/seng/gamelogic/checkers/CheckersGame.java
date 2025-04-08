package org.seng.gamelogic.checkers;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

/**
 * Represents a game of checkers following basic game logic.
 */
public class CheckersGame {

    private CheckersBoard board;
    private boolean isRedTurn;
    private Scanner scanner;
    public CheckersPlayer[] players;  // Each game can only have 2 players
    public CheckersPlayer currentPlayer;
    public int gameID;
    public String status;
    public ArrayList<String> chatLog;

    /**
     * Initializes a new checkers game with default settings.
     */
    public CheckersGame() {
        this.board = new CheckersBoard();
        this.players = new CheckersPlayer[2];
        this.gameID = 0; // Default game ID, should be set properly
        this.status = "Initialized";
        this.chatLog = new ArrayList<>();
        this.currentPlayer = players[0]; // First player starts
        isRedTurn = true;
        scanner = new Scanner(System.in);
    }

    public CheckersGame(CheckersBoard board, CheckersPlayer[] checkersPlayers, int i) {
    }

    /**
     * Initializes player symbols for red and black pieces.
     */
    private void initializePlayerSymbols() {
        if (players[0] != null) players[0].symbol = 'r';
        if (players[1] != null) players[1].symbol = 'b';
    }

    /**
     * Starts the game and handles turn-based gameplay.
     */
    public void startGame() {
        System.out.println("Welcome to Console Checkers!");
        while (true) {
            board.printBoard();
            initializePlayerSymbols();
            System.out.println((isRedTurn ? "Red" : "Black") + "'s turn");

            System.out.print("Enter chat message: ");
            scanner.nextLine(); // Consume newline
            String chatMessage = scanner.nextLine();
            if (!chatMessage.trim().isEmpty()) {
                sendMessage((isRedTurn ? "Red" : "Black") + ": " + chatMessage);
            }

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
                        if (isRedTurn) {
                            // need to put something here
                        }
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

    /**
     * Checks if the selected piece belongs to the current player.
     * @param row The row index of the piece.
     * @param col The column index of the piece.
     * @return True if the piece belongs to the current player, false otherwise.
     */
    public boolean isValidTurn(int row, int col) {
        CheckersBoard.Piece piece = board.getPieceAt(row, col);
        if (isRedTurn) {
            return piece == CheckersBoard.Piece.RED || piece == CheckersBoard.Piece.RED_KING;
        } else {
            return piece == CheckersBoard.Piece.BLACK || piece == CheckersBoard.Piece.BLACK_KING;
        }
    }

    /**
     * Checks if the game has been won.
     * @return True if one player has no pieces left, false otherwise.
     */
    public boolean checkWinCondition() {
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

    public void setStatus(int row_start, int col_start, int row_end, int col_end) {
        String symbol_color;
        if (isRedTurn) {
            currentPlayer = players[0];
            symbol_color = "red";
        } else {
            currentPlayer = players[1];
            symbol_color = "black";
        }
        String message = "Player " + currentPlayer.getName() + "has moved a " + symbol_color + " piece from row " + row_start + " and column " + col_start + " to row " + row_end + " and column " + col_end + ".";
        status = message;
    }

    /**
     * Sends a chat message and logs it.
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            chatLog.add(message);
            System.out.println("Chat: " + message);
        }
    }

    /**
     * Retrieves the chat log.
     * @return A list of chat messages.
     */
    public List<String> getChatLog() {
        return new ArrayList<>(chatLog);
    }

    /**
     * Main method to startGame the checkers game.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        CheckersGame game = new CheckersGame();
        game.startGame();
    }




}
