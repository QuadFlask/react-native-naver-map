package com.github.quadflask.react.navermap;

import android.content.Context;

import com.naver.maps.map.overlay.Overlay;

public abstract class RNNaverMapFeature<T extends Overlay> extends EventEmmitableReactView {
    protected T feature;

    public RNNaverMapFeature(EventEmittable emitter, Context context) {
        super(emitter, context);
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
}
