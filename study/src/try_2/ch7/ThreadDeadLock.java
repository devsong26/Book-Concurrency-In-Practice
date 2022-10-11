package try_2.ch7;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 단일 스레드 Executor에서 데드락이 발생하는 작업 구조, 이런 코드는 금물!
 */
public class ThreadDeadLock {

    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            header = (Future<String>) exec.submit(new LoadFileTask("header.html"));
            footer = (Future<String>) exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // 데드락 발생
            return header.get() + page + footer.get();
        }
    }

    private String renderBody() {
        return null;
    }

}
