package com.github.quadflask.react.navermap;

import com.naver.maps.map.overlay.OverlayImage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OverlayImages {
    private static Map<String, OverlayImage> store = new ConcurrentHashMap<>();

    public static void put(String uri, OverlayImage image) {
        store.put(uri, image);
    }

    public static OverlayImage get(String uri) {
        final OverlayImage overlayImage = store.get(uri);
        return overlayImage;
    }
}
