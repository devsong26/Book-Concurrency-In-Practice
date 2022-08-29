package try_1.chapter3;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * IndexingService의 프로듀서 스레드
 */
public class CrawlerThread extends Thread {
    private File root = new File("");
    private BlockingQueue<File> queue = new LinkedBlockingQueue<>();
    private static final File POISON = new File("");

    @Override
    public void run(){
        try{
            crawl(root);
        } catch(InterruptedException e) {// 통과
        } finally {
            while(true){
                try {
                    queue.put(POISON);
                    break;
                } catch (InterruptedException e1){
                    //재시도
                    continue;
                }
            }
        }
    }

    private void crawl(File root) throws InterruptedException {

    }

}
