import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter outputToServer = new PrintWriter(socket.getOutputStream(), true)) {

            // Nhận và hiển thị tin nhắn chào mừng từ server
            String welcomeMessage = inputFromServer.readLine();
            System.out.println(welcomeMessage);

            // Nhập username từ client và gửi đến server
            BufferedReader inputFromUser = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Nhập username của bạn: ");
            String username = inputFromUser.readLine();
            outputToServer.println(username);

            // Tạo luồng riêng biệt để nhận tin nhắn từ server
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = inputFromServer.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            // Nhập và gửi tin nhắn từ client đến server
            String userMessage;
            while (true) {
                userMessage = inputFromUser.readLine();
                outputToServer.println(userMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}