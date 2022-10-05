package try_2.ch6;

import java.util.logging.Level;
import java.util.logging.Logger;

// 예외 내용을 로그 파일에 출력하는 UncaughtExceptionHandler
public class UEHLogger implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE,
                "Thread terminated with exception: " + t.getName(),
                e);
    }

}
