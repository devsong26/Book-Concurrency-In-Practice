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
}
