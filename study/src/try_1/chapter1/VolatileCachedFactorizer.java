package try_1.chapter1;

import annotation.ThreadSafe;

import java.math.BigInteger;

/**
 *  최신 값을 불변 객체에 넣어 volatile 변수에 보관
 */
@ThreadSafe 
public class VolatileCachedFactorizer implements Servlet {

    private volatile OneValueCache cache = new OneValueCache(null, null);

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
        return null;
    }

    private BigInteger[] factor(BigInteger i) {
        return null;
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    /**
     * 동기화 하지 않고 객체를 외부에 공개. 이런 코드는 금물!
     * public 으로 내부 필드를 접근가능하도록 한 것
     */
    public Holder holder;

    public void initialize(){
        holder = new Holder(42);
    }

}
