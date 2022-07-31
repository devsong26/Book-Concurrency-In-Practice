package chapter1;

import annotation.ThreadSafe;

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
        return null;
    }

    private BigInteger[] factor(BigInteger i) {
        return null;
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {

    }

}
