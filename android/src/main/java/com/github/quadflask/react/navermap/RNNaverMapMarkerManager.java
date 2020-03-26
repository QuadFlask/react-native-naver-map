package com.github.quadflask.react.navermap;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import static com.github.quadflask.react.navermap.ReactUtil.toNaverLatLng;

public class RNNaverMapMarkerManager extends EventEmittableViewGroupManager<RNNaverMapMarker> {
    private final DisplayMetrics metrics;

    public RNNaverMapMarkerManager(ReactApplicationContext reactContext) {
        super(reactContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay()
                    .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }

    @Override
    String[] getEventNames() {
        return new String[]{
                "onClick"
        };
    }

    @NonNull
    @Override
    public String getName() {
        return "RNNaverMapMarker";
    }

    @NonNull
    @Override
    protected RNNaverMapMarker createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNNaverMapMarker(this, reactContext);
    }

    @ReactProp(name = "coordinate")
    public void setCoordinate(RNNaverMapMarker view, ReadableMap map) {
        view.setCoordinate(toNaverLatLng(map));
    }

    @ReactProp(name = "anchor")
    public void setAnchor(RNNaverMapMarker view, ReadableMap map) {
        // should default to (0.5, 1) (bottom middle)
        float x = map != null && map.hasKey("x") ? (float) map.getDouble("x") : 0.5f;
        float y = map != null && map.hasKey("y") ? (float) map.getDouble("y") : 1.0f;
        view.setAnchor(x, y);
    }

    @ReactProp(name = "image")
    public void setImage(RNNaverMapMarker view, @Nullable String source) {
        view.setImage(source);
    }

    @ReactProp(name = "pinColor", defaultInt = Color.RED, customType = "Color")
    public void setPinColor(RNNaverMapMarker view, int pinColor) {
        view.setIconTintColor(pinColor);
    }

    @ReactProp(name = "rotation", defaultFloat = 0.0f)
    public void setMarkerRotation(RNNaverMapMarker view, float rotation) {
        view.setRotation(rotation);
    }

    @ReactProp(name = "flat", defaultBoolean = false)
    public void setFlat(RNNaverMapMarker view, boolean flat) {
        view.setFlat(flat);
    }

    @ReactProp(name = "width", defaultFloat = 64)
    public void setWidth(RNNaverMapMarker view, float width) {
        int widthInScreenPx = Math.round(metrics.density * width);
        view.setWidth(widthInScreenPx);
    }

    @ReactProp(name = "height", defaultFloat = 64)
    public void setHeight(RNNaverMapMarker view, float height) {
        int heightInScreenPx = Math.round(metrics.density * height);
        view.setHeight(heightInScreenPx);
    }

    @ReactProp(name = "animated", defaultBoolean = false)
    public void setAnimated(RNNaverMapMarker view, boolean animated) {
        view.setAnimated(animated);
    }

    @ReactProp(name = "easing", defaultInt = -1)
    public void setEasing(RNNaverMapMarker view, int easingFunction) {
        view.setEasing(easingFunction);
    }

    @ReactProp(name = "duration", defaultInt = 500)
    public void setDuration(RNNaverMapMarker view, int duration) {
        view.setDuration(duration);
    }

    @ReactProp(name = "alpha", defaultFloat = 1f)
    public void setAlpha(RNNaverMapMarker view, float alpha) {
        view.setAlpha(alpha);
    }
}
