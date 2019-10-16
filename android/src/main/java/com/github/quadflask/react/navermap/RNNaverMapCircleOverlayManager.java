package com.github.quadflask.react.navermap;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import androidx.annotation.NonNull;

import static com.github.quadflask.react.navermap.ReactUtil.toNaverLatLng;

public class RNNaverMapCircleOverlayManager extends ViewGroupManager<RNNaverMapCircleOverlay> {
    private final DisplayMetrics metrics;

    public RNNaverMapCircleOverlayManager(ReactApplicationContext reactContext) {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "RNNaverMapCircleOverlay";
    }

    @NonNull
    @Override
    protected RNNaverMapCircleOverlay createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNNaverMapCircleOverlay(reactContext);
    }

    @ReactProp(name = "coordinate")
    public void setCenter(RNNaverMapCircleOverlay view, ReadableMap map) {
        view.setCenter(toNaverLatLng(map));
    }

    @ReactProp(name = "radius", defaultDouble = 100.0)
    public void setRadius(RNNaverMapCircleOverlay view, double radius) {
        view.setRadius(radius);
    }

    @ReactProp(name = "color", defaultInt = Color.RED, customType="Color")
    public void setColor(RNNaverMapCircleOverlay view, int color){
        view.setColor(color);
    }

}
