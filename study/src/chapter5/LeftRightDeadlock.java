package chapter5;

/**
 * 락 순서에 의한 데드락, 이런 코드는 금물!
 */
public class LeftRightDeadlock {
    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight(){
        synchronized(left){
            synchronized(right){
                doSomething();
            }
        }
    }

    private void doSomething() {
    }

    public void rightLeft(){
        synchronized (right){
            synchronized(left){
                doSomethingElse();
            }
        }
    }

    private void doSomethingElse() {

    }

}
