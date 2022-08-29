package try_1.chapter2;

import java.util.concurrent.Executor;

/**
 * 작업을 등록한 스레드에서 직접 동작시키는 Executor
 */
public class WithinThreadExecutor implements Executor {
    @Override
    public void execute(Runnable r) {
        r.run();
    }
}
