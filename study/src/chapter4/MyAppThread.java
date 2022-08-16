package chapter4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 직접 작성한 스레드 클래스
 */
public class MyAppThread extends Thread {
    public static final String DEFAULT_NAME = "MyAppThread";
    private static volatile boolean debugLifecycle = false;
    private static final AtomicInteger created = new AtomicInteger();
    private static final AtomicInteger alive = new AtomicInteger();
    private static final Logger log = Logger.getAnonymousLogger();

    public MyAppThread(Runnable r){this(r, DEFAULT_NAME);}

    public MyAppThread(Runnable r, String name) {
        super(r, name + "-" + created.incrementAndGet());
        setUncaughtExceptionHandler(
                (t, e) -> log.log(Level.SEVERE, "UNCAUGHT in thread " + t.getName(), e));
    }

    @Override
    public void run(){
        //debug 플래그를 복사해 계속해서 동일한 값을 갖도록 한다.
        boolean debug = debugLifecycle;
        if(debug)
            log.log(Level.FINE, "Created " + getName());

        try{
            alive.incrementAndGet();
            super.run();
        } finally {
            alive.decrementAndGet();
            if(debug)
                log.log(Level.FINE, "Existing " + getName());
        }
    }
    
    public static int getThreadsAlive(){return alive.get();}
    public static boolean getDebug() {return debugLifecycle;}
    public static void setDebug(boolean b) { debugLifecycle = b;}

    /**
     * 어디 위치해야 할지 모르는 로직,
     * 기본 팩토리 메서드로 만들어진 Executor의 설정 변경 모습
     */
    private void foo(){
        ExecutorService exec = Executors.newCachedThreadPool();
        if(exec instanceof ThreadPoolExecutor)
            ((ThreadPoolExecutor) exec).setCorePoolSize(10);
        else
            throw new AssertionError("Oops, bad assumption");
    }

}
