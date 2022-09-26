package try_2.ch6;

public interface Computable <A, V>{

    V compute(A arg) throws InterruptedException;

}
