package try_2.ch4;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

/**
 * 프로듀서 - 컨슈머 패턴을 활용한 데스크탑 검색 애플리케이션의 구조
 */
public class FileCrawler implements Runnable{

    private final BlockingQueue<File> fileQueue;
    private final FileFilter fileFilter;
    private final File root;

    public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
        this.fileQueue = fileQueue;
        this.fileFilter = fileFilter;
        this.root = root;
    }

    public void run(){
        try{
            crawl(root);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private void crawl(File root) throws InterruptedException{
        File[] entries = root.listFiles(fileFilter);
        if(entries != null){
            for(File entry: entries){
                if(entry.isDirectory())
                    crawl(entry);
                else if(!alreadyIndexed(entry))
                    fileQueue.put(entry);
            }
        }

    }

    private boolean alreadyIndexed(File entry) {
        return false;
    }

}

