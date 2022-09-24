package try_2.ch6;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

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

    private class IndexerThread {
        public void start() {

        }

        public void join() {
        }
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

}
