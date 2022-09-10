package try_2.ch4;

import try_1.chapter1.ServletRequest;
import try_1.chapter1.ServletResponse;
import try_2.ch1.Servlet;

import java.math.BigInteger;

/**
 * Memoizer를 사용해 결과를 캐시하는 인수분해 서블릿
 */
public class Factorizer implements Servlet {
    private final Computable<BigInteger, BigInteger[]> c =
            args -> factor(args);

    private final Computable<BigInteger, BigInteger[]> cache
            = new Sample.Memoizer<>(c);

    public void service(ServletRequest req,
                        ServletResponse resp){
        try{
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        } catch (InterruptedException e) {
            encodeError(resp, "factorization interrupted");
        }
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return BigInteger.ZERO;
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] compute) {
    }

    private void encodeError(ServletResponse resp, String factorization_interrupted) {
    }

    private BigInteger[] factor(BigInteger args) {
        return null;
    }

}
