package try_1.chapter5;

import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * SwingUtilities를 활용하는 Executor
 */
public class GuiExecutor extends AbstractExecutorService {
    // 싱글톤 객체의 생성 메서드는 private이고, public인 팩토리 메서드를 사용
    private static final GuiExecutor instance = new GuiExecutor();

    private GuiExecutor(){}

    public static GuiExecutor instance(){
        return instance;
    }

    public void execute(Runnable r){
        if(SwingUtilities.isEventDispatchThread()){
            r.run();
        }else{
            SwingUtilities.invokeLater(r);
        }
    }

    // Executor의 몇가지 기본 메서드는 생략
    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * 간단한 이벤트 리스너
     */
    public void foo(){
        final Random random = new Random();
        final JButton button = new JButton("Change Color");

        button.addActionListener(e -> button.setBackground(new Color(random.nextInt())));

        /**
         * 화면에서 이벤트가 발생했을 때 장시간 실행되는 작업을 시작하는 방법
         */
        ExecutorService backgroundExec = Executors.newCachedThreadPool();

        button.addActionListener(e -> backgroundExec.execute(() -> doBigComputation()));

        /**
         * 장시간 실행되는 작업의 결과를 화면에 표시하는 코드
         */
        button.addActionListener(e -> {
            button.setEnabled(false);
            Label label = new Label();
            label.setText("busy");
            backgroundExec.execute(() -> {
               try{
                   doBigComputation();
               }  finally {
                   GuiExecutor.instance().execute(() -> {
                      button.setEnabled(true);
                      label.setText("idle");
                   });
               }
            });
        });

        /**
         * 장시간 싱핼되는 작업 중단하기
         */
        final Future<?>[] runningTask = {null}; // 스레드 한정

        Button startButton= new Button();
        startButton.addActionListener(e -> {
            if(runningTask[0] == null){
                runningTask[0] = backgroundExec.submit(() -> {
                    while(moreWork()){
                        if(Thread.interrupted()){
                            cleanUpPartialWork();
                            break;
                        }
                        doSomeWork();
                    }
                });
            }
        });
    }

    private void doSomeWork() {

    }

    private void cleanUpPartialWork() {
    }

    private boolean moreWork() {
        return false;
    }

    private void doBigComputation() {
    }

    /**
     * 작업 중단, 작업 중단 알림, 진행 상태 알림 등의 기능을 갖고 있는 작업 클래스
     */
    abstract class BackgroundTask<V> implements Runnable, Future<V> {
        private final FutureTask<V> computation = new Computation();

        private class Computation extends FutureTask<V>{
            public Computation(){
                super(() -> BackgroundTask.this.compute());
            }

            protected final void done(){
                GuiExecutor.instance().execute(() -> {
                    V value = null;
                    Throwable thrown = null;
                    boolean cancelled = false;

                    try{
                        value = get();
                    } catch (ExecutionException e) {
                        thrown = e.getCause();
                    } catch (CancellationException e) {
                        cancelled = true;
                    } catch (InterruptedException e) {
                    } finally {
                        onCompletion(value, thrown, cancelled);
                    }
                });
            }
        }

        protected void setProgress(final int current, final int max) {
            GuiExecutor.instance().execute(() -> {
                onProgress(current, max);
            });
        }

        // 백그라운드 작업 스레드에서 호출함
        protected abstract V compute()  throws Exception;

        // 이벤트 스레드에서 호출함
        protected void onCompletion(V value, Throwable thrown, boolean cancelled) { }

        private void onProgress(int current, int max) { }
        
        // 기타 여러가지 Future 메서드
    }

    /**
     * BackgroundTask를 활용해 장시간 실행되며 중단 가능한 작업 실행
     */
    public void foo2(){
        Button startButton = new Button();
        Button cancelButton = new Button();
        Label label = new Label();
        ExecutorService backgroundExec = Executors.newCachedThreadPool();

        startButton.addActionListener(e -> {
            class CancelListener implements ActionListener{
                BackgroundTask<?> task;
                public void actionPerformed(ActionEvent event){
                    if(task != null) task.cancel(true);
                }
            }

            final CancelListener listener = new CancelListener();
            listener.task = new BackgroundTask<Void>(){
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return false;
                }

                @Override
                public Void get() throws InterruptedException, ExecutionException {
                    return null;
                }

                @Override
                public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                    return null;
                }

                @Override
                public void run() {

                }

                public Void compute(){
                    while(moreWork() && !isCancelled())
                        doSomeWork();

                    return null;
                }

                public void onCompletion(boolean cancelled, String s, Throwable exception){
                    cancelButton.removeActionListener(listener);
                    label.setText("done");
                }
            };

            cancelButton.addActionListener(listener);
            backgroundExec.execute(listener.task);
        });
    }
}
