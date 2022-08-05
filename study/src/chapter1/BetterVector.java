package chapter1;

import annotation.ThreadSafe;

import java.util.Vector;

/**
 * 기존의 Vector 클래스를 상속받아 putIfAbsent 메서드를 추가
 */
@ThreadSafe
public class BetterVector<E> extends Vector<E> {
    public synchronized boolean putIfAbsent(E x){
        boolean absent = !contains(x);

        if(absent) add(x);

        return absent;
    }
}
