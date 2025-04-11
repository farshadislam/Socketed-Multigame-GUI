package org.seng.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckersBoardController {

    @FXML
    private Button a1, a2, a3, a4, a5, a6, a7, a8;
    @FXML
    private Button b1, b2, b3, b4, b5, b6, b7, b8;
    @FXML
    private Button c1, c2, c3, c4, c5, c6, c7, c8;
    @FXML
    private Button d1, d2, d3, d4, d5, d6, d7, d8;
    @FXML
    private Button e1, e2, e3, e4, e5, e6, e7, e8;
    @FXML
    private Button f1, f2, f3, f4, f5, f6, f7, f8;
    @FXML
    private Button g1, g2, g3, g4, g5, g6, g7, g8;
    @FXML
    private Button h1, h2, h3, h4, h5, h6, h7, h8;

    @FXML
    private Button inGameChatButton;

    @FXML
    private MenuItem helpOption;

    @FXML
    private FlowPane board;

    @FXML private Label turnLabel;

    @FXML private Label timerLabel;

    private boolean isPlayerBTurn = true; // black goes first
    private boolean canMultiCapture = false;
    private boolean AIBot;
    public void setAIBot(boolean AIBot) {
        this.AIBot = AIBot;
    }

    private Button selectedPiece = null;
    private Button capturedPiece = null;
    private Button[][] buttonBoard;

    private Timeline timeline;
    private Timeline countdownTimeline;
    private Image redPieceImage;
    private Image blackPieceImage;
    private Image redKingPieceImage;
    private Image blackKingPieceImage;


    @FXML
    public void initialize() {
        // Load images
        redPieceImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/redpiece.png"));
        blackPieceImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/blackpiece.png"));
        redKingPieceImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/RedPieceKing.png"));
        blackKingPieceImage = new Image(getClass().getResourceAsStream("/org/seng/gui/images/BlackPieceKing.png"));


        setupPieces();
        clearChatHistory();

        Button[] row1 = {a1, a2, a3, a4, a5, a6, a7, a8};
        Button[] row2 = {b1, b2, b3, b4, b5, b6, b7, b8};
        Button[] row3 = {c1, c2, c3, c4, c5, c6, c7, c8};
        Button[] row4 = {d1, d2, d3, d4, d5, d6, d7, d8};
        Button[] row5 = {e1, e2, e3, e4, e5, e6, e7, e8};
        Button[] row6 = {f1, f2, f3, f4, f5, f6, f7, f8};
        Button[] row7 = {g1, g2, g3, g4, g5, g6, g7, g8};
        Button[] row8 = {h1, h2, h3, h4, h5, h6, h7, h8};
        buttonBoard = new Button[][]{row1, row2, row3, row4, row5, row6, row7, row8};

        isPlayerBTurn = true;
    }

    private void setupPieces() {
        // Black pieces
        placePiece(b6, blackPieceImage); placePiece(d6, blackPieceImage);
        placePiece(f6, blackPieceImage); placePiece(h6, blackPieceImage);
        placePiece(a7, blackPieceImage); placePiece(c7, blackPieceImage);
        placePiece(e7, blackPieceImage); placePiece(g7, blackPieceImage);
        placePiece(b8, blackPieceImage); placePiece(d8, blackPieceImage);
        placePiece(f8, blackPieceImage); placePiece(h8, blackPieceImage);

        // Red pieces
        placePiece(a1, redPieceImage); placePiece(c1, redPieceImage);
        placePiece(e1, redPieceImage); placePiece(g1, redPieceImage);
        placePiece(b2, redPieceImage); placePiece(d2, redPieceImage);
        placePiece(f2, redPieceImage); placePiece(h2, redPieceImage);
        placePiece(a3, redPieceImage); placePiece(c3, redPieceImage);
        placePiece(e3, redPieceImage); placePiece(g3, redPieceImage);

        a1.setOnAction(e -> handleButtonClick(0, 0, a1));
        a2.setOnAction(e -> handleButtonClick(0, 1, a2));
        a3.setOnAction(e -> handleButtonClick(0, 2, a3));
        a4.setOnAction(e -> handleButtonClick(0, 3, a4));
        a5.setOnAction(e -> handleButtonClick(0, 4, a5));
        a6.setOnAction(e -> handleButtonClick(0, 5, a6));
        a7.setOnAction(e -> handleButtonClick(0, 6, a7));
        a8.setOnAction(e -> handleButtonClick(0, 7, a8));

        b1.setOnAction(e -> handleButtonClick(1, 0, b1));
        b2.setOnAction(e -> handleButtonClick(1, 1, b2));
        b3.setOnAction(e -> handleButtonClick(1, 2, b3));
        b4.setOnAction(e -> handleButtonClick(1, 3, b4));
        b5.setOnAction(e -> handleButtonClick(1, 4, b5));
        b6.setOnAction(e -> handleButtonClick(1, 5, b6));
        b7.setOnAction(e -> handleButtonClick(1, 6, b7));
        b8.setOnAction(e -> handleButtonClick(1, 7, b8));

        c1.setOnAction(e -> handleButtonClick(2, 0, c1));
        c2.setOnAction(e -> handleButtonClick(2, 1, c2));
        c3.setOnAction(e -> handleButtonClick(2, 2, c3));
        c4.setOnAction(e -> handleButtonClick(2, 3, c4));
        c5.setOnAction(e -> handleButtonClick(2, 4, c5));
        c6.setOnAction(e -> handleButtonClick(2, 5, c6));
        c7.setOnAction(e -> handleButtonClick(2, 6, c7));
        c8.setOnAction(e -> handleButtonClick(2, 7, c8));

        d1.setOnAction(e -> handleButtonClick(3, 0, d1));
        d2.setOnAction(e -> handleButtonClick(3, 1, d2));
        d3.setOnAction(e -> handleButtonClick(3, 2, d3));
        d4.setOnAction(e -> handleButtonClick(3, 3, d4));
        d5.setOnAction(e -> handleButtonClick(3, 4, d5));
        d6.setOnAction(e -> handleButtonClick(3, 5, d6));
        d7.setOnAction(e -> handleButtonClick(3, 6, d7));
        d8.setOnAction(e -> handleButtonClick(3, 7, d8));

        e1.setOnAction(e -> handleButtonClick(4, 0, e1));
        e2.setOnAction(e -> handleButtonClick(4, 1, e2));
        e3.setOnAction(e -> handleButtonClick(4, 2, e3));
        e4.setOnAction(e -> handleButtonClick(4, 3, e4));
        e5.setOnAction(e -> handleButtonClick(4, 4, e5));
        e6.setOnAction(e -> handleButtonClick(4, 5, e6));
        e7.setOnAction(e -> handleButtonClick(4, 6, e7));
        e8.setOnAction(e -> handleButtonClick(4, 7, e8));

        f1.setOnAction(e -> handleButtonClick(5, 0, f1));
        f2.setOnAction(e -> handleButtonClick(5, 1, f2));
        f3.setOnAction(e -> handleButtonClick(5, 2, f3));
        f4.setOnAction(e -> handleButtonClick(5, 3, f4));
        f5.setOnAction(e -> handleButtonClick(5, 4, f5));
        f6.setOnAction(e -> handleButtonClick(5, 5, f6));
        f7.setOnAction(e -> handleButtonClick(5, 6, f7));
        f8.setOnAction(e -> handleButtonClick(5, 7, f8));

        g1.setOnAction(e -> handleButtonClick(6, 0, g1));
        g2.setOnAction(e -> handleButtonClick(6, 1, g2));
        g3.setOnAction(e -> handleButtonClick(6, 2, g3));
        g4.setOnAction(e -> handleButtonClick(6, 3, g4));
        g5.setOnAction(e -> handleButtonClick(6, 4, g5));
        g6.setOnAction(e -> handleButtonClick(6, 5, g6));
        g7.setOnAction(e -> handleButtonClick(6, 6, g7));
        g8.setOnAction(e -> handleButtonClick(6, 7, g8));

        h1.setOnAction(e -> handleButtonClick(7, 0, h1));
        h2.setOnAction(e -> handleButtonClick(7, 1, h2));
        h3.setOnAction(e -> handleButtonClick(7, 2, h3));
        h4.setOnAction(e -> handleButtonClick(7, 3, h4));
        h5.setOnAction(e -> handleButtonClick(7, 4, h5));
        h6.setOnAction(e -> handleButtonClick(7, 5, h6));
        h7.setOnAction(e -> handleButtonClick(7, 6, h7));
        h8.setOnAction(e -> handleButtonClick(7, 7, h8));
    }

    private boolean checkCheckersWin() {
        boolean playerHasValidMove = false;
        boolean opponentHasValidMove = false;

        Color currentPlayerColor = isPlayerBTurn ? Color.BLACK : Color.RED;  // Black goes first
        Color opponentColor = (currentPlayerColor == Color.BLACK) ? Color.RED : Color.BLACK;

        // Iterate through the board and check for valid moves or captures for both players
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button button = buttonBoard[row][col];
                Color pieceColor = getButtonColor(button);

                if (pieceColor == null) continue;  // Skip empty spaces

                // Check if the piece belongs to the current player
                if (pieceColor.equals(currentPlayerColor)) {
                    if (hasValidMove(row, col, currentPlayerColor)) {
                        playerHasValidMove = true;
                    }
                } else if (pieceColor.equals(opponentColor)) {  // Opponent's piece
                    if (hasValidMove(row, col, opponentColor)) {
                        opponentHasValidMove = true;
                    }
                }
            }
        }



        // If the opponent has no valid moves left, the current player wins
        if (!opponentHasValidMove) {
            return true;  // Current player wins
        }

        // If the current player has no valid moves left, they lose
        if (!playerHasValidMove) {
            return false;  // Current player loses
        }

        return Boolean.parseBoolean(null);  // Game is still ongoing
    }

    private Color getButtonColor(Button button) {
        if (button.getGraphic() != null) {
            ImageView piece = (ImageView) button.getGraphic();
            if (piece.getImage().equals(redPieceImage) || piece.getImage().equals(redKingPieceImage)) {
                return Color.RED;
            } else if (piece.getImage().equals(blackPieceImage) || piece.getImage().equals(blackKingPieceImage)) {
                return Color.BLACK;
            }
        }
        return null; // No piece
    }
    private boolean hasValidMove(int row, int col, Color playerColor) {
        // Iterate over the possible moves of the piece
        for (int i = -1; i <= 1; i += 2) {  // Checking both directions (forward and backward)
            for (int j = -1; j <= 1; j += 2) {  // Checking both diagonal directions
                int newRow = row + i;
                int newCol = col + j;

                // Check if the move is within the board bounds
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    Button targetButton = buttonBoard[newRow][newCol];

                    // Check if the target spot is empty
                    if (targetButton.getGraphic() == null) {
                        return true;  // Valid move found
                    }
                }
            }
        }

        // If no valid move is found
        return false;
    }



    private void handleButtonClick(int row, int col, Button clickedButton) {
        if (timeline != null) {
            timeline.stop();
        }
        // if no piece is selected it tries to select one
        if (selectedPiece == null) { // if there is no piece selected
            if (clickedButton.getGraphic() != null && isPlayerPiece(clickedButton)) { // if the button has a piece
                selectPiece(clickedButton);
            }
        }
        else if (canMultiCapture) {
            if (isValidJump(selectedPiece, clickedButton)) {
                Button newPosition = capturePiece(selectedPiece, clickedButton, capturedPiece);

                if (checkMultiCapture(newPosition)) {
                    canMultiCapture = true;
                    deselectPiece();
                    selectPiece(newPosition);
                }
                else {
                    canMultiCapture = false;
                    deselectPiece();
                    togglePlayerTurn(clickedButton);
                }
            }

        }
        else { // if a piece is already selected it tries to move it
            // if clicking on the same piece, deselect it
            if (clickedButton == selectedPiece) {
                deselectPiece();
            }

            //if clicking on empty square it tries to move the piece
            else if (isValidMove(selectedPiece, clickedButton)) {
                movePiece(selectedPiece, clickedButton);
                deselectPiece();
                togglePlayerTurn(clickedButton); // piece is moved, switch player turn
            }


            // jump moves that capture an opponent's piece
            else if (isValidJump(selectedPiece, clickedButton)) {
                Button newPosition = capturePiece(selectedPiece, clickedButton, capturedPiece);

                if (checkMultiCapture(newPosition)) {
                    canMultiCapture = true;
                    deselectPiece();
                    selectPiece(newPosition);
                }
                else {
                    canMultiCapture = false;
                    deselectPiece();
                    togglePlayerTurn(clickedButton);
                }
            }

            // if clicking on another piece it selects that one instead, but make sure it's the correct colour
            else if (clickedButton.getGraphic() != null && isPlayerPiece(clickedButton)){
                deselectPiece();
                selectPiece(clickedButton);
            }
        }

    }

    private void selectPiece(Button button) {
        selectedPiece = button;
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.GREENYELLOW);
        highlight.setRadius(40);
        button.setEffect(highlight);
    }

    private void deselectPiece() {
        if (selectedPiece != null) {
            selectedPiece.setEffect(null);
            selectedPiece = null;
        }
    }

    // checks if the spot selected has a player's piece on it
    private boolean isPlayerPiece(Button button) {
        ImageView boardSpot = (ImageView) button.getGraphic();

        if (boardSpot != null) {
            Image pieceColour = boardSpot.getImage();

            if (isPlayerBTurn) {
                return pieceColour.equals(blackPieceImage) || pieceColour.equals(blackKingPieceImage);
            }
            else {
                return pieceColour.equals(redPieceImage) || pieceColour.equals(redKingPieceImage);
            }

        }

        return false;
    }

    // checks if piece is a king
    private boolean isPieceKing(Button button) {
        ImageView boardSpot = (ImageView) button.getGraphic();

        if (boardSpot != null) {
            Image pieceType = boardSpot.getImage();

            return pieceType.equals(blackKingPieceImage) || pieceType.equals(redKingPieceImage);
        }

        return false;

    }

    private boolean isValidMove(Button fromSpot, Button toSpot) {
        if (toSpot.getGraphic() != null) {
            return false;
        }

        // move must be 1 diagonal
        int fromRow = getRow(fromSpot);
        int fromCol = getCol(fromSpot);
        int toRow = getRow(toSpot);
        int toCol = getCol(toSpot);

        // simple implementation of moving forward one
        if (Math.abs(toRow - fromRow) != 1 || Math.abs(toCol - fromCol) != 1) {
            return false; // move is not 1 diagonal
        }

        // direction of movement based on piece colour (players turn)
        if (isPlayerBTurn && !isPieceKing(fromSpot)) {
            // black moves up
            if (toRow >= fromRow) {
                return false;
            }
        }
        else if (!isPlayerBTurn && !isPieceKing(fromSpot)){
            // red moves down
            if (toRow <= fromRow) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidJump(Button fromSpot, Button toSpot) {
        if (toSpot.getGraphic() != null) {
            return false;
        }

        // move must be 2 diagonals
        int fromRow = getRow(fromSpot);
        int fromCol = getCol(fromSpot);
        int toRow = getRow(toSpot);
        int toCol = getCol(toSpot);

        // find row, col of captured piece
        int rowDiff = toRow - fromRow;
        int colDiff = toCol - fromCol;

        // simple implementation of moving forward 2 diagonals
        if (Math.abs(rowDiff) != 2 || Math.abs(colDiff) != 2) {
            return false;
        }
        else {
            int midRow = fromRow + (rowDiff / 2);
            int midCol = fromCol + (colDiff / 2);

            Button midSpot = buttonBoard[midCol][midRow];
            setCapturePiece(midSpot);

            if (midSpot.getGraphic() == null || isPlayerPiece(midSpot)) {
                return false;
            }
        }

        // direction of movement based on piece colour (players turn)
        if (isPlayerBTurn && !isPieceKing(fromSpot)) {
            // black moves up
            if (toRow >= fromRow) {
                return false;
            }
        }
        else if (!isPlayerBTurn && !isPieceKing(fromSpot)){
            // red moves down
            if (toRow <= fromRow) {
                return false;
            }
        }

        return true;
    }

    // fromSpot is the new position to check multi-capture from
    public boolean checkMultiCapture(Button fromSpot) {
        // call isValidJump from the new position in four different directions (2 diagonal: top right/left, bottom right/left)
        int row = getRow(fromSpot);
        int col = getCol(fromSpot);

        if ((row - 2 >= 0 && col - 2 >= 0) && isValidJump(fromSpot, buttonBoard[col-2][row-2])) { // top right
            return true;
        }
        else if ((row + 2 < 8 && col - 2 >= 0) && isValidJump(fromSpot, buttonBoard[col-2][row+2])) { // top left
            return true;
        }
        else if ((row - 2 >= 0 && col + 2 < 8) && isValidJump(fromSpot, buttonBoard[col+2][row-2])) { // bottom right
            return true;
        }
        else if ((row + 2 < 8 && col + 2 < 8) && isValidJump(fromSpot, buttonBoard[col+2][row+2])) { // bottom left
            return true;
        }
        return false;
    }

    private int getRow(Button spot) {

        for (int i = 0; i < buttonBoard.length; i++) {
            for (int j = 0; j < buttonBoard[i].length; j++) {
                if (buttonBoard[i][j] == spot) {
                    return j; // returns row index
                }
            }
        }

        return -1;
    }

    private int getCol(Button spot) {

        for (int i = 0; i < buttonBoard.length; i++) {
            for (int j = 0; j < buttonBoard[i].length; j++) {
                if (buttonBoard[i][j] == spot) {
                    return i; // returns col index
                }
            }
        }

        return -1;
    }

    private void movePiece(Button from, Button to) {
        // moves the piece from one button to another
        to.setGraphic(from.getGraphic());
        from.setGraphic(null);

        // check if piece can be promoted to king
        if (isPlayerBTurn) { // black piece needs to be at row 0
            if (getRow(to) == 0) {
                placePiece(to, blackKingPieceImage);
            }
        }
        else { // red piece needs to be at row 7
            if (getRow(to) == 7) {
                placePiece(to, redKingPieceImage);
            }
        }
    }

    private void setCapturePiece(Button spot) {
        capturedPiece = spot;
    }

    private Button getCapturePiece() {
        return capturedPiece;
    }

    // moves piece two diagonals and captures opponent piece and returns new position of piece that jumps
    private Button capturePiece(Button from, Button to, Button capturedPiece) {
        // remove captured piece from board
        getCapturePiece().setGraphic(null);

        // moves piece from one spot (button) to another
        to.setGraphic(from.getGraphic());
        from.setGraphic(null);

        // check if piece can be promoted to king
        if (isPlayerBTurn) { // black piece needs to be at row 0
            if (getRow(to) == 0) {
                placePiece(to, blackKingPieceImage);
            }
        }
        else { // red piece needs to be at row 7
            if (getRow(to) == 7) {
                placePiece(to, redKingPieceImage);
            }
        }

        return to;
    }

    private void placePiece(Button button, Image pieceImage) {
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(33);
        imageView.setFitHeight(33);
        button.setGraphic(imageView);
    }


    private void togglePlayerTurn(Button button) {
        boolean win = checkCheckersWin();

        if (win) {
            if (timeline != null) timeline.stop();
            if (countdownTimeline != null) countdownTimeline.stop();

            Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (isPlayerBTurn) {
                    openWinningPage(button);
                } else {
                    openLosingPage(button);
                }
            }));
            delay.setCycleCount(1);
            delay.play();
            return;
        }


        isPlayerBTurn = !isPlayerBTurn;

        // Cancel any existing timers
        if (timeline != null) timeline.stop();
        if (countdownTimeline != null) countdownTimeline.stop();

        // Update turn label
        turnLabel.setText(isPlayerBTurn ? "Your Turn" : "Opponent's Turn");

        if (!isPlayerBTurn && AIBot) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(this::makeAIMove);
            }).start();
            return;
        }
        startTurnTimer(button);
    }

    private void startTurnTimer(Button referenceButton) {
        final int[] timeLeft = {15};
        timerLabel.setText("Time: " + timeLeft[0]);

        timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> {
            openLosingPage(referenceButton);
        }));
        timeline.setCycleCount(1);
        timeline.play();

        countdownTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timeLeft[0]--;
            timerLabel.setText("Time: " + timeLeft[0]);
        }));
        countdownTimeline.setCycleCount(15);
        countdownTimeline.play();
    }

    private void makeAIMove() {
        // Go through every piece on the board, find valid moves
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Button piece = buttonBoard[row][col];
                if (piece.getGraphic() != null && isPlayerPiece(piece)) { // is AI's piece
                    for (int i = -1; i <= 1; i += 2) {
                        for (int j = -1; j <= 1; j += 2) {
                            int newRow = row + i;
                            int newCol = col + j;

                            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                                Button target = buttonBoard[newRow][newCol];
                                if (isValidMove(piece, target)) {
                                    movePiece(piece, target);
                                    togglePlayerTurn(target);
                                    return;
                                }
                            }

                            // Check for jumps
                            int jumpRow = row + i * 2;
                            int jumpCol = col + j * 2;
                            if (jumpRow >= 0 && jumpRow < 8 && jumpCol >= 0 && jumpCol < 8) {
                                Button jumpTarget = buttonBoard[jumpRow][jumpCol];
                                if (isValidJump(piece, jumpTarget)) {
                                    capturePiece(piece, jumpTarget, capturedPiece);
                                    togglePlayerTurn(jumpTarget);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private final String CHAT_LOG_PATH = "chatlog.txt";

    private void saveMessage(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_LOG_PATH, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadChatHistory() {
        try {
            return Files.readString(Paths.get(CHAT_LOG_PATH));
        } catch (IOException e) {
            return "";
        }
    }

    private StringBuilder chatHistory = new StringBuilder();

    @FXML
    private void openChat() {
        Stage chatStage = new Stage();
        chatStage.setTitle("In-Game Chat");

        VBox chatBox = new VBox(10);
        chatBox.setPadding(new javafx.geometry.Insets(10));
        chatBox.getStyleClass().add("chat-window");

        TextArea chatDisplay = new TextArea();
        chatDisplay.setEditable(false);
        chatDisplay.setWrapText(true);
        chatDisplay.setPrefHeight(200);
        chatDisplay.getStyleClass().add("chat-display");
        chatDisplay.setText(loadChatHistory());

        TextField messageField = new TextField();
        messageField.setPromptText("Type your message...");
        messageField.getStyleClass().add("chat-input");

        Button sendButton = new Button("Send");
        sendButton.getStyleClass().add("chat-send-button");

        sendButton.setOnAction(e -> {
            String msg = messageField.getText().trim();
            if (!msg.isEmpty()) {
                String formatted = "You: " + msg + "\n";
                chatDisplay.appendText(formatted);
                saveMessage(formatted);
                messageField.clear();
            }
        });
        messageField.setOnAction(e -> sendButton.fire());
        chatBox.getChildren().addAll(chatDisplay, messageField, sendButton);

        Scene scene = new Scene(chatBox, 350, 300);
        scene.getStylesheets().add(getClass().getResource("gameChat.css").toExternalForm());
        chatStage.setScene(scene);
        chatStage.show();
    }

    private void clearChatHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHAT_LOG_PATH))) {
            writer.write(""); // Clear the contents
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // how to play rules
    @FXML
    void howToPlayDescription(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.NONE); // No default icon

        alert.setTitle("Help");
        alert.setHeaderText("How to Play");

        // Set the content
        alert.setContentText(
                "1. Players take turns moving their pieces diagonally forward.\n\n"
                        + "2. Regular pieces can only move forward on black squares.\n\n"
                        + "3. Capture an opponent's piece by jumping over it diagonally.\n\n"
                        + "4. If a piece reaches the last row, it becomes a King.\n\n"
                        + "5. Kings can move and capture both forward and backward.\n\n"
                        + "6. If a player has no valid moves left, they lose the game. \n"
        );

        alert.getButtonTypes().add(javafx.scene.control.ButtonType.OK);
        // Apply CSS file
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("gameChat.css").toExternalForm());

        dialogPane.setPrefWidth(400); // Set a preferred width

        alert.showAndWait();
    }

    @FXML
    private void handleQuit() {
        Stage dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.setTitle("Confirm Quit");

        Label message = new Label("                      Are you sure?\nQuitting the game will result in a loss.");
        message.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            if (timeline != null) {
                timeline.stop();
            }
            if (countdownTimeline != null) {
                countdownTimeline.stop();
            }

            dialogStage.close();
            openToGameDashboard();
        });

        noButton.setOnAction(e -> dialogStage.close());

        HBox buttons = new HBox(10, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, message, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("quit-background");

        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(getClass().getResource("connectfourstyles.css").toExternalForm());

        dialogStage.setScene(scene);

        Stage currentStage = (Stage) board.getScene().getWindow();
        dialogStage.initOwner(currentStage);
        dialogStage.setX(currentStage.getX() + currentStage.getWidth() / 2 - 150);
        dialogStage.setY(currentStage.getY() + currentStage.getHeight() / 2 - 100);
        dialogStage.show();
    }

    private void openToGameDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game-dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load(), 900, 600);
            dashboardScene.getStylesheets().add(getClass().getResource("basic-styles.css").toExternalForm());

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Game Dashboard");
            dashboardStage.setScene(dashboardScene);

            // Close current window
            Stage currentStage = (Stage) board.getScene().getWindow();
            currentStage.close();

            dashboardStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openWinningPage(Button sourceButton) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("winningPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("checkerstyles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();

            Stage currentStage = (Stage) sourceButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openLosingPage(Button sourceButton) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("losingPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 450);
            scene.getStylesheets().add(getClass().getResource("checkerstyles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("OMG Platform");
            stage.show();

            Stage currentStage = (Stage) sourceButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

