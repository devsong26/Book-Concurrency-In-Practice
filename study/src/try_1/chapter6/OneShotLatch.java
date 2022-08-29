package try_1.chapter6;

import annotation.ThreadSafe;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * AbstractQueuedSynchronizer 를 활용한 바이너리 래치 클래스
 */
@ThreadSafe
public class OneShotLatch {
    private final Sync sync = new Sync();

    public void signal() {
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {
        /**
         * Semaphore 클래스의 tryAcquireShared 메서드와 tryReleaseShared 메서드
         */
        protected int tryAcquireShared(int acquires){
            while(true){
                int available = getState();
                int remaining = available - acquires;
                if(remaining < 0
                        || compareAndSetState(available, remaining))
                    return remaining;
            }
        }

        protected boolean tryReleaseShared(int release){
            while(true){
                int p = getState();
                if (compareAndSetState(p, p + release))
                    return true;
            }
        }

        /**
         * 공정하지 않은 ReentrantLock 클래스의 tryAcquire 메서드 내부
         */
        protected boolean tryAcquire(int ignored){
            final Thread current = Thread.currentThread();
            int c = getState();
            Thread owner = null;
            if (c == 0){
                if(compareAndSetState(0, 1)){
                    owner = current;
                    return true;
                }
            }else if(current == owner){
                setState(c + 1);
                return true;
            }
            return false;
        }
    }

}
