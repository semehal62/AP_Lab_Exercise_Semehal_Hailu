package Poker_Game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class pokergui extends Application {
    private String[] deck = {"King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2", "Ace"};
    private ArrayList<String> tableCards = new ArrayList<>();
    private Player p1 = new Player();
    private Player p2 = new Player();
    private int turn = 0; // 0 for P1, 1 for P2

    // GUI Elements
    private Label tableLabel = new Label();
    private Label statusLabel = new Label("Player 1's Turn");
    private Label scoreLabel = new Label("P1: 0 | P2: 0");
    private VBox handContainer = new VBox(10);
    private TextField cardInput = new TextField();

    @Override
    public void start(Stage primaryStage) {
        setupGame();

        // Layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: rgb(2, 101, 6);"); 

        tableLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-font-weight: bold;");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: yellow;");
        scoreLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        cardInput.setPromptText("Type card name here...");
        cardInput.setMaxWidth(200);

        Button btnMatch = new Button("Take Match");
        Button btnDrop = new Button("Drop Card");
        
        HBox controls = new HBox(10, btnMatch, btnDrop);
        controls.setAlignment(Pos.CENTER);

        root.getChildren().addAll(statusLabel, scoreLabel, new Label("TABLE:"), tableLabel, new Separator(), handContainer, cardInput, controls);

        // Logic for Match
        btnMatch.setOnAction(e -> handleMove(1));
        
        // Logic for Drop
        btnDrop.setOnAction(e -> handleMove(2));

        updateUI();

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Simple Poker Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupGame() {
        Set<Integer> given = new TreeSet<>();
        Random rand = new Random();
        
        // Setup Table
        while (tableCards.size() < 7) {
            int idx = rand.nextInt(13);
            if (given.add(idx)) {
                tableCards.add(deck[idx]);
            }
        }
        // Setup Players
        p1.take_cards(deck);
        p2.take_cards(deck);
    }

    private void handleMove(int moveType) {
        Player current = (turn == 0) ? p1 : p2;
        String input = cardInput.getText().trim();

        if (moveType == 1) { // Match
            if (current.cards.contains(input) && tableCards.contains(input)) {
                current.score++;
                current.cards.remove(input);
                tableCards.remove(input);
                nextTurn();
            } else {
                statusLabel.setText("Try Again! Invalid Match.");
            }
        } else { // Drop
            if (current.cards.contains(input)) {
                current.cards.remove(input);
                tableCards.add(input);
                nextTurn();
            } else {
                statusLabel.setText("Try Again! You don't have that card.");
            }
        }
        cardInput.clear();
        updateUI();
        checkWinner();
    }

    private void nextTurn() {
        turn = 1 - turn;
        statusLabel.setText("Player " + (turn + 1) + "'s Turn");
    }

    private void updateUI() {
        tableLabel.setText(tableCards.toString());
        scoreLabel.setText("P1 Score: " + p1.score + " | P2 Score: " + p2.score);
        
        handContainer.getChildren().clear();
        Player current = (turn == 0) ? p1 : p2;
        Label handLabel = new Label("Your Hand: " + current.cards.toString());
        handLabel.setStyle("-fx-text-fill: white;");
        handContainer.getChildren().add(handLabel);
    }

    private void checkWinner() {
        if (p1.cards.isEmpty() && p2.cards.isEmpty()) {
            String winner = (p1.score > p2.score) ? "Player 1 Wins!" : (p2.score > p1.score) ? "Player 2 Wins!" : "It's a Draw!";
            statusLabel.setText("GAME OVER: " + winner);
            cardInput.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
