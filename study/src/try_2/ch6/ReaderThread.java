package try_2.ch6;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * interrupt 메서드를 오버라이드해 표준에 정의되어 있지 않은 작업 중단 방법을 구현
 */
public class ReaderThread extends Thread{

    private final Socket socket;
    private final InputStream in;
    private int BUFSZ;

    public ReaderThread(Socket socket, InputStream in) {
        this.socket = socket;
        this.in = in;
    }

    public void interrupt(){
        try{
            socket.close();
        } catch (IOException ignored) {}
        finally {
            super.interrupt();
        }
    }

    public void run(){
        try{
            byte[] buf = new byte[BUFSZ];
            while(true){
                int count = in.read(buf);
                if(count < 0) break;
                else if ( count > 0) processBuffer(buf, count);
            }
        } catch (IOException e) {
            interrupt(); // 스레드를 종료한다.
        }
    }

    private void processBuffer(byte[] buf, int count) {
    }

}
