package chapter6;

import annotation.ThreadSafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock을 사용해 구현한 난수 발생기
 */
@ThreadSafe
public class ReentrantLockPseudoRandom extends PseudoRandom{
    private final Lock lock = new ReentrantLock(false);
    private int seed;

    ReentrantLockPseudoRandom(int seed){
        this.seed = seed;
    }

    public int nextInt(int n){
        lock.lock();
        try{
            int s = seed;
            seed = calculateNext(s);
            int remainder = s % n;
            return remainder > 0 ? remainder : remainder + n;
        } finally {
            lock.unlock();
        }
    }

}
