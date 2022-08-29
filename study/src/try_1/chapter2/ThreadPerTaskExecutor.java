package try_1.chapter2;

import java.util.concurrent.Executor;

/**
 * 작업마다 스레드를 새로 생성시키는 Executor
 */
public class ThreadPerTaskExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
