package test;

import try_1.chapter5.Account;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;

/**
 * ThreadPoolExecutor를 테스트하기 위한 TestingThreadFactory
 */
public class TestingThreadFactory implements ThreadFactory {
    public final AtomicInteger numCreated = new AtomicInteger();
    private final ThreadFactory factory
            = Executors.defaultThreadFactory();

    @Override
    public Thread newThread(Runnable r) {
        numCreated.incrementAndGet();
        return factory.newThread(r);
    }

    /**
     * 스레드 풀의 스레드 개수가 제대로 늘어나느지를 확인할 수 있는 테스트 케이스
     */
    public void testPoolExpansion() throws InterruptedException {
        int MAX_SIZE = 10;
        TestingThreadFactory threadFactory = new TestingThreadFactory();
        ExecutorService exec
                = Executors.newFixedThreadPool(MAX_SIZE, threadFactory);

        for(int i=0; i<10 * MAX_SIZE; i++)
            exec.execute(() -> {
                try{
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            });

        for(int i=0;
            i< 20 && threadFactory.numCreated.get() < MAX_SIZE;
            i++)
            Thread.sleep(100);

        assertEquals(threadFactory.numCreated.get(), MAX_SIZE);
        exec.shutdown();
    }

    /**
     * Thread.yield 메서드를 사용해 교차 실행 가능성을 높이는 방법
     */
    public synchronized void transferCredits (Account from,
                                              Account to,
                                              int amount){
        from.setBalance(from.getBalance2() - amount);
        int THRESHOLD = 1;
        Random random = new Random();
        if(random.nextInt(1_000) > THRESHOLD)
            Thread.yield();
        to.setBalance(to.getBalance2() + amount);
    }

}
