package org.seng.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.seng.gamelogic.tictactoe.OnlineTicTacToeGame;
import org.seng.gamelogic.tictactoe.TicTacToeBoard;
import org.seng.networking.SocketGameClient;

import java.io.IOException;

public class OnlineTicTacToeController {

    @FXML
    private GridPane board;

    @FXML
    private Label turnLabel;

    @FXML
    private Button inGameChatButton;

    @FXML
    private MenuButton SettingMenu;

    @FXML
    private MenuItem helpOption;

    private Button[][] buttonBoard;
    private static final int BOARD_SIZE = 3;

    private boolean myTurn;
    private String mySymbol;
    private String opponentSymbol;

    private SocketGameClient client;
    private OnlineTicTacToeGame game;

    /**
     * Initializes the controller with network references, game logic, and the role assignment.
     * @param client the network client instance
     * @param game the Tic Tac Toe game logic object
     * @param amIPlayerOne indicates if this client is player one (X) or not (O)
     */
    public void init(SocketGameClient client, OnlineTicTacToeGame game, boolean amIPlayerOne) {
        this.client = client;
        this.game = game;

        if (amIPlayerOne) {
            myTurn = true;
            mySymbol = "X";
            opponentSymbol = "O";
        } else {
            myTurn = false;
            mySymbol = "O";
            opponentSymbol = "X";
        }

        System.out.println("[debug] init amiplayerone=" + amIPlayerOne + " myturn=" + myTurn);

        createBoard();
        startNetworkListener();
        updateTurnLabel();
        updateGridEnableState();

        // Notify the server that our scene is ready
        try {
            client.sendMessage("GAME_SCENE_READY");
        } catch (IOException e) {
            System.err.println("[error] could not send GAME_SCENE_READY");
            e.printStackTrace();
        }
    }

    /**
     * Creates a 3x3 board by populating the GridPane with Buttons.
     */
    private void createBoard() {
        buttonBoard = new Button[BOARD_SIZE][BOARD_SIZE];
        board.getChildren().clear();

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Button btn = new Button();
                btn.setPrefSize(100, 100);
                btn.setFont(new Font(36));
                final int row = r, col = c;
                btn.setOnAction(e -> handleMove(row, col, btn));
                buttonBoard[r][c] = btn;
                board.add(btn, c, r);
            }
        }
    }

    /**
     * Handles a move when a button is pressed on the board.
     * @param row the row index of the move
     * @param col the column index of the move
     * @param btn the button that was pressed
     */
    private void handleMove(int row, int col, Button btn) {
        // Only process the move if it's our turn and the cell is empty
        if (!myTurn || !btn.getText().isEmpty()) return;

        boolean moveApplied = game.applyMove(row, col, mySymbol.charAt(0));
        if (moveApplied) {
            btn.setText(mySymbol);
            btn.setDisable(true);

            // Send the move to the server in the format: MOVE:TICTACTOE:row,col
            String moveMsg = "MOVE:TICTACTOE:" + row + "," + col;
            System.out.println("[debug] sending: " + moveMsg);
            try {
                client.sendMessage(moveMsg);
            } catch (IOException e) {
                showError("Failed to send move");
                e.printStackTrace();
            }

            checkAndSendGameOver(row, col, mySymbol.charAt(0));

            // Pass the turn to the opponent if the game is still in progress
            if (game.getStatus().equals("in progress")) {
                myTurn = false;
                updateTurnLabel();
                updateGridEnableState();
            }
        }
    }

    /**
     * Starts a background thread to listen for server messages.
     */
    private void startNetworkListener() {
        Thread t = new Thread(() -> {
            try {
                String msg;
                while ((msg = client.receiveMessage()) != null) {
                    final String finalMsg = msg;
                    Platform.runLater(() -> processServerMessage(finalMsg));
                }
            } catch (IOException e) {
                Platform.runLater(() -> showError("Connection lost!"));
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Processes incoming server messages.
     * @param message the server message
     */
    private void processServerMessage(String message) {
        System.out.println("[debug] received: " + message);

        if (message.startsWith("MOVE:TICTACTOE:")) {
            String data = message.substring("MOVE:TICTACTOE:".length());
            String[] coords = data.split(",");
            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);

            Button btn = buttonBoard[row][col];
            if (btn.getText().isEmpty()) {
                btn.setText(opponentSymbol);
                btn.setDisable(true);
                game.applyMove(row, col, opponentSymbol.charAt(0));
                checkAndSendGameOver(row, col, opponentSymbol.charAt(0));
                if (game.getStatus().equals("in progress")) {
                    myTurn = true;
                    updateTurnLabel();
                    updateGridEnableState();
                }
            }
        } else if (message.startsWith("GAME_OVER:")) {
            String reason = message.substring("GAME_OVER:".length());
            handleGameOver(reason);
        } else if (message.startsWith("OPPONENT_NAME:")) {
            // Optionally handle opponent name message if needed
        } else {
            System.out.println("[debug] unhandled server message: " + message);
        }
    }

    /**
     * Checks the board state after a move and sends a GAME_OVER message if necessary.
     * @param row the row index
     * @param col the column index
     * @param symbol the symbol placed ('X' or 'O')
     */
    private void checkAndSendGameOver(int row, int col, char symbol) {
        TicTacToeBoard.Mark mark = symbol == 'X' ? TicTacToeBoard.Mark.X : TicTacToeBoard.Mark.O;

        if (game.checkWinner(mark)) {
            try {
                String msg = symbol == 'X' ? "GAME_OVER:X_WINS" : "GAME_OVER:O_WINS";
                client.sendMessage(msg);
                handleGameOver(msg.substring("GAME_OVER:".length()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (game.boardFull()) {
            try {
                client.sendMessage("GAME_OVER:DRAW");
                handleGameOver("DRAW");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles game over logic and transitions to an end screen.
     * @param result the outcome string ("X_WINS", "O_WINS", or "DRAW")
     */
    private void handleGameOver(String result) {
        disableAllBoardButtons();
        if (result.equals("X_WINS")) {
            if (mySymbol.equals("X")) {
                loadWinningScene();
            } else {
                loadLosingScene();
            }
        } else if (result.equals("O_WINS")) {
            if (mySymbol.equals("O")) {
                loadWinningScene();
            } else {
                loadLosingScene();
            }
        } else if (result.equals("DRAW")) {
            loadTieScene();
        } else {
            System.out.println("[debug] unknown game over result: " + result);
        }
    }

    /**
     * Updates the turn label to indicate whose turn it is.
     */
    private void updateTurnLabel() {
        if (myTurn) {
            turnLabel.setText("Your turn (" + mySymbol + ")");
        } else {
            turnLabel.setText("Opponent's turn (" + opponentSymbol + ")");
        }
    }

    /**
     * Enables or disables buttons in the grid based on whose turn it is.
     */
    private void updateGridEnableState() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Button btn = buttonBoard[r][c];
                if (btn.getText().isEmpty()) {
                    btn.setDisable(!myTurn);
                }
            }
        }
    }

    /**
     * Displays an error message in a pop-up alert.
     * @param msg the error message
     */
    private void showError(String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * Disables all buttons on the board.
     */
    private void disableAllBoardButtons() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                buttonBoard[r][c].setDisable(true);
            }
        }
    }

    /**
     * Loads the winning scene from WinningPage.fxml.
     */
    private void loadWinningScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WinningPage.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            Stage st = (Stage) board.getScene().getWindow();
            st.setScene(scene);
            st.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the losing scene from LosingPage.fxml.
     */
    private void loadLosingScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LosingPage.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            Stage st = (Stage) board.getScene().getWindow();
            st.setScene(scene);
            st.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the tie (draw) scene from TiePage.fxml.
     */
    private void loadTieScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TiePage.fxml"));
            Scene scene = new Scene(loader.load(), 700, 450);
            Stage st = (Stage) board.getScene().getWindow();
            st.setScene(scene);
            st.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the Chat button is pressed.
     */
    @FXML
    public void openChat() {
        Alert info = new Alert(Alert.AlertType.INFORMATION, "Chat coming soon!", ButtonType.OK);
        info.setTitle("Chat");
        info.showAndWait();
    }

    /**
     * Called when the How To Play option is selected.
     */
    @FXML
    public void howToPlayDescription() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play Tic Tac Toe");
        alert.setHeaderText(null);
        alert.setContentText("Classic Tic Tac Toe rules:\n1) X always goes first\n2) ...");
        alert.showAndWait();
    }

    /**
     * Called when the Quit option is selected.
     */
    @FXML
    public void handleQuit() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to quit the current game?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Quit Game");
        confirmation.showAndWait();
        if (confirmation.getResult() == ButtonType.YES) {
            try {
                client.sendMessage("QUIT");
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();
        }
    }
}
