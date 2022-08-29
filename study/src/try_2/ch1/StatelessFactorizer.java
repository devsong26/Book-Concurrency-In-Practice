package try_2.ch1;

import annotation.ThreadSafe;
import try_1.chapter1.ServletRequest;
import try_1.chapter1.ServletResponse;

import java.math.BigInteger;

/**
 * 상태없는 서블릿
 */
@ThreadSafe
public class StatelessFactorizer implements Servlet{
    public void service(ServletRequest req, ServletResponse resp){
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
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
