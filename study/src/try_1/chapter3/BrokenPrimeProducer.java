package try_1.chapter3;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

public class BrokenPrimeProducer extends Thread {

    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    BrokenPrimeProducer(BlockingQueue<BigInteger> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try{
            BigInteger p = BigInteger.ONE;
            while (!cancelled)
                queue.put(p = p.nextProbablePrime());
        } catch (InterruptedException consumed) {}
    }


    public void cancel(){
        cancelled = true;
    }

    /**
     * 프로듀서가 대기 중인 상태로 계속 멈춰 있을 가능성이 있는 안전하지 않은 취소 방법의 예.
     * 이런 코드는 금물
     */
    void consumePrimes() throws InterruptedException {
        BlockingQueue<BigInteger> primes = null;
        BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);
        producer.start();
        try {
            while (needMorePrimes())
                consume(primes.take());
        } finally {
            producer.cancel();
        }
    }

    private void consume(BigInteger take) {
    }

    private boolean needMorePrimes() {
        return false;
    }
}
