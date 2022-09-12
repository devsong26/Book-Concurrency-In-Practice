package try_2.ch5;

import java.util.ArrayList;
import java.util.List;

/**
 * 페이지 내용을 순차적으로 렌더링
 */
public class SingleThreadRenderer {
    void renderPage(CharSequence source){
        renderText(source);
        List<ImageData> imageData = new ArrayList<>();
        for(ImageInfo imageInfo : scanForImageInfo(source))
            imageData.add(imageInfo.downloadImage());

        for(ImageData data : imageData)
            renderImage(data);
    }

    private void renderText(CharSequence source) {}

    private ImageInfo[] scanForImageInfo(CharSequence source) {
        return null;
    }

    private void renderImage(ImageData data) {}

}
