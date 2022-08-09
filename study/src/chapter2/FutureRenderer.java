package chapter2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Future를 사용해 이미지 파일 다운로드 작업을 기다림
 */
public class FutureRenderer {
    private final ExecutorService executor = new ExecutorService() {
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

        @Override
        public Future<List<ImageData>> submit(Callable<List<ImageData>> task) {
            return null;
        }

        @Override
        public void execute(Runnable command) {

        }
    };

    void renderPage(CharSequence source) throws Exception {
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =
                () -> {
                    List<ImageData> result
                            = new ArrayList<>();

                    for (ImageInfo imageInfo : imageInfos)
                        result.add(imageInfo.downloadImage());

                    return result;
                };

        Future<List<ImageData>> future = executor.submit(task);
        renderText(source);

        try{
            List<ImageData> imageData = future.get();
            for(ImageData data : imageData)
                renderImage(data);
        } catch (ExecutionException e) {
            throw launderThrowable(e.getCause());
        } catch (InterruptedException e) {
            // 스레드의 인터럽트 상태를 재설정
            Thread.currentThread().interrupt();
            // 결과는 더 이상 필요없으니 해당 작업도 취소한다.
            future.cancel(true);
        }
    }

    private Exception launderThrowable(Throwable cause) {
        return null;
    }

    private void renderImage(ImageData data) {
    }

    private void renderText(CharSequence source) {
    }

    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return null;
    }
}
