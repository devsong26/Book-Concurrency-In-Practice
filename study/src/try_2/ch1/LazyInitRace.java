package try_2.ch1;

import annotation.NotThreadSafe;

/**
 * 늦은 초기화에서 발생한 경쟁 조건, 이런 코드는 금물!
 */
@NotThreadSafe
public class LazyInitRace {
    private ExpensiveFunction instance = null;

    public ExpensiveFunction getInstance(){
        if(instance == null)
            instance = new ExpensiveObject();
        return instance;
    }
}
