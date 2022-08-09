package chapter1;

import annotation.GuardedBy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static chapter1.PreLoader.launderThrowable;

/**
 * CyclicBarrier를 사용해 셀룰러 오토마타의 연산을 제어
 */
public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board){
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count,
                mainBoard::commitNewValues);

        this.workers = new Worker[count];
        for(int i=0; i<count; i++){
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }
    }

    private class Worker implements Runnable {
        private final Board board;

        public Worker(Board board) {this.board = board;}

        public void run(){
            while(!board.hasConverged()){
                for(int x = 0; x < board.getMaxX(); x++){
                    for(int y = 0; y < board.getMaxY(); y++){
                        board.setNewValue(x, y, computeValue(x, y));
                    }
                }

                try{
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException ex){
                    return;
                }
            }
        }

        private Object computeValue(int x, int y) {
            return null;
        }
    }

    public void start(){
        for (Worker worker : workers) {
            new Thread(worker).start();
        }
        mainBoard.waitForConvergence();
    }

    /**
     * HashMap과 동기화 기능을 사용해 구현한 첫 번째 캐시
     */
    public interface Computable<A, V>{
        V compute(A arg) throws InterruptedException;
    }

    public class ExpensiveFunction implements Computable<String, BigInteger> {
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

    public class Memoizer2<A, V> implements Computable<A, V>{
        private final Map<A, V> cache = new ConcurrentHashMap<>();
        private final Computable<A, V> c;

        public Memoizer2(Computable<A, V> c) {
            this.c = c;
        }

        @Override
        public V compute(A arg) throws InterruptedException {
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
                = new ConcurrentHashMap<>();
        private final Computable<A, V> c;

        public Memoizer3(Computable<A, V> c) {
            this.c = c;
        }

        public V compute(final A arg) throws InterruptedException {
            Future<V> f = cache.get(arg);
            if(f == null){
                Callable<V> eval = () -> c.compute(arg);
                FutureTask<V> ft = new FutureTask<>(eval);
                f = ft;
                cache.put(arg, ft);
                ft.run();   // c.compute는 이 안에서 호출
            }

            try{
                return f.get();
            }catch(ExecutionException e){
                throw launderThrowable(e.getCause());
            }
        }
    }

    /**
     * Memoizer 최종 버전
     */
    public static class Memoizer<A, V> implements Computable<A, V> {
        private final ConcurrentMap<A, Future<V>> cache
                = new ConcurrentHashMap<>();
        private final Computable<A, V> c;

        public Memoizer(Computable<A, V> c){
            this.c = c;
        }

        public V compute(final A arg) throws InterruptedException{
            while(true){
                Future<V> f = cache.get(arg);
                if(f == null){
                    Callable<V> eval = () -> c.compute(arg);

                    FutureTask<V> ft = new FutureTask<>(eval);
                    f = cache.putIfAbsent(arg, ft);
                    if (f == null){
                        f = ft;
                        ft.run();
                    }

                    try{
                        return f.get();
                    }catch(CancellationException e){
                        cache.remove(arg, f);
                    }catch(ExecutionException e){
                        throw launderThrowable(e.getCause());
                    }
                }
            }
        }
    }

}
