package chapter3;

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

    private final BlockingQueue<String> queue;
    private final LoggerThread loggerThread;
    private final PrintWriter writer;
    @GuardedBy("this") private boolean isShutdown;
    @GuardedBy("this") private int reservations;

    public LogService(BlockingQueue<String> queue, LoggerThread loggerThread, PrintWriter writer) {
        this.queue = queue;
        this.loggerThread = loggerThread;
        this.writer = writer;
    }

    public void start(){
        loggerThread.start();
    }

    public void stop(){
        synchronized (this) { // 클라이언트 락
            isShutdown = true;
        }

        loggerThread.interrupt();
    }

    public void log(String msg) throws InterruptedException {
        synchronized(this){
            if(isShutdown)
                throw new IllegalStateException();
            ++reservations;
        }
        queue.put(msg);
    }

    private class LoggerThread extends Thread {
        @Override
        public void run(){
            try{
                while(true){
                    try {
                        synchronized (LogService.this){
                            if(isShutdown && reservations == 0)
                                break;
                        }
                        String msg = queue.take();
                        synchronized (LogService.this) {
                            --reservations;
                        }
                        writer.println(msg);
                    } catch (InterruptedException e) {
                        // 재시도
                        continue;
                    }
                }
            } finally {
                writer.close();
            }
        }
    }

    /**
     * ExecutorService를 활용한 로그 서비스
     */
    private static final long TIMEOUT = 1L;
    private static final TimeUnit UNIT = TimeUnit.MINUTES;
    private final ExecutorService exec = newSingleThreadExecutor();

    public void stop2() throws InterruptedException {
        try {
            exec.shutdown();
            exec.awaitTermination(TIMEOUT, UNIT);
        } finally {
            writer.close();
        }
    }

    public void log2(String msg){
        try{
            exec.execute(new WriteTask(msg));
        } catch(RejectedExecutionException ignored){}
    }
}
