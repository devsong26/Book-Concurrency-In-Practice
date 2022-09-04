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

    public synchronized boolean putIfAbsent(E x){
        boolean absent = !list.contains(x);

        if (absent){
            list.add(x);
        }

        return absent;
    }
}
