package try_2.ch6;

import annotation.GuardedBy;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap과 동기화 기능을 사용해 구현한 첫 번째 캐시
 */
public class Memoizer1<A,V> implements Computable<A, V>{

    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> c;

    public Memoizer1(Computable<A, V> c) {
        this.c = c;
    }

    public synchronized V compute(A arg) throws InterruptedException{
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }

        return result;
    }

}
