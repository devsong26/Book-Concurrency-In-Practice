package chapter3;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * interrupt 메서드를 오버라이드해 표준에 정의되어 있지 않은 작업 중단 방법을 구현
 */
public class RenderThread extends Thread {
    private static final int BUFSZ = 1111;
    private final Socket socket;
    private final InputStream in;

    public RenderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
    }

    @Override
    public void interrupt(){
        try{
            socket.close();
        } catch (IOException ignored) {}
        finally {
            super.interrupt();
        }
    }

    @Override
    public void run(){
        try{
            byte[] buf = new byte[BUFSZ];
            while(true){
                int count = in.read(buf);
                if(count < 0) break;
                else if (count > 0) processBuffer(buf, count);
            }
        }catch(IOException e) {
            // 스레드를 종료한다.
            interrupt();
        }
    }

    private void processBuffer(byte[] buf, int count) {
    }

}
