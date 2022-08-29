package test;

import try_1.chapter5.BoundedBuffer;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static test.BoundedBufferTest.xorShift;

/**
 * BoundedBuffer를 테스트하는 프로듀서-컨슈머 구조의 테스트 프로그램
 */
public class PutTakeTest {
    private static final ExecutorService pool
            = Executors.newCachedThreadPool();
    private final AtomicInteger putSum = new AtomicInteger(0);
    private final AtomicInteger takeSum = new AtomicInteger(0);
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTrials, nPairs;

    public static void main(String[] args){
        new PutTakeTest(10, 10, 100_000).test(); // 예제 인자 값
        pool.shutdown();
    }

    public PutTakeTest(int capacity, int nPairs, int nTrials) {
        this.bb = new BoundedBuffer<>(capacity);
        this.nTrials = nTrials;
        this.nPairs = nPairs;
        this.barrier = new CyclicBarrier(nPairs * 2 + 1);
    }

    void test(){
        try{
            for(int i=0; i< nPairs; i++){
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            
            barrier.await();    // 모든 스레드가 준비될 때까지 대기
            barrier.await();    // 모든 스레드의 작업이 끝날 때까지 대기
            assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PutTakeTest에서 사용한 프로듀서 클래스와 컨슈머 클래스
     */
    private class Producer implements Runnable {
        public void run(){
            try{
                int seed = (this.hashCode() ^ (int)System.nanoTime());
                int sum = 0;
                barrier.await();

                for(int i = nTrials; i > 0; --i){
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }

                putSum.getAndAdd(sum);
                barrier.await();
            } catch( Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    private class Consumer implements Runnable {
        public void run(){
            try{
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i){
                    sum += bb.take();
                }

                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 자원 유출 테스트
     */
    class Big{
        double[] data = new double[100_000];
    }

    void testLeak() throws InterruptedException {
        int CAPACITY = 1;
        BoundedBuffer<Big> bb = new BoundedBuffer<>(CAPACITY);
        int heapSize1 = 1; // 힙 스냅샷
        for( int i=0; i<CAPACITY; i++)
            bb.put(new Big());

        for(int i=0; i< CAPACITY; i++)
            bb.take();

        int heapSize2 = 2; // 힙 스냅샷
        int THRESHOLD = 1;
        assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
    }

}
