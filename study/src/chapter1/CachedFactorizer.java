package chapter1;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.math.BigInteger;

/**
 * 최근 입력 값과 그 결과를 캐시하는 서블릿
 *
 * 서블릿: 클라이언트의 요청을 처리하고, 그 결과를 반환하는 Servlet 클래스의 구현 규칙을 지킨 자바 웹 프로그래밍 기술
 */
@ThreadSafe
public class CachedFactorizer implements Servlet {

    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;

    public synchronized long getHits(){return hits;}
    public synchronized double getCacheHitRatio(){
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
        throw new UnsupportedOperationException();
    }

    private BigInteger[] factor(BigInteger i) {
        throw new UnsupportedOperationException();
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
        throw new UnsupportedOperationException();
    }

}
