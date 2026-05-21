import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class ChatServer {
    private static final String DB_URL = "jdbc:sqlite:chat_app.db";
    protected static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Initializing Database...");
        setupDatabase();

        System.out.println("Server started on port 12345. Waiting for clients...");
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (true) {
                new Handler(serverSocket.accept()).start();
            }
        }
    }

    private static void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS messages (id INTEGER PRIMARY KEY AUTOINCREMENT, sender TEXT, content TEXT)");
        } catch (SQLException e) {
            System.err.println("Database setup error: " + e.getMessage());
        }
    }

    private static void saveMessage(String sender, String content) {
        String sql = "INSERT INTO messages(sender, content) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sender);
            pstmt.setString(2, content);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving message: " + e.getMessage());
        }
    }

    private static class Handler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public Handler(Socket socket) { this.socket = socket; }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Load History
                loadHistory(out);

                synchronized (clientWriters) { clientWriters.add(out); }

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Broadcasting: " + message);
                    
                    // Parse "Name: Content" to save to DB
                    if (message.contains(": ")) {
                        String[] parts = message.split(": ", 2);
                        saveMessage(parts[0], parts[1]);
                    }

                    synchronized (clientWriters) {
                        for (PrintWriter writer : clientWriters) {
                            writer.println(message);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected.");
            } finally {
                if (out != null) {
                    synchronized (clientWriters) { clientWriters.remove(out); }
                }
            }
        }

        private void loadHistory(PrintWriter writer) {
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT sender, content FROM messages ORDER BY id ASC LIMIT 50")) {
                while (rs.next()) {
                    writer.println("[History] " + rs.getString("sender") + ": " + rs.getString("content"));
                }
            } catch (SQLException e) {
                System.err.println("Error loading history: " + e.getMessage());
            }
        }
    }
}
