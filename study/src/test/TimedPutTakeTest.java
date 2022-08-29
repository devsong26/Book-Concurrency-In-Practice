package test;

import try_1.chapter5.BarrierTimer;
import try_1.chapter5.BoundedBuffer;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;
import static test.BoundedBufferTest.xorShift;

public class TimedPutTakeTest {

    private static final ExecutorService pool
            = Executors.newCachedThreadPool();
    private final AtomicInteger putSum = new AtomicInteger(0);
    private final AtomicInteger takeSum = new AtomicInteger(0);
    private final CyclicBarrier barrier;
    private final BoundedBuffer<Integer> bb;
    private final int nTrials, nPairs;


    public TimedPutTakeTest(int capacity, int nPairs, int nTrials) {
        this.bb = new BoundedBuffer<>(capacity);
        this.nTrials = nTrials;
        this.nPairs = nPairs;
        this.barrier = new CyclicBarrier(nPairs * 2 + 1);
    }

    /**
     * 배리어 기반 타이머를 사용한 테스트
     */
    public void test2(){
        BarrierTimer timer = new BarrierTimer();

        try{
            timer.clear();
            for(int i=0; i<nPairs; i++){
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }

            barrier.await();
            barrier.await();
            long nsPerItem = timer.getTime() / (nPairs * (long)nTrials);
            System.out.println("Throughput: " + nsPerItem + " ns/item");
            assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * TimedPutTakeTest 실행 프로그램
     */
    public static void main2(String[] args) throws Exception {
        int tpt = 100_000;  // 스레드별 실행 횟수

        for(int cap = 1; cap <= 1_000; cap *= 10){
            System.out.println("Capacity: " + cap);
            for(int pairs = 1; pairs <= 128; pairs *= 2){
                TimedPutTakeTest t = new TimedPutTakeTest(cap, pairs, tpt);
                System.out.println("Pairs: " + pairs + "\t");
                t.test();
                System.out.println("\t");
                Thread.sleep(1_000);
                t.test();
                System.out.println();
                Thread.sleep(1_000);
            }
        }
        pool.shutdown();
    }

    private void test() {
    }

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

}
