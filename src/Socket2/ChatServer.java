package Socket2;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server dang chay " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client da ket noi: " + clientSocket);

                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(output);

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader input;
        private String username;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Yêu cầu client nhập username
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                output.println("Nhap username cua ban:");
                username = input.readLine();
                broadcast(username + " da tham gia chat");

                // Đọc và gửi tin nhắn cho tất cả client
                String message;
                while ((message = input.readLine()) != null) {
                    broadcast(username + ": " + message);
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (username != null) {
                    broadcast(username + " da roi chat");
                    clientWriters.remove(clientSocket);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    private synchronized static void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
            writer.flush();
        }
    }
}