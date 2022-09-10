package try_2.ch5;

import org.omg.CORBA.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import static com.sun.activation.registries.LogSupport.log;

/**
 * 종료 기능을 추가한 웹서버
 */
public class LifecycleWebServer {
    private final ExecutorService exec = Executors.newFixedThreadPool(100);

    public void start() throws IOException {
        try(ServerSocket socket = new ServerSocket(80)){
            while(!exec.isShutdown()){
                try{
                    final Socket conn = socket.accept();
                    exec.execute(() -> handleRequest(conn));
                }catch(RejectedExecutionException e){
                    if(!exec.isShutdown())
                        log("task submission rejected", e);
                }
            }
        }
    }

    public void stop(){
        exec.shutdown();
    }

    private void handleRequest(Socket connection){
        Request req = readRequest(connection);
        if(isShutdownRequest(req)) stop();
        else dispatchRequest(req);
    }

    private Request readRequest(Socket connection) {
        return null;
    }

    private boolean isShutdownRequest(Request req) {
        return false;
    }

    private void dispatchRequest(Request req) {
    }

}
