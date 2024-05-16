import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        final int PORT = 12345;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server đang chạy và lắng nghe cổng " + PORT);

            while (true) {
                // Chấp nhận kết nối từ client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client đã kết nối.");

                // Mở luồng ra với client
                PrintWriter outputToClient = new PrintWriter(clientSocket.getOutputStream(), true);

                // Gửi các số từ 1 đến 1000 cho client
                for (int i = 1; i <= 1000; i++) {
                    outputToClient.println(i);
                    Thread.sleep(100);
                }

                // Đóng kết nối với client
                clientSocket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
