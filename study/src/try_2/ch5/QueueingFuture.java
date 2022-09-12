package try_2.ch5;

import try_1.chapter1.BoundedHashSet;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * ExecutorCompletionService에서 사용하는 QueueingFuture 클래스
 */
public class QueueingFuture<V> extends FutureTask<V> {
    private Queue<QueueingFuture<V>> completionQueue;

    QueueingFuture(Callable<V> c){super(c);}
    QueueingFuture(Runnable t, V r){
        super(t, r);
    }

    protected void done(){
        completionQueue.add(this);
    }
}
