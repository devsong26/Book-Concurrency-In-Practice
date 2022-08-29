package try_1.chapter1;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 프로듀서-컨슈머 패턴을 활용한 데스크탑 검색 애플리케이션의 구조
 */
public class FileCrawler implements Runnable {
    private static final int BOUND = 1;
    private static final int N_CONSUMERS = 1;
    private final BlockingQueue<File> fileQueue;
    private final FileFilter fileFilter;
    private final File root;

    public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
        this.fileQueue = fileQueue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    @Override
    public void run() {
        try{
            crawl(root);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(File root) throws InterruptedException {
        File[] entries = root.listFiles(fileFilter);
        if(entries != null){
            for(File entry : entries){
                if(entry.isDirectory()){
                    crawl(entry);
                }else if(!alreadyIndexed(entry)){
                    fileQueue.put(entry);
                }
            }
        }
    }
    
    private boolean alreadyIndexed(File entry) {
        return false;
    }

    public static class Indexer implements Runnable {
        private final BlockingQueue<File> queue;
        
        public Indexer(BlockingQueue<File> queue){
            this.queue = queue;
        }
        
        @Override
        public void run() {
            try{
                while(true) {
                    indexFile(queue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void indexFile(File take) {
        }
    }
    
    // 데스크탑 검색 애플리케이션 동작시키기
    public static void startIndexing(File[] roots){
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(BOUND);
        FileFilter filter = new FileFilter(){
            public boolean accept(File file){return true;}
        };

        for (File root : roots){
            new Thread(new FileCrawler(queue, filter, root)).start();
        }

        for(int i=0; i<N_CONSUMERS; i++){
            new Thread(new Indexer(queue)).start();
        }
    }

}
