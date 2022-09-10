package try_2.ch5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 요청이 들어올 때마다 스레드를 생성하는 웹서버
 */
public class ThreadPerTaskWebServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket socket = new ServerSocket(80)) {
            while (true) {
                final Socket connection = socket.accept();
                Runnable task = () -> handleRequest(connection);
                new Thread(task).start();
            }
        }
    }

    private static void handleRequest(Socket connection) {
        System.out.println(connection.toString());
    }
}
