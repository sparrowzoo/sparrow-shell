package com.sparrow.image;

import com.sparrow.protocol.enums.EDITOR_TYPE;
import com.sparrow.utility.StringUtility;

import java.util.HashMap;
import java.util.Map;

public class ImageExtractorRegistry {
    private Map<String, ImageExtractor> container = new HashMap<>();

    public void register(ImageExtractor instance) {
        String simpleName = instance.getClass().getSimpleName();
        simpleName = simpleName.substring(0, simpleName.indexOf("ImageExtractor"));
        this.container.put(StringUtility.humpToLower(simpleName).toUpperCase(), instance);
    }

    public ImageExtractor get(EDITOR_TYPE name) {
        ImageExtractor imageExtractor = this.container.get(name.name());
        if (imageExtractor == null) {
            imageExtractor = this.container.get(EDITOR_TYPE.HTML.name());
        }
        return imageExtractor;
    }
}
