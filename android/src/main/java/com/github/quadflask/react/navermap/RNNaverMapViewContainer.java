package com.github.quadflask.react.navermap;

import android.view.Choreographer;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.util.FusedLocationSource;

public class RNNaverMapViewContainer extends FrameLayout implements RNNaverMapViewProps {
    private final ReactApplicationContext appContext;
    private final ThemedReactContext themedReactContext;
    private final FusedLocationSource locationSource;
    private final NaverMapOptions naverMapOptions;
    private RNNaverMapView mapView;
    private RNNaverMapView prevMapView;
    private boolean isAttachedToWindow = false;

    public RNNaverMapViewContainer(@NonNull ThemedReactContext themedReactContext, ReactApplicationContext appContext, FusedLocationSource locationSource, NaverMapOptions naverMapOptions) {
        super(ReactUtil.getNonBuggyContext(themedReactContext, appContext));
        this.appContext = appContext;
        this.themedReactContext = themedReactContext;
        this.locationSource = locationSource;
        this.naverMapOptions = naverMapOptions;
        mapView = new RNNaverMapView(themedReactContext, appContext, locationSource, naverMapOptions);
        addView(mapView);
    }

    // https://github.com/facebook/react-native/issues/17968#issuecomment-457236577
    private void setupLayoutHack() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren();
                getViewTreeObserver().dispatchOnGlobalLayout();
                if (isAttachedToWindow)
                    Choreographer.getInstance().postFrameCallbackDelayed(this, 500);
            }
        });
    }

    private void manuallyLayoutChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        if (mapView == null) {
            mapView = new RNNaverMapView(themedReactContext, appContext, locationSource, naverMapOptions);
            mapView.restoreFrom(prevMapView);
            addView(mapView);
        }
        mapView.setId(getId());
        setupLayoutHack();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        onStop();
        removeView(mapView);
        prevMapView = mapView;
        mapView = null;
    }

    @Override
    public void onStop() {
        if (mapView != null)
            mapView.onStop();
    }

    @Override
    public void setCenter(LatLng latLng) {
        if (mapView != null)
            mapView.setCenter(latLng);
    }

    @Override
    public void setCenter(LatLng latLng, Double zoom, Double tilt, Double bearing) {
        if (mapView != null)
            mapView.setCenter(latLng, zoom, tilt, bearing);
    }

    @Override
    public void zoomTo(LatLngBounds latLngBounds, int paddingInPx) {
        if (mapView != null)
            mapView.zoomTo(latLngBounds, paddingInPx);
    }

    @Override
    public void setTilt(int tilt) {
        if (mapView != null)
            mapView.setTilt(tilt);
    }

    @Override
    public void setBearing(int bearing) {
        if (mapView != null)
            mapView.setBearing(bearing);
    }

    @Override
    public void setZoom(float zoom) {
        if (mapView != null)
            mapView.setZoom(zoom);
    }

    @Override
    public void setMapPadding(int left, int top, int right, int bottom) {
        if (mapView != null)
            mapView.setMapPadding(left, top, right, bottom);
    }

    @Override
    public void onInitialized() {
        if (mapView != null)
            mapView.onInitialized();
    }

    @Override
    public void showsMyLocationButton(boolean show) {
        if (mapView != null)
            mapView.showsMyLocationButton(show);
    }

    @Override
    public void setCompassEnabled(boolean show) {
        if (mapView != null)
            mapView.setCompassEnabled(show);
    }

    @Override
    public void setScaleBarEnabled(boolean show) {
        if (mapView != null)
            mapView.setScaleBarEnabled(show);
    }

    @Override
    public void setZoomControlEnabled(boolean show) {
        if (mapView != null)
            mapView.setZoomControlEnabled(show);
    }

    @Override
    public void setLocationTrackingMode(int mode) {
        if (mapView != null)
            mapView.setLocationTrackingMode(mode);
    }

    @Override
    public void setMapType(NaverMap.MapType value) {
        if (mapView != null)
            mapView.setMapType(value);
    }

    @Override
    public void setBuildingHeight(float height) {
        if (mapView != null)
            mapView.setBuildingHeight(height);
    }

    @Override
    public void setLayerGroupEnabled(String layerGroup, boolean enable) {
        if (mapView != null)
            mapView.setLayerGroupEnabled(layerGroup, enable);
    }

    @Override
    public void setNightModeEnabled(boolean enable) {
        if (mapView != null)
            mapView.setNightModeEnabled(enable);
    }

    @Override
    public void setLogoMargin(int left, int top, int right, int bottom) {
        if (mapView != null)
            mapView.setLogoMargin(left, top, right, bottom);
    }

    @Override
    public void setLogoGravity(int gravity) {
        if (mapView != null)
            mapView.setLogoGravity(gravity);
    }

    @Override
    public void moveCameraFitBound(LatLngBounds bounds, int left, int top, int right, int bottom) {
        if (mapView != null)
            mapView.moveCameraFitBound(bounds, left, top, right, bottom);
    }

    @Override
    public void addFeature(View child, int index) {
        if (mapView != null)
            mapView.addFeature(child, index);
    }

    @Override
    public void removeFeatureAt(int index) {
        if (mapView != null)
            mapView.removeFeatureAt(index);
    }

    @Override
    public int getFeatureCount() {
        if (mapView != null)
            return mapView.getFeatureCount();
        return 0;
    }

    @Override
    public View getFeatureAt(int index) {
        if (mapView != null)
            return mapView.getFeatureAt(index);
        return null;
    }
}
