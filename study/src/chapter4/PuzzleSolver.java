package chapter4;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 최종 결과가 없다는 사실을 확인하는 기능이 추가된 버전
 */
public class PuzzleSolver<P, M> extends ConcurrentPuzzleSolver<P, M> {
    private final AtomicInteger taskCount = new AtomicInteger(0);

    public PuzzleSolver(Puzzle<P, M> puzzle, ExecutorService exec, ConcurrentMap<P, Boolean> seen) {
        super(puzzle, exec, seen);
    }

    protected Runnable newTask(P p, M m, Node<P, M> n){
        return new CountingSolverTask(p, m, n);
    }


    private class CountingSolverTask extends ConcurrentPuzzleSolver<P, M>.SolverTask {
        public CountingSolverTask(P p, M m, Node<P, M> n) {
            super(p, m, n);
            taskCount.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0)
                    solution.setValue(null);
            }
        }
    }
}
