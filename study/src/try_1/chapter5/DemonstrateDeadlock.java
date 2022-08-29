package try_1.chapter5;

import java.util.Random;

/**
 * 일반적으로 데드락에 빠지는 반복문
 */
public class DemonstrateDeadlock {

    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;
    private static final int NUM_ITERATIONS = 1_000_000;

    public static void main(String[] args){
        final Random rnd = new Random();
        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for(int i=0; i<accounts.length; i++)
            accounts[i] = new Account();

        class TransferThread extends Thread {
            @Override
            public void run(){
                for(int i=0; i<NUM_ITERATIONS; i++){
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    DollarAmount amount =
                            new DollarAmount(rnd.nextInt(1_000));

                    transferMoney(accounts[fromAcct], 
                                  accounts[toAcct], amount);
                }

                for(int i=0; i<NUM_THREADS; i++)
                    new TransferThread().start();
            }
        }
    }

    private static void transferMoney(Account account, Account account1, DollarAmount amount) {
    }

}
