package chapter3;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 종료 기능이 구현되지 않은 프로듀서-컨슈머 패턴의 로그 서비스
 */
public class LogWriter {
    private static final int CAPACITY = 10;
    private final BlockingQueue<String> queue;
    private final LoggerThread logger;

    public LogWriter(Writer writer){
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    public void start() {
        logger.start();
    }

    private class LoggerThread extends Thread{
        private final PrintWriter writer;

        public LoggerThread(Writer writer) {
            this.writer = (PrintWriter) writer;
        }

        public void run(){
            try {
                while(true)
                    writer.println(queue.take());
            } catch (InterruptedException ignored) {
            } finally {
                writer.close();
            }
        }
    }

    /**
     * 로그 서비스에 종료 기능을 덧붙이지만 안정적이지 않은 방법
     */
    boolean shutdownRequested = true;

    public void log(String msg) throws InterruptedException {
        if(!shutdownRequested)
            queue.put(msg);
        else
            throw new IllegalStateException("logger is shutdown");
    }

}
