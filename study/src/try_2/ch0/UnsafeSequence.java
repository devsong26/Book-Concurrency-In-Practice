package try_2.ch0;

import annotation.NotThreadSafe;

/**
 * 스레드 안전하지 않은 일련번호 생성 프로그램
 */
@NotThreadSafe
public class UnsafeSequence {
    private int value;

    // 유일한 값을 리턴
    public int getNext(){
        return value++;
    }
}
