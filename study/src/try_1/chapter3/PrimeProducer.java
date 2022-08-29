package try_1.chapter3;

import try_1.chapter1.Task;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static try_1.chapter1.PreLoader.launderThrowable;

/**
 * 인터럽트를 사용해 작업을 취소
 */
public class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run(){
        try {
            BigInteger p = BigInteger.ONE;
            while(!Thread.currentThread().isInterrupted())
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException e) {
            // 스레드를 종료한다.
            cancel();
        }
    }

    public void cancel() {
        this.interrupt();
    }


    /**
     * InterruptedException을 상위 메소드로 전달
     */
    BlockingQueue<Task> taskQueue;

    public Task getNextTask() throws InterruptedException {
        return taskQueue.take();
    }

    /**
     * 인터럽트 상태를 종료 직전에 복구시키는 중단 불가능 작업
     */
    public Task getNextTask(BlockingQueue<Task> queue){
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    return queue.take();
                } catch (InterruptedException e){
                    interrupted = true;
                    // 그냥 넘어가고 재시도
                }
            }
        } finally {
            if (interrupted)
                Thread.currentThread().interrupt();
        }
    }

    /**
     * 임의로 빌려 사용하는 스레드에 인터럽트 거는 방법. 이런 코드는 금물!
     */
    private static final ScheduledExecutorService cancelExec = new ScheduledExecutorService() {
        @Override
        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
            return null;
        }

        @Override
        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
            return null;
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
            return null;
        }

        @Override
        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
            return null;
        }

        @Override
        public void shutdown() {

        }

        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }

        @Override
        public boolean isShutdown() {
            return false;
        }

        @Override
        public boolean isTerminated() {
            return false;
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return null;
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return null;
        }

        @Override
        public Future<?> submit(Runnable task) {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return null;
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }

        @Override
        public void execute(Runnable command) {

        }
    };

    public static void timedRun(Runnable r,
                                long timeout,
                                TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();
        cancelExec.schedule(() -> taskThread.interrupt(), timeout, unit);
        r.run();
    }

    /**
     * 작업 실행 전용 스레드에 인터럽트 거는 방법
     */
    public static void timedRun2(final Runnable r,
                                long timeout, TimeUnit unit)
                                throws InterruptedException {
        class RethrowableTask implements Runnable {
            private volatile Throwable t;
            public void run(){
                try {r.run();}
                catch(Throwable t) {this.t = t;}
            }
            void rethrow() {
                if (t != null)
                    throw launderThrowable(t);
            }

            private RuntimeException launderThrowable(Throwable t) {
                return new RuntimeException(t);
            }
        }

        RethrowableTask task = new RethrowableTask();
        final Thread taskThread = new Thread(task);
        taskThread.start();
        cancelExec.schedule(() -> taskThread.interrupt(), timeout, unit);
        taskThread.join(unit.toMillis(timeout));
        task.rethrow();
    }

    /**
     * Future를 사용해 작업 중단하기
     */
    public static void timedRun3(Runnable r,
                                 long timeout, TimeUnit unit)
                                throws InterruptedException {
        AbstractExecutorService taskExec = null; // 코드 에러 안나게만
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        } catch (ExecutionException e) {
            // 작업 내부에서 예외 상황 발생. 예외를 다시 던진다.
            throw launderThrowable(e.getCause());
        } catch (TimeoutException e) {
            // finally 블록에서 작업이 중단될 것이다.
        } finally {
            // 이미 종료됐다 하더라도 별다른 악영향은 없다.
            task.cancel(true); // 실행중이라도 인터럽트를 건다.
        }
    }

}
