package try_1.chapter4;

import javax.lang.model.element.Element;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * ThreadPoolExecutor를 상속받아 로그와 시간 측정 기능을 추가한 클래스
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    public TimingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected void beforeExecute(Thread t, Runnable r){
        super.beforeExecute(t, r);
        log.fine(String.format("Thread %s: start %s", t, r));
        startTime.set(System.nanoTime());
    }

    protected void afterExecute(Runnable r, Throwable t){
        try{
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            log.fine(String.format("Thread %s: end %s, time=%dns", t, r, taskTime));
        } finally {
            super.afterExecute(r, t);
        }
    }

    protected void terminated(){
        try {
            log.info (String.format("Terminated: avg time=%dns",
                    totalTime.get() / numTasks.get()));
        } finally {
            super.terminated();
        }
    }

    /**
     * 순차적인 실행 구조를 병렬화
     */
    void processSequentially(List<Element> elements){
        for(Element e : elements) process(e);
    }

    void processInParallel(Executor exec, List<Element> elements){
        for(final Element e : elements)
            exec.execute(() -> process(e));
    }

    private void process(Element e) {
    }

    /**
     * 순차적인 재귀 함수를 병렬화한 모습
     */
    public <T> void sequentialRecursive(List<ThNode<T>> nodes,
                                       Collection<T> results){
        for(ThNode<T> n : nodes){
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }

    public <T> void parallelRecursive(final Executor exec,
                                      List<ThNode<T>> nodes,
                                      final Collection<T> results){
        for(final ThNode<T> n : nodes){
            exec.execute(() -> results.add(n.compute()));
            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    /**
     * 병렬 연산 작업이 모두 끝나기를 기다리는 예제
     */
    public <T> Collection<T> getParallelResults(List<ThNode<T>> nodes)
            throws InterruptedException{
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }

}

class ThNode<T> {
    public T compute() {
        return null;
    }

    public List<ThNode<T>> getChildren() {
        return null;
    }
}