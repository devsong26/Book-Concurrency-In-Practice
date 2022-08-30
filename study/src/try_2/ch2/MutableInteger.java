package try_2.ch2;

import annotation.NotThreadSafe;

/**
 * 동기화되지 않은 상태로 정수 값을 보관하는 클래스
 */
@NotThreadSafe
public class MutableInteger {
    private int value;

    public int get(){return value;}
    public void set(int value){
        this.value = value;
    }
}
