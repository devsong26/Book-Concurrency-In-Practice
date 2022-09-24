package try_2.ch6;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 독약 객체를 사용해 서비스를 종료
 */
public class IndexingService {

    private static final File POISON = new File("");
    private final IndexerThread consumer = new IndexerThread();
    private final CrawlerThread producer = new CrawlerThread();
    private final BlockingQueue<File> queue;
    private final FileFilter fileFilter;
    private final File root;

    public IndexingService(BlockingQueue<File> queue, FileFilter fileFilter, File root) {
        this.queue = queue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    /**
     * IndexingService의 프로듀서 스레드
     */
    private class CrawlerThread {
        public void run() {
            try {
                crawl(root);
            } catch(InterruptedException e) {
                // 통과
            } finally {
                while (true) {
                    try {
                        queue.put(POISON);
                        break;
                    } catch (InterruptedException e1) {
                        // 재시도1
                    }
                }

            }
        }

        private void crawl(File root) throws InterruptedException {

        }

        public void start() {
        }

        public void interrupt() {
        }
    }

    /**
     * IndexingService의 컨슈머 스레드
     */
    private class IndexerThread {
        public void run() {
            try {
                while (true) {
                    File file = queue.take();
                    if (file == POISON)
                        break;
                    else
                        indexFile(file);
                }
            } catch (InterruptedException consumed) {}
        }

        public void start() {

        }

        public void join() {
        }
    }

    private void indexFile(File file) {

    }

    public void start() {
        producer.start();
        consumer.start();
    }

    public void stop(){
        producer.interrupt();
    }

    public void awaitTermination() throws InterruptedException {
        consumer.join();
    }

    /**
     * 메서드 내부에서 Executor를 사용하는 모습
     */
    boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit)
        throws InterruptedException {

        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);

        try {
            for (final String host : hosts)
                exec.execute(() -> {
                    if(checkMail(host))
                        hasNewMail.set(true);
                });
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }

        return hasNewMail.get();
    }

    private boolean checkMail(String host) {
        System.out.println(host);
        return true;
    }

}
