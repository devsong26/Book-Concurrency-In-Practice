package chapter5;

public class MoneyExample {

    /**
     * 동적인 락 순서에 의한 데드락, 이런 코드는 금물!
     */
    public void transferMoney(Account fromAccount,
                              Account toAccount,
                              DollarAmount amount)
            throws InsufficientFundsException{
        synchronized(fromAccount){
            synchronized(toAccount){
                if(fromAccount.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else{
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }

    /**
     * 데드락을 방지하기 위해 락을 순서대로 확보하는 모습
     */
    private static final Object tieLock = new Object();

    public void transferMoney2(final Account fromAcct,
                              final Account toAcct,
                              final DollarAmount amount)
        throws InsufficientFundsException{
        class Helper {
            public void transfer() throws InsufficientFundsException{
                if(fromAcct.getBalance().compareTo(amount) < 0)
                    throw new InsufficientFundsException();
                else{
                    fromAcct.debit(amount);
                    toAcct.credit(amount);
                }
            }
        }

        int fromHash = System.identityHashCode(fromAcct);
        int toHash = System.identityHashCode(toAcct);

        if(fromHash < toHash){
            synchronized(fromAcct){
                synchronized(toAcct){
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized(toAcct){
                synchronized(fromAcct){
                    new Helper().transfer();
                }
            }
        } else {
            synchronized(tieLock) {
                synchronized (fromAcct){
                    synchronized(toAcct){
                        new Helper().transfer();
                    }
                }
            }
        }
    }

}
