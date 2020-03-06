package com.github.quadflask.react.navermap;

import android.content.Context;

import com.facebook.react.bridge.Callback;
import com.facebook.react.views.view.ReactViewGroup;
import com.naver.maps.map.overlay.Overlay;

public abstract class RNNaverMapFeature<T extends Overlay> extends ReactViewGroup {
    protected T feature;

    public RNNaverMapFeature(Context context) {
        super(context);
    }

    public void addToMap(RNNaverMapView map) {
        feature.setMap(map.getMap());
    }

    public void removeFromMap() {
        feature.setMap(null);
        feature = null;
    }

    public T getFeature() {
        return feature;
    }

    public void setOnClickListener(Callback callback) {
        feature.setOnClickListener(overlay -> {
            callback.invoke(overlay);
            return true;
        });
    }
}
