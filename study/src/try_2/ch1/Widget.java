package try_2.ch1;

/**
 * 암묵적인 락이 재진입 가능하지 않았다면 데드락에 빠졋을 코드
 */
public class Widget {
    public synchronized void doSomething(){}

}

class LoggingWidget extends Widget {
    public synchronized void doSomething(){
        System.out.println(toString() + " : calling doSomething");
        super.doSomething();
    }
}
