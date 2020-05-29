package com.github.quadflask.react.navermap;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.naver.maps.map.NaverMapOptions;

public class RNNaverMapViewTextureManager extends RNNaverMapViewManager {
    public RNNaverMapViewTextureManager(ReactApplicationContext context) {
        super(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "RNNaverMapViewTexture";
    }

    @Override
    protected NaverMapOptions getNaverMapViewOptions() {
        return new NaverMapOptions()
                .useTextureView(true)
                .translucentTextureSurface(true);
    }
}
