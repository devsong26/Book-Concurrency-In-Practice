package try_2.ch0;

import annotation.GuardedBy;
import annotation.ThreadSafe;

/**
 * 스레드 안전한 일련번호 생성 프로그램
 */
@ThreadSafe
public class Sequence {
    @GuardedBy("this") private int value;

    public synchronized int getNext(){
        return value++;
    }
}
