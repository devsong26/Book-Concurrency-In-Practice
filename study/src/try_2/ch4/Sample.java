package try_2.ch4;

import annotation.GuardedBy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * HashMap과 동기화 기능을 사용해 구현한 첫 번째 캐시
 */
public class Sample {
    public interface Computable<A, V>{
        V compute(A arg) throws InterruptedException;
    }

    public class ExpensiveFunction implements Computable<String, BigInteger>{
        public BigInteger compute(String arg){
            // 잠시 생각 좀 하고...
            return new BigInteger(arg);
        }
    }

    public class Memoizer1<A, V> implements Computable<A, V>{
        @GuardedBy("this")
        private final Map<A, V> cache = new HashMap<>();
        private final Computable<A, V> c;

        public Memoizer1(Computable<A, V> c){
            this.c = c;
        }

        public synchronized V compute(A arg) throws InterruptedException{
            V result = cache.get(arg);
            if (result == null){
                result = c.compute(arg);
                cache.put(arg, result);
            }
            return result;
        }
    }

    /**
     * HashMap 대신 ConcurrentHashMap을 적용
     */
    public class Memoizer2<A, V> implements Computable<A, V> {
        private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
        private final Computable<A, V> c;

        public Memoizer2(Computable<A, V> c){
            this.c = c;
        }

        public V compute(A arg) throws InterruptedException{
            V result = cache.get(arg);
            if(result == null){
                result = c.compute(arg);
                cache.put(arg, result);
            }
            return result;
        }
    }

    /**
     * FutureTask를 사용한 결과 캐시
     */
    public class Memoizer3<A, V> implements Computable<A, V>{
        private final Map<A, Future<V>> cache
                = new ConcurrentHashMap<A, Future<V>>();
        private final Computable<A, V> c;

        public Memoizer3(Computable<A, V> c){
            this.c = c;
        }

        public V compute(final A arg) throws InterruptedException {
            Future<V> f = cache.get(arg);
            if(f== null){
                Callable<V> eval = () -> c.compute(arg);
                FutureTask<V> ft = new FutureTask<V>(eval);
                f = ft;
                cache.put(arg, ft);
                ft.run(); // c.compute는 이 안에서 호출
            }
            try{
                return f.get();
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }

        private InterruptedException launderThrowable(Throwable cause) {
            return null;
        }
    }

}
