package try_2.ch2;

import annotation.GuardedBy;
import annotation.ThreadSafe;

/**
 * 동기화된 상태로 정수 값을 보관하는 클래스
 */
@ThreadSafe
public class SynchronizedInteger {

    @GuardedBy("this") private int value;

    public synchronized int get() {
        return value;
    }

    public synchronized void set(int value){
        this.value = value;
    }

}
