package try_2.ch1;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import try_1.chapter1.ServletRequest;
import try_1.chapter1.ServletResponse;

import java.math.BigInteger;

/**
 * 최근 입력 값과 그 결과를 캐시하는 서블릿
 */
@ThreadSafe
public class CachedFactorizer implements Servlet {
    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;

    public synchronized long getHit(){
        return hits;
    }
    public synchronized double getCachedHitRatio(){
        return (double) cacheHits / (double) hits;
    }

    public void service(ServletRequest req, ServletResponse resp){
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;

        synchronized (this) {
            ++hits;
            if(i.equals(lastNumber)){
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }

        if(factors == null){
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
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
