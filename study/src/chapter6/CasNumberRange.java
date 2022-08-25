package chapter6;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS를 사용해 다중 변수의 안전성을 보장하는 예
 */
public class CasNumberRange {
    @Immutable
    private static class IntPair{
        final int lower;    // 불변조건: lower <= upper
        final int upper;

        public IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPair> values =
            new AtomicReference<>(new IntPair(0, 0));

    public int getLower(){
        return values.get().lower;
    }

    public int getUpper(){
        return values.get().upper;
    }

    public void setLower(int i){
        while(true){
            IntPair oldV = values.get();
            if(i > oldV.upper)
                throw new IllegalArgumentException("Can't set lower to " + i + " > upper");
            
            IntPair newV = new IntPair(i, oldV.upper);
            if(values.compareAndSet(oldV, newV))
                return;
        }
    }
    // setUpper 메서드도 setLower와 비슷하다
}
