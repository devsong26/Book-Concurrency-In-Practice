package try_1.chapter1;

import annotation.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 클라이언트측 락을 사용해 putIfAbsent 메서드를 구현
 */
@ThreadSafe
public class ListHelper<E> {

    public final List<E> list = Collections.synchronizedList(new ArrayList<>());

    public boolean putIfAbsent(E x){
        synchronized(list){
            boolean absent = !list.contains(x);

            if(absent) list.add(x);

            return absent;
        }
    }

}
