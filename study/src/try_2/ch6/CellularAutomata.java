package try_2.ch6;

import try_2.ch4.Board;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier를 사용해 셀룰러 오토마타의 연산을 제어
 */
public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board){
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, () -> mainBoard.commitNewValues());

        this.workers = new Worker[count];
        for(int i=0; i<count; i++)
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
    }

    private class Worker implements Runnable{
        private final Board board;

        public Worker(Board board) {
            this.board = board;
        }

        @Override
        public void run() {
            while(!board.hasConverged()){
                for(int x=0; x<board.getMaxX(); x++)
                    for(int y=0; y<board.getMaxY(); y++)
                        board.setNewValue(x,y,computeValue(x,y));

                try{
                    barrier.await();
                } catch (BrokenBarrierException|InterruptedException e) {
                    return;
                }
            }
        }

        private Object computeValue(int x, int y) {
            return null;
        }
    }

    public void start(){
        for(int i=0; i<workers.length; i++)
            new Thread(workers[i]).start();

        mainBoard.waitForConvergence();
    }
}
