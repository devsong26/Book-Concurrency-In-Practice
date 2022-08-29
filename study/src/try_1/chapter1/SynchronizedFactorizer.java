package try_1.chapter1;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.math.BigInteger;

/**
 * 마지막 결과를 캐시하지만 성능이 현저하게 떨어지는 서블릿. 이런 코드는 금물!
 */
@ThreadSafe
public class SynchronizedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;

    public synchronized void service(ServletRequest req,
                                     ServletResponse resp){
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber)){
            encodeIntoResponse(resp, lastFactors);
        } else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        throw new UnsupportedOperationException();
    }

    private BigInteger[] factor(BigInteger i) {
        throw new UnsupportedOperationException();
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
        throw new UnsupportedOperationException();
    }

}
