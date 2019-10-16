package com.github.quadflask.react.navermap;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import androidx.annotation.NonNull;

import static com.github.quadflask.react.navermap.ReactUtil.getLatLngList;
import static com.github.quadflask.react.navermap.ReactUtil.toLatLngList;

public class RNNaverMapPolylineOverlayManager extends ViewGroupManager<RNNaverMapPolylineOverlay> {
    private final DisplayMetrics metrics;

    public RNNaverMapPolylineOverlayManager(ReactApplicationContext reactContext) {
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

    @NonNull
    @Override
    public String getName() {
        return "RNNaverMapPolylineOverlay";
    }

    @NonNull
    @Override
    protected RNNaverMapPolylineOverlay createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNNaverMapPolylineOverlay(reactContext);
    }

    @ReactProp(name = "coordinates")
    public void setCoordinate(RNNaverMapPolylineOverlay view, ReadableArray coordinates) {
        view.setCoords(toLatLngList(coordinates));
    }

    @ReactProp(name = "strokeWidth", defaultFloat = 1f)
    public void setStrokeWidth(RNNaverMapPolylineOverlay view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints;
        view.setLineWidth(widthInScreenPx);
    }

    @ReactProp(name = "strokeColor", defaultInt = Color.RED, customType = "Color")
    public void setStrokeColor(RNNaverMapPolylineOverlay view, int color) {
        view.setLineColor(color);
    }
}
