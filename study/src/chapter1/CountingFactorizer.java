package chapter1;

import annotation.ThreadSafe;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * AtomicLong 객체를 이용해 요청 횟수를 세는 서블릿
 */
@ThreadSafe
public class CountingFactorizer implements Servlet{
    private final AtomicLong count = new AtomicLong(0);

    public long getCount(){return count.get();}

    public void service(ServletRequest req, ServletResponse resp){
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(resp, factors);
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
