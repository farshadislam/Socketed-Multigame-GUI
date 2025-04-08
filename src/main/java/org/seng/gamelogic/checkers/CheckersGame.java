package org.seng.gamelogic.checkers;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import org.seng.authentication.Player;
import org.seng.leaderboard_matchmaking.connect4Stats;

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

    public ExtendedAIBotCheckers AIBot;
    public char AISymbol;

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
        if (players[0] != null) players[0].setSymbol('r'); // Player 1 is always red
        if (players[1] != null) players[1].setSymbol('b'); // Player 2 is always black
    }

    /**
     * Starts the game and handles turn-based gameplay.
     */
    public void startGame() {
        System.out.println("Welcome to Console Checkers!");
        initializePlayerSymbols();
        if (players[0] instanceof ExtendedAIBotCheckers) {
            AIBot = (ExtendedAIBotCheckers) players[0];
            AISymbol = 'r';
        } else if (players[1] instanceof ExtendedAIBotCheckers) {
            AIBot = (ExtendedAIBotCheckers) players[1];
            AISymbol = 'b';
        } else {
            AIBot = null;
            AISymbol = 'n';
        }
        while (true) {
            board.printBoard();
            System.out.println((isRedTurn ? "Red" : "Black") + "'s turn");
            if (isRedTurn) {
                if (AISymbol == 'r') {
                    AIBot.nextMove(board);
                    continue;
                }
            } else {
                if (AISymbol == 'b') {
                    AIBot.nextMove(board);
                    continue;
                }
            }
            System.out.print("Enter chat message: ");
            scanner.nextLine(); // Consume newline
            String chatMessage = scanner.nextLine();
            if (!chatMessage.trim().isEmpty()) {
                sendMessage((isRedTurn ? "Red" : "Black") + ": " + chatMessage);
            }

            int fromRow = 0; // changed from scanner.nextInt, 0 is a placeholder for now - based on user selection from GUI
            int fromCol = 0;
            int toRow = 0;
            int toCol = 0;

            if (isValidTurn(fromRow, fromCol)) {
                if (board.isValidMove(fromRow, fromCol, toRow, toCol)) {
                    board.makeMove(fromRow, fromCol, toRow, toCol);
                    if (checkWinCondition()) {
                        board.printBoard();
                        System.out.println((isRedTurn ? "Red" : "Black") + " wins!");
                        if (isRedTurn) { // red (Player 1) has won
                            players[0].getCheckersStats().win(); // Player 1 wins Checkers game
                            players[1].getCheckersStats().lose(); // Player 2 loses Checkers game
                        }
                        else { // black (Player 2) has won
                            players[0].getCheckersStats().lose(); // Player 1 loses Checkers game
                            players[1].getCheckersStats().win(); // Player 2 wins Checkers game
                        }
                        break;
                    }

                    if (gameDraw(board)) { // checks if game is at a draw
                        players[0].getCheckersStats().tie();
                        players[1].getCheckersStats().tie();
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
        String message = "Player " + currentPlayer.getUsername() + "has moved a " + symbol_color + " piece from row " + row_start + " and column " + col_start + " to row " + row_end + " and column " + col_end + ".";
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

    /**
     * Checks if there is a draw in the game.
     * @return A boolean is returned. True means the game is at a draw. False means the game is not at a draw..
     */
    public boolean gameDraw(CheckersBoard board){
        boolean redHasMoves = false; // if true, then at least one red piece can move
        boolean blackHasMoves = false; // if true, then at least one black piece can move

        // iterate through every single spot in the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                CheckersBoard.Piece piece = board.getPieceAt(row, col);

                // make sure piece is a valid piece (a red piece or a black piece)
                if (piece != piece.EMPTY) {

                    // We need to first find out which team's turn it is currently.

                    // work on red team
                    if (piece == piece.RED) {
                        if (board.getPieceAt(row+1, col-1) == piece.EMPTY) {
                            redHasMoves = true;
                        }
                        else if (board.getPieceAt(row+1, col+1) == piece.EMPTY) {
                            redHasMoves = true;
                        }
                        // red piece can capture a black piece (this is a valid move)
                        else if (board.getPieceAt(row+1, col-1) == piece.BLACK || board.getPieceAt(row+1, col-1) == piece.BLACK_KING) {
                            if (board.getPieceAt(row+2, col-2) == piece.EMPTY) {
                                redHasMoves = true;
                            }
                        }
                        else if (board.getPieceAt(row+1, col+1) == piece.BLACK || board.getPieceAt(row+1, col+1) == piece.BLACK_KING) {
                            if (board.getPieceAt(row+2, col+2) == piece.EMPTY) {
                                redHasMoves = true;
                            }
                        }
                    }

                    else if (piece == piece.RED_KING) {
                        if (board.getPieceAt(row+1, col-1) == piece.EMPTY) {
                            redHasMoves = true;
                        }
                        else if (board.getPieceAt(row+1, col+1) == piece.EMPTY) {
                            redHasMoves = true;
                        }
                        else if (board.getPieceAt(row-1, col-1) == piece.EMPTY) {
                            redHasMoves = true;
                        }
                        else if (board.getPieceAt(row-1, col+1) == piece.EMPTY) {
                            redHasMoves = true;
                        }

                        // red piece can capture a black piece (this is a valid move)
                        else if (board.getPieceAt(row+1, col-1) == piece.BLACK || board.getPieceAt(row+1, col-1) == piece.BLACK_KING) {
                            if (board.getPieceAt(row+2, col-2) == piece.EMPTY) {
                                redHasMoves = true;
                            }
                        }
                        else if (board.getPieceAt(row+1, col+1) == piece.BLACK || board.getPieceAt(row+1, col+1) == piece.BLACK_KING) {
                            if (board.getPieceAt(row + 2, col + 2) == piece.EMPTY) {
                                redHasMoves = true;
                            }
                        }
                        else if (board.getPieceAt(row-1, col-1) == piece.BLACK || board.getPieceAt(row-1, col-1) == piece.BLACK_KING) {
                            if (board.getPieceAt(row-2, col-2) == piece.EMPTY) {
                                redHasMoves = true;
                            }
                        }
                        else if (board.getPieceAt(row-1, col+1) == piece.BLACK || board.getPieceAt(row-1, col+1) == piece.BLACK_KING) {
                            if (board.getPieceAt(row-2, col+2) == piece.EMPTY) {
                                redHasMoves = true;
                            }
                        }
                    }

                    // work on black team
                    else if (piece == piece.BLACK) {
                        if (board.getPieceAt(row-1, col-1) == piece.EMPTY) {
                            blackHasMoves = true;
                        }
                        else if (board.getPieceAt(row-1, col+1) == piece.EMPTY) {
                            blackHasMoves = true;
                        }
                        // black piece can capture a red piece (this is a valid move)
                        else if (board.getPieceAt(row-1, col-1) == piece.RED || board.getPieceAt(row-1, col-1) == piece.RED_KING) {
                            if (board.getPieceAt(row-2, col-2) == piece.EMPTY) {
                                blackHasMoves = true;
                            }
                        }
                        else if (board.getPieceAt(row-1, col+1) == piece.RED || board.getPieceAt(row-1, col+1) == piece.RED_KING) {
                            if (board.getPieceAt(row-2, col+2) == piece.EMPTY) {
                                blackHasMoves = true;
                            }
                        }
                    }

                    else { // Black King
                        if (board.getPieceAt(row+1, col-1) == piece.EMPTY) {
                            blackHasMoves = true;
                        }
                        else if (board.getPieceAt(row+1, col+1) == piece.EMPTY) {
                            blackHasMoves = true;
                        }
                        else if (board.getPieceAt(row-1, col-1) == piece.EMPTY) {
                            blackHasMoves = true;
                        }
                        else if (board.getPieceAt(row-1, col+1) == piece.EMPTY) {
                            blackHasMoves = true;
                        }
                    }
                }
            }
        }

        // We need to check if this team can even make a move in the first place.
        // If the team doesn't have a piece that can make a move, the game would result in a tie/draw

        // A piece can move in two different ways:

        // Red king AND black king moves up OR down 1 square diagonal
        // A piece can move diagonally. Non-king pieces move forward diagonally. King pieces move forward or backward diagonally.
        // A piece also move when capturing the opposing team's piece.
        // The space 2 spots diagonally has to be empty to be able to capture

        // If either redHasMoves OR blackHasMoves is false gameDraw() is true
        if (!redHasMoves || !blackHasMoves) {
            return true;
        }

        return false;

    }


}