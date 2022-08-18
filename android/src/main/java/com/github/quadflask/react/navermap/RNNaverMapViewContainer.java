package com.github.quadflask.react.navermap;

import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;

public class RNNaverMapViewContainer extends FrameLayout implements RNNaverMapViewProps {
    private RNNaverMapView mapView;
    private Bundle instanceStateBundle = new Bundle();
    private boolean isAttachedToWindow = false;

    public RNNaverMapViewContainer(@NonNull ThemedReactContext themedReactContext, ReactApplicationContext appContext, NaverMapOptions naverMapOptions) {
        super(ReactUtil.getNonBuggyContext(themedReactContext, appContext));
        this.mapView = new RNNaverMapView(themedReactContext, appContext, naverMapOptions, instanceStateBundle);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mapView != null && mapView.getMap().getUiSettings().isScrollGesturesEnabled()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        mapView.onStart();
        mapView.setId(getId());
        setupLayoutHack();
    }

    @Override
    protected void onDetachedFromWindow() {
        isAttachedToWindow = false;
        if (mapView != null)
            mapView.onSaveInstanceState(instanceStateBundle);
        super.onDetachedFromWindow();
    }

    public void onDropViewInstance() {
        mapView.onStop();
        mapView.onDestroy();
        removeAllViews();
        instanceStateBundle.clear();
        instanceStateBundle = null;
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
    public void setMinZoom(float minZoom) {
        if (mapView != null)
            mapView.setMinZoom(minZoom);
    }

    @Override
    public void setMaxZoom(float maxZoom) {
        if (mapView != null)
            mapView.setMaxZoom(maxZoom);
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
    public void setScrollGesturesEnabled(boolean enabled) {
        if (mapView != null)
            mapView.setScrollGesturesEnabled(enabled);
    }

    @Override
    public void setZoomGesturesEnabled(boolean enabled) {
        if (mapView != null)
            mapView.setZoomGesturesEnabled(enabled);
    }

    @Override
    public void setTiltGesturesEnabled(boolean enabled) {
        if (mapView != null)
            mapView.setTiltGesturesEnabled(enabled);
    }

    @Override
    public void setRotateGesturesEnabled(boolean enabled) {
        if (mapView != null)
            mapView.setRotateGesturesEnabled(enabled);
    }

    @Override
    public void setStopGesturesEnabled(boolean enabled) {
        if (mapView != null)
            mapView.setStopGesturesEnabled(enabled);
    }

    @Override
    public void setLiteModeEnabled(boolean enabled) {
        if (mapView != null)
            mapView.setLiteModeEnabled(enabled);
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
