package try_1.chapter5;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * 작업 큐에 대한 순차적인 접근
 */
public class WorkerThread extends Thread {
    private final BlockingQueue<Runnable> queue;

    public WorkerThread(BlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(true){
            try {
                Runnable task = queue.take();
                task.run();
            } catch (InterruptedException e) {
                break; // 스레드를 종료시킨다.
            }
        }
    }

    /**
     * 아무런 의미가 없는 동기화 구문, 이런 코드는 금물!
     */
    public void foo(){
        synchronized (new Object()){
            // 작업 진행
        }
    }

    /**
     * 락 제거 대상
     */
    public String getStoogeNames() {
        List<String> stooges = new Vector<>();
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
        return stooges.toString();
    }
}
