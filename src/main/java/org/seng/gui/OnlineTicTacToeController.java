package org.seng.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.seng.gamelogic.tictactoe.OnlineTicTacToeGame;
import org.seng.networking.SocketGameClient;

import java.io.IOException;

public class OnlineTicTacToeController {

    @FXML private GridPane board;
    @FXML private Label turnLabel;

    private Button[][] buttonBoard;
    private static final int BOARD_SIZE = 3;

    private boolean myTurn;
    private String mySymbol;
    private String opponentSymbol;

    private SocketGameClient client;
    private OnlineTicTacToeGame game;

    /** this initializes the controller with network references + the game logic and role assignment */
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

        // this notifies the server that our scene is ready
        try {
            client.sendMessage("GAME_SCENE_READY");
        } catch (IOException e) {
            System.err.println("[error] could not send GAME_SCENE_READY");
            e.printStackTrace();
        }
    }

    /** this creates the 3x3 board of buttons */
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

    /** this handles a local move */
    private void handleMove(int row, int col, Button btn) {
        // this only does something if its our turn and the cell is empty
        if (!myTurn) return;
        if (!btn.getText().isEmpty()) return;

        boolean moveApplied = game.applyMove(row, col, mySymbol.charAt(0));
        if (moveApplied) {
            btn.setText(mySymbol);
            btn.setDisable(true);

            // this sends the move to the server
            String moveMsg = "MOVE:TICTACTOE:" + row + "," + col;
            System.out.println("[debug] sending: " + moveMsg);
            try {
                client.sendMessage(moveMsg);
            } catch (IOException e) {
                showError("failed to send move");
                e.printStackTrace();
            }

            checkAndSendGameOver();

            // this passes the turn to the opponent
            if (game.getStatus().equals("In Progress")) {
                myTurn = false;
                updateTurnLabel();
                updateGridEnableState();
            }
        }
    }

    /** this starts a thread to read server messages */
    private void startNetworkListener() {
        Thread t = new Thread(() -> {
            try {
                String msg;
                while ((msg = client.receiveMessage()) != null) {
                    final String finalMsg = msg;
                    Platform.runLater(() -> processServerMessage(finalMsg));
                }
            } catch (IOException e) {
                Platform.runLater(() -> showError("connection lost!"));
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /** this processes messages from the server */
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
                checkAndSendGameOver();
                if (game.getStatus().equals("In Progress")) {
                    myTurn = true;
                    updateTurnLabel();
                    updateGridEnableState();
                }
            }
        } else if (message.startsWith("GAME_OVER:")) {
            // this handles a game condition
            String reason = message.substring("GAME_OVER:".length());
            handleGameOver(reason);
        } else if (message.startsWith("OPPONENT_NAME:")) {
            // no action for opponent name message yet
        } else {
            System.out.println("[debug] unhandled server message: " + message);
        }
    }

    /** this checks if the local game logic says the game ended and sends a game over message */
    private void checkAndSendGameOver() {
        String status = game.getStatus();
        if (!status.equals("In Progress")) {
            String msg;
            if (status.contains("X Wins")) {
                msg = "GAME_OVER:X_WINS";
            } else if (status.contains("O Wins")) {
                msg = "GAME_OVER:O_WINS";
            } else {
                msg = "GAME_OVER:DRAW";
            }
            try {
                client.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handleGameOver(msg.substring("GAME_OVER:".length()));
        }
    }

    /** this is the local function to handle a final result and show the end screen */
    private void handleGameOver(String result) {
        // result might be x_wins o_wins or draw
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

    /** this updates the turn label based on myturn */
    private void updateTurnLabel() {
        if (myTurn) {
            turnLabel.setText("your turn (" + mySymbol + ")");
        } else {
            turnLabel.setText("opponent's turn (" + opponentSymbol + ")");
        }
    }

    /** this enables or disables all empty cells depending on whether its the players turn */
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

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    /** this disables the entire board */
    private void disableAllBoardButtons() {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                buttonBoard[r][c].setDisable(true);
            }
        }
    }

    /** this loads the winning scene winningpage.fxml */
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

    /** this loads the losing scene losingpage.fxml */
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

    /** this loads the tie scene tiepage.fxml */
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

    @FXML
    public void openChat() {
        Alert info = new Alert(Alert.AlertType.INFORMATION, "chat coming soon!", ButtonType.OK);
        info.setTitle("chat");
        info.showAndWait();
    }

    @FXML
    public void howToPlayDescription() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("how to play tic tac toe");
        alert.setHeaderText(null);
        alert.setContentText("classic tic tac toe rules ...\n1) x always goes first\n2) ... ");
        alert.showAndWait();
    }

    @FXML
    public void handleQuit() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "are you sure you want to quit the current game",
                ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("quit game");
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
