package try_2.ch6;

import annotation.GuardedBy;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * LogWriter에 추가한 안정적인 종료 방법
 */
public class LogService {
    private static final long TIMEOUT = 1L;
    private static final TimeUnit UNIT = TimeUnit.NANOSECONDS;
    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardedBy("this") private boolean isShutdown;
    @GuardedBy("this") private int reservations;

    private final ExecutorService exec = newSingleThreadExecutor();

    public LogService(BlockingQueue<String> queue, LoggerThread loggerThread, PrintWriter writer) {
        this.queue = queue;
        this.loggerThread = loggerThread;
        this.writer = writer;
    }

    public void start(){ }

    public void stop() throws InterruptedException {
        try {
            exec.shutdown();
            exec.awaitTermination(TIMEOUT, UNIT);
        } finally {
            writer.close();
        }
    }

    public void log(String msg) throws InterruptedException {
        try {
            exec.execute(new WriteTask());
        } catch (RejectedExecutionException ignored) { }
    }

    private class LoggerThread extends Thread {
        public void run(){
            try {
                while (true) {
                    try {
                        synchronized (LogService.this) {
                            if(!isShutdown && reservations == 0)
                                break;
                        }

                        String msg = queue.take();
                        synchronized (LogService.this) {
                            --reservations;
                        }

                        writer.println(msg);
                    } catch (InterruptedException e) {
                        // 재시도
                    }
                }
            } finally {
                writer.close();
            }
        }

        public void start() {
        }

        public void interrupt() {

        }
    }
}
