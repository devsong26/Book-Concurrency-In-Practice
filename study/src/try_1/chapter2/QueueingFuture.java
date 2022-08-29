package try_1.chapter2;

import try_1.chapter1.BoundedHashSet;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * ExecutorCompletionService에서 사용하는 QueueingFuture 클래스
 */
public class QueueingFuture<V> extends FutureTask<V> {
    private BoundedHashSet<QueueingFuture<V>> completionQueue;

    public QueueingFuture(Callable<V> callable) {
        super(callable);
    }

    public QueueingFuture(Runnable runnable, V result) {
        super(runnable, result);
    }

    protected void done(){
        try {
            completionQueue.add(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
