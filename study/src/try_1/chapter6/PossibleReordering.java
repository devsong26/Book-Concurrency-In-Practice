package try_1.chapter6;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 제대로 동기화되지 않아 어이없는 결과를 출력하기도 하는 프로그램, 이런 코드는 금물!
 */
public class PossibleReordering {
    static int x = 0, y = 0;
    static int a = 0, b = 0;

    public static void main(String[] args)
            throws InterruptedException {
        Thread one = new Thread(() -> {
            a = 1;
            x = b;
        });
        Thread other = new Thread(() -> {
            b = 1;
            y = a;
        });

        one.start();
        other.start();

        one.join();
        other.join();

        System.out.println("(" + x + "," + y + ")");
    }

    /**
     * 동기화 피기백 방법을 사용하고 있는 FutureTask의 내부 클래스
     */
    private final class Sync<V> extends AbstractQueuedSynchronizer {
        private static final int RUNNING = 1, RAN = 2, CANCELLED = 4;
        private V result;
        private Exception exception;

        void innerSet(V v){
            while(true){
                int s = getState();
                if(ranOrCancelled(s))
                    return;
                if(compareAndSetState(s, RAN))
                    break;
            }
            result = v;
            releaseShared(0);
            done();
        }

        private void done() {
        }

        private boolean ranOrCancelled(int s) {
            return false;
        }

        V innerGet() throws InterruptedException, ExecutionException {
            acquireSharedInterruptibly(0);
            if(getState() == CANCELLED)
                throw new CancellationException();
            if(exception != null)
                throw new ExecutionException(exception);
            return result;
        }
    }
}
