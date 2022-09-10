package try_2.ch4;

public interface Computable<A, V>{
    V compute(A arg) throws InterruptedException;
}
