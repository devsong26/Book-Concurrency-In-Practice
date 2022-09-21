package try_2.ch6;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 인터럽트를 사용해 작업을 취소
 */
public class PrimeProducer extends Thread{

    private final BlockingQueue<BigInteger> queue;

    public PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    public void run(){
        try{
            BigInteger p = BigInteger.ONE;
            while(!Thread.currentThread().isInterrupted())
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException e) {
            cancel(); // 스레드 종료
        }
    }

    public void cancel(){
        interrupt();
    }

    /**
     * InterruptedException을 상위 메서드로 전달
     */
    BlockingQueue<Task> taskQueue;

    public Task getNextTask() throws InterruptedException {
        return taskQueue.take();
    }

    /**
     * 인터럽트 상태를 종료 직전에 복구시키는 중단 불가능 작업
     */
    public Task getNextTask(BlockingQueue<Task> queue){
        boolean isInterrupted = false;

        try {
            while(true){
                try{
                    return taskQueue.take();
                } catch(InterruptedException e){
                    isInterrupted = true;
                    // 그냥 넘어가고 재시도
                }
            }
        } finally {
            if (!isInterrupted)
                Thread.currentThread().interrupt();
        }
    }

    /**
     * 임시로 빌려 사용하는 스레드에 인터럽트 거는 방법. 이런 코드는 금물!
     */
    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r,
                                long timeout, TimeUnit unit){
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(() -> taskThread.interrupt(), timeout, unit);
        r.run();
    }


}
