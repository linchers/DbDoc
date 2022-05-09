package com.database.doc.ui.util;

import java.net.URL;

/**
 *
 */
public final class ResourceLoader {

    private ResourceLoader() {
    }

    public static URL getResource(String path) {
        String resourcePath = "/images/" + path;
        return ResourceLoader.class.getResource(resourcePath);
    }

    public static URL getFxmlResource(String fxmlName) {
        String resourcePath = "/fxml/" + fxmlName;
        return ResourceLoader.class.getResource(resourcePath);
    }

    public static URL getCssResource(String fxmlName) {
        String resourcePath = "/css/" + fxmlName;
        return ResourceLoader.class.getResource(resourcePath);
    }
}
