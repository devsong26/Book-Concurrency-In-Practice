package chapter2;

import sun.misc.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static com.sun.activation.registries.LogSupport.log;

/**
 * 종료 기능을 추가한 웹서버
 */
public class LifecycleWebServer {
    private final ExecutorService exec = new ExecutorService() {
        @Override
        public void shutdown() {}

        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public Future submit(Callable task) {
            return null;
        }

        @Override
        public List<Future<TravelQuote>> invokeAll(List tasks, long time, TimeUnit unit) {
            return null;
        }

        @Override
        public void execute(Runnable command) {}
    };

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
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

    public void stop(){exec.shutdown();}

    private void handleRequest(Socket conn) {
        Request req = readRequest(conn);
        if(isShutdownRequest(req)){
            stop();
        }else{
            dispatchRequest(req);
        }
    }

    private void dispatchRequest(Request req) {
    }

    private boolean isShutdownRequest(Request req) {
        return false;
    }

    private Request readRequest(Socket conn) {
        return null;
    }

}
