package try_2.ch4;

import java.io.File;
import java.util.concurrent.BlockingQueue;

/**
 * 프로듀서 - 컨슈머 패턴을 활용한 데스크탑 검색 애플리케이션의 구조
 */
public class Indexer implements Runnable{
    private final BlockingQueue<File> queue;

    public Indexer(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    public void run(){
        try{
            while(true) {
                indexFile(queue.take());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void indexFile(File take) {
    }

}
