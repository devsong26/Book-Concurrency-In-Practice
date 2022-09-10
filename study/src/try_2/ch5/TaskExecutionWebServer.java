package try_2.ch5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 스레드 풀을 사용한 웹서버
 */
public class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec
            = Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws IOException {
        try(ServerSocket socket = new ServerSocket(80)){
            while(true){
                final Socket connection = socket.accept();
                exec.execute(() -> handleRequest(connection));
            }
        }
    }

    private static void handleRequest(Socket connection) {
        System.out.println(connection.toString());
    }

}
