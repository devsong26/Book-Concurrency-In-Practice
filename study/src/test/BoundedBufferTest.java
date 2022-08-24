package test;

import chapter5.BoundedBuffer;
import junit.framework.TestCase;

/**
 * BoundedBuffer 클래스의 기능을 테스트하는 기본 테스트 케이스
 */
public class BoundedBufferTest extends TestCase {
    private static final long LOCKUP_DETECT_TIMEOUT = 1L;

    void testIsEmptyWhenConstructed(){
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        assertTrue(bb.isEmpty());
        assertFalse(bb.isFull());
    }

    void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        for(int i = 0; i < 10; i++)
            bb.put(i);

        assertTrue(bb.isFull());
        assertFalse(bb.isEmpty());
    }

    /**
     * 대기 상태와 인터럽트에 대한 대응을 테스트하는 루틴
     */
    void testTakeBlockWhenEmpty(){
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        Thread taker = new Thread(() -> {
            try{
                int unused = bb.take();
                fail();
            }catch(InterruptedException success){}
        });

        try{
            taker.start();
            Thread.sleep(LOCKUP_DETECT_TIMEOUT);
            taker.interrupt();
            taker.join(LOCKUP_DETECT_TIMEOUT);
            assertFalse(taker.isAlive());
        } catch (Exception unexpected){
            fail();
        }
    }

    /**
     * 테스트 프로그램에 적합한 중간 품질의 난수 발생기
     */
    static int xorShift(int y){
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }
}
