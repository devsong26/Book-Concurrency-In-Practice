package try_2.ch6;

import annotation.ThreadSafe;
import try_2.ch1.Servlet;

import java.math.BigInteger;

@ThreadSafe
public class Factorizer implements Servlet {

    private final Computable<BigInteger, BigInteger[]> c = (arg) -> factor(arg);
    private final Computable<BigInteger, BigInteger[]> cache = new Memoizer<>(c);

    private BigInteger[] factor(BigInteger arg) {
        return new BigInteger[10];
    }

    public void service(ServletRequest req, ServletResponse resp) {
        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        } catch (InterruptedException e) {
            encodeError(resp, "factorization interrupted");
        }
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return null;
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] compute) {
    }

    private void encodeError(ServletResponse resp, String factorization_interrupted) {

    }

}
