package try_1.chapter6;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock을 사용해 구현한 카운팅 세마포어
 */
@ThreadSafe
public class SemaphoreOnLock {

    private final Lock lock = new ReentrantLock();
    // 조건 서술어: permitsAvailable (permits > 0)
    private final Condition permitsAvailable = lock.newCondition();

    @GuardedBy("lock")
    private int permits;

    SemaphoreOnLock (int initialPermits){
        lock.lock();
        try{
            permits = initialPermits;
        } finally {
            lock.unlock();
        }
    }

    //만족할 때까지 대기: permitsAvailable
    public void acquire() throws InterruptedException {
        lock.lock();
        try{
            while(permits < 0)
                permitsAvailable.await();
            --permits;
        } finally {
            lock.unlock();
        }
    }

    public void release(){
        lock.lock();
        try {
            ++permits;
            permitsAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * AQS에서 확보와 해제 연산이 동작하는 구조
     *
     * boolean acquire() throws InterruptedException{
     *     while(확보 연산을 처리할 수 없는 상태이다){
     *         if(확보 연산을 처리할 때까지 대기하길 원한다){
     *             현재 스레드가 큐에 들어 있지 않다면 스레드를 큐에 넣는다
     *             대기 상태에 들어간다
     *         }else{
     *             return 실패
     *         }
     *     }
     *     상황에 따라 동기화 상태 업데이트
     *     스레드가 큐에 들어 있었다면 큐에서 제거한다
     *     return 성공
     * }
     *
     * void release(){
     *     동기화 상태 업데이트
     *     if (업데이트된 상태에서 대기 중인 스레드를 풀어줄 수 있다)
     *          큐에 쌓여 있는 하나 이상의 스레드를 풀어준다
     * }
     */

}
