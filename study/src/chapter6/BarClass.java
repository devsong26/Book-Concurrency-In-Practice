package chapter6;

import chapter5.Account;
import chapter5.DollarAmount;
import chapter5.InsufficientFundsException;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class BarClass {

    /**
     * ReentrantLock 을 사용한 객체 동기화
     */
    public void foo(){
        Lock lock = new ReentrantLock();

        lock.lock();
        try{
            // 객체 내부 값을 사용
            // 예외가 발생한 경우, 적절하게 내부 값을 복원해야 할 수도 있음
        } finally {
            lock.unlock();
        }
    }

    /**
     * tryLock 메서드로 락 정렬 문제 해결
     */
    public boolean transferMoney(Account fromAcct,
                                 Account toAcct,
                                 DollarAmount amount,
                                 long timeout,
                                 TimeUnit unit)
            throws InsufficientFundsException, InterruptedException {
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);
        Random rnd = new Random();

        while(true){
            if(fromAcct.lock.tryLock()){
                try{
                    if(toAcct.lock.tryLock()){
                        try{
                            if(fromAcct.getBalance().compareTo(amount) < 0)
                                throw new InsufficientFundsException();
                            else{
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                return true;
                            }
                        } finally {
                            toAcct.lock.unlock();
                        }
                    }
                } finally {
                    fromAcct.lock.unlock();
                }
            }
            if(System.nanoTime() >= stopTime)
                return false;

            NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
        }
    }

    private long getRandomDelayModulusNanos(long timeout, TimeUnit unit) {
        return 0L;
    }

    private long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return 0L;
    }

    Lock lock = new ReentrantLock();

    /**
     * 일정 시간 이내에 락을 확보하는 모습
     */
    public boolean trySendOnSharedLine(String message,
                                       long timeout, TimeUnit unit)
                                       throws InterruptedException {
        long nanosToLock = unit.toNanos(timeout)
                           - estimatedNanosToSend(message);

        if(!lock.tryLock(nanosToLock, NANOSECONDS))
            return false;

        try{
            return sendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

    private long estimatedNanosToSend(String message) {
        return 0L;
    }

    /**
     * 인터럽트를 걸 수 있는 락 확보 방법
     */
    private boolean sendOnSharedLine(String message)
            throws InterruptedException {
        lock.lockInterruptibly();
        try{
            return cancellableSendOnSharedLine(message);
        } finally {
            lock.unlock();
        }
    }

    private boolean cancellableSendOnSharedLine(String message) {
        return true;
    }

}
