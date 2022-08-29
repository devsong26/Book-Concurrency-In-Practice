package try_1.chapter1;

/**
 * 생성 메서드에서 this 변수가 유출되지 않도록 팩토리 메서드를 사용하는 모습
 */
public class SafeListener {
    private final EventListener listener;

    private SafeListener(){
        listener = new EventListener() {
            public void onEvent(Event e){
                doSomething(e);
            }
        };
    }

    private void doSomething(Event e) {
    }

    public static SafeListener newInstance(EventSource source){
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

}
