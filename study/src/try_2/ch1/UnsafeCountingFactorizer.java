package try_2.ch1;

import annotation.NotThreadSafe;
import try_1.chapter1.ServletRequest;
import try_1.chapter1.ServletResponse;

import java.math.BigInteger;

/**
 * 동기화 구문없이 요청 횟수를 세는 서블릿, 이런 코드는 금물!
 */
@NotThreadSafe
public class UnsafeCountingFactorizer implements Servlet{
    private long count = 0;

    public long getCount(){
        return count;
    }

    // 동기화 처리가 안되어 있는 메서드이므로 스레드에 안전하지 않음
    public void service(ServletRequest req, ServletResponse resp){
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;
        encodeIntoResponse(resp, factors);
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        return BigInteger.ZERO;
    }

    private BigInteger[] factor(BigInteger i) {
        return null;
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

}
