package try_1.chapter6;

import annotation.ThreadSafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 조건 큐를 사용해 구현한 BoundedBuffer
 */
@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V>{

    // 조건 서술어: not-full (!isFull())
    // 조건 서술어: not-empty (!isEmpty())

    public BoundedBuffer(int size){
        super(size);
    }

    //만족할 때까지 대기: not-full
    // BoundedBuffer.put 메서드에 조건부 알림 방법을 적용한 모습
    public synchronized void put(V v) throws InterruptedException {
        while(isFull())
            wait();

        boolean wasEmpty = isEmpty();
        doPut(v);
        if(wasEmpty)
            notifyAll();
    }

    // 만족할 때까지 대기: not-empty
    public synchronized V take() throws InterruptedException {
        while(isEmpty())
            wait();

        V v = doTake();
        notifyAll();
        return v;
    }

    /**
     * 상태 종속적인 메소드의 표준적인 형태
     */
    void stateDependentMethod() throws InterruptedException {
        Lock lock = new ReentrantLock();
        // 조건 서술어는 반드시 락으로 동기화된 이후에 확인해야 한다.
        synchronized (lock){
            while(!conditionPredicate())
                lock.wait();
            // 객체가 원하는 상태에 맞춰졌다.
        }
    }

    private boolean conditionPredicate() {
        return true;
    }
}
