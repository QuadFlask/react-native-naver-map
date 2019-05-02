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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.quadflask.react.navermap.ReactUtil.toNaverLatLng;

public class RNNaverMapMarkerManager extends ViewGroupManager<RNNaverMapMarker> {
    private final DisplayMetrics metrics;

    public RNNaverMapMarkerManager(ReactApplicationContext reactContext) {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay()
                    .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return "RNNaverMapMarker";
    }

    @Nonnull
    @Override
    protected RNNaverMapMarker createViewInstance(@Nonnull ThemedReactContext reactContext) {
        return new RNNaverMapMarker(reactContext);
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
}
