package org.seng.gamelogic.checkers;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

/**
 * Represents a game of checkers following basic game logic.
 * In our version of the game, black pieces are at the bottom and red pieces are the top of the board.
 * Red piece, which is always assigned to Player 1, will move first.
 * A piece can capture multiple pieces, but each capture is considered one turn. In this case, when a player's piece
 * can capture more than once, then the turn will remain on that current player. The turn will switch only
 * after the player can no longer capture any of the opponent's pieces.
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
    public CheckersGame(CheckersBoard board, CheckersPlayer[] players, int gameID) {
        this.board = board;
        this.players = players;
        this.gameID = gameID;
        this.status = "Initialized";
        this.chatLog = new ArrayList<>();
        this.currentPlayer = players[0]; // First player starts
        isRedTurn = true; // First player starts
        scanner = new Scanner(System.in);
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

        initializePlayerSymbols();

        // checks if the human player has selected to play against AI bot
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

        // game loop starts
        while (true) {
            board.display(); // to be integrated with GUI
            System.out.println((isRedTurn ? "Red" : "Black") + "'s turn"); // to be integrated with GUI

            // AI bot
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

                // make a move
                board.makeMove(fromRow, fromCol, toRow, toCol);

                // a Player wins. Update player stats and break out of game loop
                if (checkWinCondition()) {
                    board.display();
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

                // no one wins - a tie. Update player stats and break out of game loop
                if (gameDraw()) {
                    players[0].getCheckersStats().tie();
                    players[1].getCheckersStats().tie();
                    break;
                }

                // If the played piece has just captured an opponent's piece AND it can still capture more pieces, otherwise switch turn
                //     - Use isJumpMove(...) to check if there was a piece captured
                //     - Use getCapturablePieces(...) in CheckersBoard class to check if the piece at the new position can capture
                //       more opponent pieces
                //          - if it cannot, then that means getCapturablePieces(...) returns an empty list
                //          - the if-statement below makes sure that getCapturablePieces(...) is not empty
                if (!isJumpMove(fromRow, fromCol, toRow, toCol) && board.getCapturablePieces(toRow, toCol, board.getPieceAt(toRow, toCol)).isEmpty()) {
                    // do nothing so that in the next turn, same player makes a move to capture multiple pieces
                }
                else {
                    switchPlayer();
                }
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
     * Checks if a piece has been captured
     * @return True if piece has captured an opponent's piece, false otherwise
     */
    public boolean isJumpMove(int fromRow, int fromCol, int toRow, int toCol) {
        return Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 2;
    }

    // Switches turn to the next player/piece
    public void switchPlayer() {
        if (players[0] == currentPlayer) {
            currentPlayer = players[1];
            isRedTurn = false;
        } else {
            currentPlayer = players[0];
            isRedTurn = true;
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
     * Checks if there is a draw in the game.
     * @return A boolean is returned. True means the game is at a draw. False means the game is not at a draw.
     */
    public boolean gameDraw(){
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

                        // black piece can capture a red piece (this is a valid move)
                        else if (board.getPieceAt(row+1, col-1) == piece.RED || board.getPieceAt(row+1, col-1) == piece.RED_KING) {
                            if (board.getPieceAt(row+2, col-2) == piece.EMPTY) {
                                blackHasMoves = true;
                            }
                        }
                        else if (board.getPieceAt(row+1, col+1) == piece.RED || board.getPieceAt(row+1, col+1) == piece.RED_KING) {
                            if (board.getPieceAt(row + 2, col + 2) == piece.EMPTY) {
                                blackHasMoves = true;
                            }
                        }
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
                }
            }
        }

        // If either redHasMoves OR blackHasMoves is false gameDraw() is true
        if (!redHasMoves || !blackHasMoves) {
            return true;
        }

        return false;
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

}