package com.github.quadflask.react.navermap;

import android.content.Context;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.PolygonOverlay;

import java.util.List;

public class RNNaverMapPolygonOverlay extends ClickableRNNaverMapFeature<PolygonOverlay> {
    public RNNaverMapPolygonOverlay(EventEmittable emitter, Context context) {
        super(emitter, context);
        feature = new PolygonOverlay();
    }

    public void setCoords(List<LatLng> coords) {
        feature.setCoords(coords);
    }

    public void setColor(int color) {
        feature.setColor(color);
    }

    public void setOutlineWidth(float widthInScreenPx) {
        feature.setOutlineWidth(Math.round(widthInScreenPx));
    }

    public void setOutlineColor(int color) {
        feature.setOutlineColor(color);
    }

    public void setHoles(List<List<LatLng>> holes) {
        feature.setHoles(holes);
    }
}
