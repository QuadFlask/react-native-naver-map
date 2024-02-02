package com.github.quadflask.react.navermap;

import android.content.Context;

import androidx.annotation.NonNull;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.Overlay;

public class RNNaverMapCircleOverlay extends ClickableRNNaverMapFeature<CircleOverlay> {
    public RNNaverMapCircleOverlay(EventEmittable emitter, Context context) {
        super(emitter, context);
        feature = new CircleOverlay();
    }

    public void setCenter(LatLng center) {
        feature.setCenter(center);
    }

    public void setRadius(double radius) {
        feature.setRadius(radius);
    }

    public void setColor(int color) {
        feature.setColor(color);
    }

    public void setOutlineWidth(int width) {
        feature.setOutlineWidth(width);
    }

    public void setOutlineColor(int color) {
        feature.setOutlineColor(color);
    }

    public void setZIndex(int zIndex) {
        feature.setZIndex(zIndex);
    }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        super.onClick(overlay);
        return false;
    }
}
