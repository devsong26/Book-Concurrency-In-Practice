package try_1.chapter3;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * IndexingService의 컨슈머 스레드
 */
public class IndexerThread extends Thread{
    private File root = new File("");
    private BlockingQueue<File> queue = new LinkedBlockingQueue<>();
    private static final File POISON = new File("");

    @Override
    public void run() {
        try{
            while(true){
                File file = queue.take();

                if(file == POISON)
                    break;
                else
                    indexFile(file);
            }
        } catch (InterruptedException consumed) {}
    }

    private void indexFile(File file) {
    }

}
