package try_1.chapter1;

import java.math.BigInteger;

/**
 * Memoizer를 사용해 결과를 캐시하는 인수분해 서블릿
 */
public class Factorizer implements Servlet{
    private final CellularAutomata.Computable<BigInteger, BigInteger[]> c =
            this::factor;

    private final CellularAutomata.Computable<BigInteger, BigInteger[]> cache =
            new CellularAutomata.Memoizer<>(c);

    public void service(ServletRequest req, ServletResponse resp) {
        try{
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        } catch(InterruptedException e){
            encodeError(resp, "factorization interrupted");
        }
    }

    private void encodeError(ServletResponse resp, String factorization_interrupted) {
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] compute) {
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return null;
    }

    private BigInteger[] factor(BigInteger arg) {
        return null;
    }

}
