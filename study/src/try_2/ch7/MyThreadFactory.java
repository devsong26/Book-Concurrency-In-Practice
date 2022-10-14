package try_2.ch7;

import try_1.chapter4.MyAppThread;

/**
 * 직접 작성한 스레드 팩토리
 */
public class MyThreadFactory implements ThreadFactory {

    private final String poolName;

    public MyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new MyAppThread(r, poolName);
    }

}
