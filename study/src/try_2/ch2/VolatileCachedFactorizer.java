package try_2.ch2;

import annotation.ThreadSafe;
import try_1.chapter1.ServletRequest;
import try_1.chapter1.ServletResponse;
import try_2.ch1.Servlet;

import java.math.BigInteger;

/**
 * 최신 값을 불변 객체에 넣어 volatile 변수에 보관
 */
@ThreadSafe
public class VolatileCachedFactorizer implements Servlet {
    private volatile OneValueCache cache =
            new OneValueCache(null, null);

    public void service(ServletRequest req, ServletResponse resp){
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);

        if(factors == null){
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }

        encodeIntoResponse(resp, factors);
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return BigInteger.ZERO;
    }

    private BigInteger[] factor(BigInteger i) {
        return new BigInteger[]{BigInteger.ZERO};
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {

    }
}
