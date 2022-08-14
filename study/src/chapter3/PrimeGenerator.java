package chapter3;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * volatile 변수를 사용해 취소 상태를 확인
 */
@ThreadSafe
public class PrimeGenerator implements Runnable {

    private static final Thread SECONDS = new Thread();
    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<>();
    private volatile boolean cancelled;

    public void run(){
        BigInteger p = BigInteger.ONE;
        while(!cancelled){
            p = p.nextProbablePrime();
            synchronized (this){
                primes.add(p);
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get(){
        return new ArrayList<>(primes);
    }

    /**
     * 1초간 소수를 계산하는 프로그램
     */
    List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();

        try{
            SECONDS.sleep(1);
        } finally {
            generator.cancel();
        }

        return generator.get();
    }

}
