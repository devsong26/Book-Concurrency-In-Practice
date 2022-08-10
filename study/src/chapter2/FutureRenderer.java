package chapter2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Future를 사용해 이미지 파일 다운로드 작업을 기다림
 */
public class FutureRenderer {
    private static final long TIME_BUDGET = 1;
    private static final Ad DEFAULT_AD = new Ad();
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
        public Future submit(Callable task) {
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

    /**
     * 제한된 시간 안에 광고 가져오기
     */
    Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;

        Future<Ad> f = executor.submit(new FetchAdTask());
        // 광고 가져오는 작업을 등록했으니, 원래 페이지를 작업한다.
        Page page = renderPageBody();

        Ad ad;

        try{
            // 남은 시간 만큼만 대기한다.
            long timeLeft = endNanos - System.nanoTime();
            ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
            e.printStackTrace();
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            f.cancel(true);
        }

        page.setAd(ad);
        return page;
    }

    private Page renderPageBody() {
        return null;
    }
}
