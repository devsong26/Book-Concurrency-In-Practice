package chapter2;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

import static chapter1.PreLoader.launderThrowable;

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

        for(final ImageInfo imageInfo : info)
            completionService.submit(imageInfo::downloadImage);

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

    private void renderImage(ImageData imageData) {
    }

    private void renderText(CharSequence source) {
    }

    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return null;
    }

}
