package org.seng.gui;

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


    private boolean isPlayerBTurn = true; // black goes first
    private Button selectedPiece = null;
    private Button capturedPiece = null;
    private Image redPieceImage;
    private Image blackPieceImage;
    private Image redKingPieceImage;
    private Image blackKingPieceImage;
    private Button[][] buttonBoard;


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

        isPlayerBTurn = false;
        togglePlayerTurn();
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

    private void handleButtonClick(int row, int col, Button clickedButton) {
        // if no piece is selected it tries to select one
        if (selectedPiece == null) { // if there is no piece selected
            if (clickedButton.getGraphic() != null && isPlayerPiece(clickedButton)) { // if the button has a piece
                selectPiece(clickedButton);
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
                togglePlayerTurn(); // piece is moved, switch player turn
                // todo: check win
            }
            // jump moves that capture an opponent's piece
            else if (isValidJump(selectedPiece, clickedButton)) {
                capturePiece(selectedPiece, clickedButton, capturedPiece);
                deselectPiece(); // THEY CANNOT DESELECT PIECE
                togglePlayerTurn(); // to be removed if we implement multi-jumps

                // cannot do while loop
                // boolean: two conditions - a piece has been captured AND there are still more pieces that can be captured
                //
                // if (piece has been captured AND there are still more pieces that can be captured : TRUE)
                //
                // else (there has been a piece captured but no more moves left)
                //      deselectPiece();
                //      togglePlayerTurn();

                // check again if player can make a capture in which case player gets another turn. if not then switch player's turn

                // todo: check win
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
//
//    //everything under here is experimental
//    //this function is checking if there is capturable pieces from a certain spot and gives a list
//
//    public List<int[]> getCapturablePieces(int fromRow, int fromCol, Piece piece) {
//        List<int[]> capturableLocations = new ArrayList<>();
//        if (piece == Piece.EMPTY) {
//            return capturableLocations;//if theres no pieces present it just returns an empty list
//        }
//        int[] directions; //initialization, nothing serious
//        if (piece == Piece.RED_KING || piece == Piece.BLACK_KING) {
//            directions = new int[]{-1, 1}; //since kings can move both up and down they should check for pieces both up and down the rows
//        } else if (piece == Piece.RED) {
//            directions = new int[]{-1}; //since red is at the top conventionally it can only move down visually (up in row count)
//        } else if (piece == Piece.BLACK) {
//            directions = new int[]{1}; // since black is at the bottom conventionally it can only move up visually (down in row count)
//        } else {
//            directions = new int[]{-1, 1}; //honestly useless, idk why i even put this here
//        }
//
//
//        for (int rowDir : directions) { //for each row in the directions the piece can go (what we just did earlier)
//            for (int colDir : new int[]{-1, 1}) { //in terms of columns pieces can move both left and right no problem
//                int targetRow = fromRow + 2 * rowDir; //first we need to check if theres nothing blocking pieces from being captured
//                int targetCol = fromCol + 2 * colDir; //technically you move left and up twice in order to capture a piece
//
//
//                if (inBounds(targetRow, targetCol) && board[targetRow][targetCol] == Piece.EMPTY) { //as long as its in the bounds of the board, ando nothing is blocking it, the if function activates
//                    int middleRow = fromRow + rowDir;
//                    int middleCol = fromCol + colDir;//finding the middle piece (depending on the piece calculation is different)
//                    Piece middlePiece = boardd[middleRow][middleCol]; //checks what piece it is, we have to make sure that it is not one of the player's own chips
//
//                    //this if statement checking if the middle piece is dfferent
//                    if ((piece == Piece.RED || piece == Piece.RED_KING) && (middlePiece == Piece.BLACK || middlePiece == Piece.BLACK_KING) ||
//                            (piece == Piece.BLACK || piece == Piece.BLACK_KING) && (middlePiece == Piece.RED || middlePiece == Piece.RED_KING)) {
//
//                        capturableLocations.add(new int[]{targetRow, targetCol}); //this returns the locations where it can go basically, into the list (can be upto 4 if its a king!)
//                    }
//                }
//            }
//        }
//        return capturableLocations;
//    }


private int getRow(Button spot) {
        Button[][] buttonBoard = {{a1, a2, a3, a4, a5, a6, a7, a8},
        {b1, b2, b3, b4, b5, b6, b7, b8},
        {c1, c2, c3, c4, c5, c6, c7, c8},
        {d1, d2, d3, d4, d5, d6, d7, d8},
        {e1, e2, e3, e4, e5, e6, e7, e8},
        {f1, f2, f3, f4, f5, f6, f7, f8},
        {g1, g2, g3, g4, g5, g6, g7, g8},
        {h1, h2, h3, h4, h5, h6, h7, h8}};

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
        Button[][] buttonBoard = {{a1, a2, a3, a4, a5, a6, a7, a8},
                {b1, b2, b3, b4, b5, b6, b7, b8},
                {c1, c2, c3, c4, c5, c6, c7, c8},
                {d1, d2, d3, d4, d5, d6, d7, d8},
                {e1, e2, e3, e4, e5, e6, e7, e8},
                {f1, f2, f3, f4, f5, f6, f7, f8},
                {g1, g2, g3, g4, g5, g6, g7, g8},
                {h1, h2, h3, h4, h5, h6, h7, h8}};

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

    // moves piece two diagonals and captures opponent piece
    private void capturePiece(Button from, Button to, Button capturedPiece) {
        // remove captured piece from board
        capturedPiece.setGraphic(null);

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
    }

    private void placePiece(Button button, Image pieceImage) {
        ImageView imageView = new ImageView(pieceImage);
        imageView.setFitWidth(33);
        imageView.setFitHeight(33);
        button.setGraphic(imageView);
    }

    private void togglePlayerTurn() {
        isPlayerBTurn = !isPlayerBTurn;
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
            dialogStage.close();
            openToGameDashboard();
        });
        noButton.setOnAction(e -> dialogStage.close());

        HBox buttons = new HBox(10, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, message, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("quit-background"); // ⭐ Add style class

        Scene scene = new Scene(layout, 300, 150);
        scene.getStylesheets().add(getClass().getResource("connectfourstyles.css").toExternalForm()); // ⭐ Load your CSS

        dialogStage.setScene(scene);

        Stage currentStage = (Stage) board.getScene().getWindow(); // 'board' is your main pane
        dialogStage.initOwner(currentStage);

        dialogStage.setX(currentStage.getX() + currentStage.getWidth() / 2 - 150); // 150 = half of popup width
        dialogStage.setY(currentStage.getY() + currentStage.getHeight() / 2 - 100);  // 75 = half of popup height

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

}

