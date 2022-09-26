package try_2.ch6;

import java.math.BigInteger;

public class ExpensiveFunction implements Computable<String, BigInteger>{

    public BigInteger compute(String arg){
        // 잠시 생각 좀 하고...

        return new BigInteger(arg);
    }

}
