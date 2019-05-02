package com.github.quadflask.react.navermap;

import android.content.Context;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.PolylineOverlay;

import java.util.List;

public class RNNaverMapPolylineOverlay extends RNNaverMapFeature<PolylineOverlay> {
    public RNNaverMapPolylineOverlay(Context context) {
        super(context);
        feature = new PolylineOverlay();
    }

    public void setCoords(List<LatLng> coords) {
        feature.setCoords(coords);
    }

    public void setLineWidth(float widthInScreenPx) {
        feature.setWidth(Math.round(widthInScreenPx));
    }

    public void setLineColor(int color) {
        feature.setColor(color);
    }
}
