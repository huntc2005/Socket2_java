import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int PORT = 12345;

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String numberFromServer;

            // Đọc các số từ server cho đến khi kết thúc
            while ((numberFromServer = inputFromServer.readLine()) != null) {
                System.out.println("Số từ server: " + numberFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}