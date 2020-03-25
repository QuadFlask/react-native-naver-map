package com.github.quadflask.react.navermap;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.naver.maps.map.overlay.PolylineOverlay;

import static com.github.quadflask.react.navermap.ReactUtil.toLatLngList;

public class RNNaverMapPolylineOverlayManager extends EventEmittableViewGroupManager<RNNaverMapPolylineOverlay> {
    private final DisplayMetrics metrics;

    public RNNaverMapPolylineOverlayManager(ReactApplicationContext reactContext) {
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
        return "RNNaverMapPolylineOverlay";
    }

    @NonNull
    @Override
    protected RNNaverMapPolylineOverlay createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNNaverMapPolylineOverlay(this, reactContext);
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

    @ReactProp(name = "capType", defaultInt = 0)
    public void setCapType(RNNaverMapPolylineOverlay view, int capType) {
        view.setCapType(PolylineOverlay.LineCap.values()[capType]);
    }

    @ReactProp(name = "joinType", defaultInt = 0)
    public void setJoinType(RNNaverMapPolylineOverlay view, int joinType) {
        view.setJoinType(PolylineOverlay.LineJoin.values()[joinType]);
    }

    @ReactProp(name = "pattern")
    public void setPattern(RNNaverMapPolylineOverlay view, ReadableArray pattern) {
        int[] patternArray = new int[pattern.size()];
        for (int i = 0; i < pattern.size(); i++)
            patternArray[i] = pattern.getInt(i);
        view.setPattern(patternArray);
    }
}
