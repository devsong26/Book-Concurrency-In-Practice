package try_2.ch5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Future를 사용해 이미지 파일 다운로드 작업을 기다림
 */
public class FutureRenderer {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    void renderPage(CharSequence source){
        final List<ImageInfo> imageInfos = scanForImageInfo(source);
        Callable<List<ImageData>> task =
                () -> {
                    List<ImageData> result = new ArrayList<>();
                    for(ImageInfo imageInfo : imageInfos)
                        result.add(imageInfo.downloadImage());
                    return result;
                };

        Future<List<ImageData>> future = new CompletableFuture<>();
        try{
            List<ImageData> imageData = future.get();
            for(ImageData data : imageData)
                renderImage(data);
        }catch(InterruptedException e) {
            // 스레드의 인터럽트 상태를 재설정
            Thread.currentThread().interrupt();
            // 결과는 더 이상 필요없으니 해당 작업도 취소한다.
            future.cancel(true);
        }catch(ExecutionException e){
            throw launderThrowable(e.getCause());
        }
    }

    private List<ImageInfo> scanForImageInfo(CharSequence source) {
        return null;
    }

    private void renderImage(ImageData data) {
    }

    private RuntimeException launderThrowable(Throwable cause) {
        return null;
    }
}
