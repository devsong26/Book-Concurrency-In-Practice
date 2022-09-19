package try_2.ch6;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

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

}
