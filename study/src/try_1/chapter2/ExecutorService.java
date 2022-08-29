package try_1.chapter2;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * ExecutorService 인터페이스의 동작 주기 관리
 */
public interface ExecutorService<V> extends Executor {
    void shutdown();
    List<Runnable> shutdownNow();
    boolean isShutdown();
    boolean isTerminated();
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    Future<V> submit(Callable<V> task);

    List<Future<TravelQuote>> invokeAll(List<V> tasks, long time, TimeUnit unit);
    // .. 작업을 등록할 수 있는 몇가지 추가 메소드
}
