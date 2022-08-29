package try_1.chapter6;

import annotation.ThreadSafe;

/**
 * 세련되지 못한 대기 방법을 사용하는 SleepBoundedBuffer
 */
@ThreadSafe
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    private static final long SLEEP_GRANULARITY = 1L;

    public SleepyBoundedBuffer(int size){
        super(size);
    }

    public void put(V v) throws InterruptedException {
        while(true){
            synchronized (this){
                if(!isFull()){
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

    public V take() throws InterruptedException {
        while(true){
            synchronized(this){
                if(!isEmpty()) return doTake();
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

}
