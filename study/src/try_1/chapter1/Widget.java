package try_1.chapter1;

/**
 * 암묵적인 락이 재진입 가능하지 않았다면 데드락에 빠졌을 코드
 */
public class Widget {
    public synchronized void doSomething(){
    }
}

class LoggingWidget extends Widget {
    public synchronized void doSomeThing(){
        System.out.println(this + ": calling doSomething");
        super.doSomething();
    }
}
