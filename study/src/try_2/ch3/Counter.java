package try_2.ch3;

import annotation.GuardedBy;
import annotation.ThreadSafe;

/**
 * 자바 모니터 패턴을 활용해 스레드 안전성을 확보한 카운터 클래스
 */
@ThreadSafe
public class Counter {
    @GuardedBy("this") private long value = 0;

    public synchronized long getValue(){
        return value;
    }

    public synchronized long increment(){
        if (value == Long.MAX_VALUE)
            throw new IllegalStateException("Counter overflow");

        return ++value;
    }

}
