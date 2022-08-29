package try_1.chapter3;

import annotation.GuardedBy;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TrackingExecutorService를 사용해 중단된 작업을 나중에 사용할 수 있도록 보관하는 모습
 */
public abstract class WebCrawler {
    private static final long TIMEOUT = 1L;
    private static final TimeUnit UNIT = TimeUnit.MINUTES;
    private volatile TrackingExecutor exec;
    @GuardedBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<>();

    public synchronized void start(){
        exec = new TrackingExecutor(Executors.newCachedThreadPool());

        for(URL url : urlsToCrawl)
            submitCrawlTask(url);

        urlsToCrawl.clear();
    }

    public synchronized void stop() throws InterruptedException{
        try {
            saveUncrawled(exec.shutdownNow());
            if(exec.awaitTermination(TIMEOUT, UNIT))
                saveUncrawled(exec.getCancelledTasks());
        } finally {
            exec = null;
        }
    }

    protected abstract List<URL> processPage(URL url);

    private void saveUncrawled(List<Runnable> uncrawled){
        for (Runnable task : uncrawled)
            urlsToCrawl.add(((CrawlTask) task).getPage());
    }

    protected void submitCrawlTask(URL url){
        exec.execute(new CrawlTask(url));
    }

    private class CrawlTask implements Runnable{
        private final URL url;

        public CrawlTask(URL url) {
            this.url = url;
        }

        public URL getPage() {
            return url;
        }

        @Override
        public void run() {
            for(URL link: processPage(url)) {
                if (Thread.currentThread().isInterrupted())
                    return;
                submitCrawlTask(link);
            }
        }

        /**
         * 스레드 풀에서 사용하는 작업용 스레드의 일반적인 모습
         */
        public void run2(){
            Throwable thrown = null;
            try {
                while(!isInterrupted())
                    runTask(getTaskFromWorkQueue());
            } catch(Throwable e){
                thrown = e;
            } finally {
                threadExited(this, thrown);
            }
        }

        protected void threadExited(CrawlTask crawlTask, Throwable thrown){}

        protected void runTask(Object taskFromWorkQueue){}

        protected Object getTaskFromWorkQueue(){return null;}

        protected boolean isInterrupted(){return false;}

    }

}
