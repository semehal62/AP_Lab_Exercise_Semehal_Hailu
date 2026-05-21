package chat_application;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;

public class ChatClient extends Application {
    private PrintWriter out;
    private String username;
    private ListView<String> messageList = new ListView<>();
    private TextField inputField = new TextField();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ask for username
        TextInputDialog dialog = new TextInputDialog("User");
        dialog.setTitle("Login");
        dialog.setHeaderText("Welcome to the Chat!");
        dialog.setContentText("Enter your username:");
        username = dialog.showAndWait().orElse("Anonymous");

        // UI Layout
        inputField.setPromptText("Type your message here...");
        VBox root = new VBox(10, new Label("Logged in as: " + username), messageList, inputField);
        
        // Connect to Server
        try {
            Socket socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);

            // Thread to listen for incoming messages
            new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        String finalLine = line;
                        Platform.runLater(() -> messageList.getItems().add(finalLine));
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> messageList.getItems().add("Disconnected from server."));
                }
            }).start();

        } catch (IOException e) {
            messageList.getItems().add("Could not connect to server.");
        }

        // Action: Send Message
        inputField.setOnAction(e -> {
            String msg = inputField.getText();
            if (!msg.isEmpty()) {
                out.println(username + ": " + msg);
                inputField.clear();
            }
        });

        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.setTitle("JavaFX Chat App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
