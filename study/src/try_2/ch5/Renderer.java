package try_2.ch5;


import java.util.List;
import java.util.concurrent.*;

/**
 * CompletionService를 사용해 페이지 구성 요소를 받아오는 즉시 렌더링
 */
public class Renderer {
    private final ExecutorService executor;

    public Renderer(ExecutorService executor) {
        this.executor = executor;
    }

    void renderPage(CharSequence source){
        final List<ImageInfo> info = scanForImageInfo(source);
        CompletionService<ImageData> completionService =
                new ExecutorCompletionService<>(executor);

        for(final ImageInfo imageInfo : info){
            completionService.submit(() -> imageInfo.downloadImage());
        }

        renderText(source);

        try{
            for(int t = 0, n = info.size(); t < n; t++){
                Future<ImageData> f = completionService.take();
                ImageData imageData = f.get();
                renderImage(imageData);
            }
        } catch (ExecutionException e) {
            Thread.currentThread().interrupt();
        } catch (InterruptedException e) {
            throw launderThrowable(e.getCause());
        }
    }

    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return null;
    }

    private void renderText(CharSequence source) {
    }

    private void renderImage(ImageData imageData) {
    }

    private RuntimeException launderThrowable(Throwable cause) {
        return null;
    }

    /**
     * 제한된 시간 안에 광고 가져오기
     */
    private long TIME_BUDGET = 1L;
    private Ad DEFAULT_AD;

    Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        Future<Ad> f = executor.submit(new FetchAdTask());

        //광고 가져오는 작업을 등록했으니, 원래 페이지를 작업한다.
        Page page = renderPageBody();
        Ad ad;

        try{
            // 남은 시간 만큼만 대기한다.
            long timeLeft = endNanos - System.nanoTime();
            ad = f.get(timeLeft, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
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
