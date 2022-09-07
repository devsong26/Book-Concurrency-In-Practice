package try_2.ch4;

import try_1.chapter1.DataLoadException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutureTask를 사용해 추후 필요한 데이터를 미리 읽어들이는 모습
 */
public class PreLoader {
    private final FutureTask<ProductInfo> future =
            new FutureTask<>(new Callable<>(){
               public ProductInfo call() throws DataLoadException{
                   return loadProductInfo();
               }

                private ProductInfo loadProductInfo() {
                   return null;
                }
            });

    private final Thread thread = new Thread(future);

    public void start(){
        thread.start();
    }

    public ProductInfo get()
        throws DataLoadException, InterruptedException{
        try{
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if(cause instanceof DataLoadException){
                throw (DataLoadException) cause;
            }else{
                throw launderThrowable(cause);            }
        }
        }

//    private DataLoadException launderThrowable(Throwable cause) {
//        return null;
//    }

    /**
     * Throwable을 RuntimeException 으로 변환
     */
    public static RuntimeException launderThrowable(Throwable t){
        if(t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("RuntimeException 이 아님", t);
    }

}
