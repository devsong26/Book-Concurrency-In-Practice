package try_2.ch3;

import annotation.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 목록에 없으면 추가하는 기능을 잘못 구현한 예. 이런 코드는 금물!
 */
@NotThreadSafe
public class ListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<>());

    /**
     * 클라이언트 측 락을 사용해 putIfAbsent 메서드를 구현
     */
    public synchronized boolean putIfAbsent(E x){
        synchronized(list){
            boolean absent = !list.contains(x);

            if (absent){
                list.add(x);
            }

            return absent;
        }
    }
}
