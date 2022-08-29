package try_2.ch1;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import try_1.chapter1.ServletRequest;
import try_1.chapter1.ServletResponse;

import java.math.BigInteger;

/**
 * 마지막 결과를 캐시하지만 성능이 현저하게 떨어지는 서블릿, 이런 코드는 금물!
 */
@ThreadSafe
public class SynchronizedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;

    // synchronized 키워드가 추가됨
    public synchronized void service(ServletRequest req,
                                     ServletResponse resp){
        BigInteger i = extractFromRequest(req);
        if(i.equals(lastNumber))
            encodeIntoResponse(resp, lastFactors);
        else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return null;
    }

    private BigInteger[] factor(BigInteger i) {
        return null;
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }


}
