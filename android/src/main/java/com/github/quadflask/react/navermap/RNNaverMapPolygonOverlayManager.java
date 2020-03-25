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
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import static com.github.quadflask.react.navermap.ReactUtil.toLatLngList;

public class RNNaverMapPolygonOverlayManager extends EventEmittableViewGroupManager<RNNaverMapPolygonOverlay> {
    private final DisplayMetrics metrics;

    public RNNaverMapPolygonOverlayManager(ReactApplicationContext reactContext) {
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
        return "RNNaverMapPolygonOverlay";
    }

    @NonNull
    @Override
    protected RNNaverMapPolygonOverlay createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNNaverMapPolygonOverlay(this, reactContext);
    }

    @ReactProp(name = "coordinates")
    public void setCoordinate(RNNaverMapPolygonOverlay view, ReadableArray coordinates) {
        view.setCoords(toLatLngList(coordinates));
    }

    @ReactProp(name = "outlineWidth", defaultFloat = 1f)
    public void setOutlineWidth(RNNaverMapPolygonOverlay view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints;
        view.setOutlineWidth(widthInScreenPx);
    }

    @ReactProp(name = "outlineColor", defaultInt = Color.RED, customType = "Color")
    public void setOutlineColor(RNNaverMapPolygonOverlay view, int color) {
        view.setOutlineColor(color);
    }

    @ReactProp(name = "color", defaultInt = Color.RED, customType = "Color")
    public void setColor(RNNaverMapPolygonOverlay view, int color) {
        view.setColor(color);
    }

    @ReactProp(name = "holes")
    public void setHoles(RNNaverMapPolygonOverlay view, ReadableArray holes) {
        List<List<LatLng>> holesArray = new ArrayList<>(holes.size());
        for (int i = 0; i < holes.size(); i++) {
            ReadableArray hole = holes.getArray(i);
            if (hole.size() >= 3)
                holesArray.add(toLatLngList(hole));
        }
        view.setHoles(holesArray);
    }
}
