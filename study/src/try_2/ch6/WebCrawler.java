package try_2.ch6;

import annotation.GuardedBy;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TrackingExecutorService 를 사용해 중단된 작업을 나중에 사용할 수 있도록 보관하는 모습
 */
public abstract class WebCrawler {

    private volatile TrackingExecutor exec;

    @GuardedBy("this")
    private final Set<URL> urlsToCrawl = new HashSet<>();
    private final long TIMEOUT = 10L;
    private final TimeUnit UNIT = TimeUnit.HOURS;

    public synchronized void start() {
        exec = new TrackingExecutor(Executors.newCachedThreadPool());

        for (URL url : urlsToCrawl) submitCrawlTask(url);
        urlsToCrawl.clear();
    }

    public synchronized void stop() throws InterruptedException {
        try {
            saveUncrawled(exec.shutdownNow());
            if(exec.awaitTermination(TIMEOUT, UNIT))
                saveUncrawled(exec.getCancelledTasks());
        } finally {
            exec = null;
        }
    }

    private void saveUncrawled(List<Runnable> uncrawled) {
        for(Runnable task : uncrawled)
            urlsToCrawl.add(((CrawlTask) task).getPage());
    }

    protected abstract List<URL> processPage(URL url);


    private void submitCrawlTask(URL u) {
        exec.execute(new CrawlTask(u));
    }

    private class CrawlTask implements Runnable {
        private final URL url;

        private CrawlTask(URL url) {
            this.url = url;
        }

        public void run() {
            for (URL link : processPage(url)){
                if(Thread.currentThread().isInterrupted())
                    return;
                submitCrawlTask(link);
            }
        }
        public URL getPage() {return url;}
    }

}
