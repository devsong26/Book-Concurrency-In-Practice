package try_2.ch3;

import annotation.GuardedBy;
import try_2.ch1.Widget;

/**
 * private 이면서 final인 변수를 사용해 동기화
 */
public class PrivateLock {
    private final Object myLock = new Object();

    @GuardedBy("myLock")
    Widget widget;

    void someMethod(){
        synchronized(myLock){
            // widget 변수의 값을 읽거나 변경
        }
    }

}
