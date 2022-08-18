package com.github.quadflask.react.navermap;

import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.airbnb.android.react.maps.ViewAttacherGroup;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.*;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class RNNaverMapView extends MapView implements OnMapReadyCallback, NaverMap.OnCameraIdleListener, NaverMap.OnMapClickListener, RNNaverMapViewProps {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private ThemedReactContext themedReactContext;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private NaverMapSdk naverMapSdk;
    private ViewAttacherGroup attacherGroup;
    private long lastTouch = 0;
    private final List<RNNaverMapFeature<?>> features = new ArrayList<>();

    public RNNaverMapView(@NonNull ThemedReactContext themedReactContext, ReactApplicationContext appContext, NaverMapOptions naverMapOptions, Bundle instanceStateBundle) {
        super(ReactUtil.getNonBuggyContext(themedReactContext, appContext), naverMapOptions);
        this.themedReactContext = themedReactContext;
        this.locationSource = new FusedLocationSource(appContext.getCurrentActivity(), LOCATION_PERMISSION_REQUEST_CODE);
        super.onCreate(instanceStateBundle);
        naverMapSdk = NaverMapSdk.getInstance(appContext);
//        super.onStart();
        getMapAsync(this);

        // Set up a parent view for triggering visibility in subviews that depend on it.
        // Mainly ReactImageView depends on Fresco which depends on onVisibilityChanged() event
        attacherGroup = new ViewAttacherGroup(this.themedReactContext);
        LayoutParams attacherLayoutParams = new LayoutParams(0, 0);
        attacherLayoutParams.width = 0;
        attacherLayoutParams.height = 0;
        attacherLayoutParams.leftMargin = 99999999;
        attacherLayoutParams.topMargin = 99999999;
        attacherGroup.setLayoutParams(attacherLayoutParams);
        addView(attacherGroup);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        this.naverMap.setLocationSource(locationSource);
        this.naverMap.setOnMapClickListener(this);
        this.naverMap.addOnCameraIdleListener(this);
        this.naverMap.addOnCameraChangeListener((reason, animated) -> {
            if (reason == -1 && System.currentTimeMillis() - lastTouch > 500) { // changed by user
                WritableMap param = Arguments.createMap();
                param.putInt("reason", reason);
                param.putBoolean("animated", animated);
                emitEvent("onTouch", param);
                lastTouch = System.currentTimeMillis();
            }
        });
        naverMapSdk.flushCache(() -> Log.i("NaverMap", "Map Cache Clean"));
        onInitialized();
    }

    @Override
    public void setCenter(LatLng latLng) {
        getMapAsync(e -> {
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        });
    }

    @Override
    public void setCenter(LatLng latLng, Double zoom, Double tilt, Double bearing) {
        getMapAsync(e -> {
            CameraPosition cam = naverMap.getCameraPosition();
            double zoomValue = zoom == null ? cam.zoom : zoom;
            double tiltValue = tilt == null ? cam.tilt : tilt;
            double bearingValue = bearing == null ? cam.bearing : bearing;

            naverMap.moveCamera(CameraUpdate.toCameraPosition(new CameraPosition(latLng, zoomValue, tiltValue, bearingValue))
                    .animate(CameraAnimation.Easing));
        });
    }

    @Override
    public void zoomTo(LatLngBounds latLngBounds, int paddingInPx) {
        getMapAsync(e -> {
            CameraUpdate cameraUpdate = CameraUpdate.fitBounds(latLngBounds, paddingInPx)
                    .animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        });
    }

    @Override
    public void setTilt(int tilt) {
        getMapAsync(e -> {
            final CameraPosition cameraPosition = naverMap.getCameraPosition();
            naverMap.moveCamera(CameraUpdate.toCameraPosition(
                    new CameraPosition(cameraPosition.target, cameraPosition.zoom, tilt, cameraPosition.bearing)));
        });
    }

    @Override
    public void setBearing(int bearing) {
        getMapAsync(e -> {
            final CameraPosition cameraPosition = naverMap.getCameraPosition();
            naverMap.moveCamera(CameraUpdate.toCameraPosition(
                    new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, bearing)));
        });
    }

    @Override
    public void setZoom(float zoom) {
        getMapAsync(e -> {
            naverMap.moveCamera(CameraUpdate.zoomTo(zoom));
        });
    }

    @Override
    public void setMapPadding(int left, int top, int right, int bottom) {
        getMapAsync(e -> {
            naverMap.setContentPadding(left, top, right, bottom);
        });
    }

    @Override
    public void onInitialized() {
        emitEvent("onInitialized", null);
    }

    @Override
    public void showsMyLocationButton(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setLocationButtonEnabled(show));
    }

    @Override
    public void setCompassEnabled(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setCompassEnabled(show));
    }

    @Override
    public void setScaleBarEnabled(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setScaleBarEnabled(show));
    }

    @Override
    public void setZoomControlEnabled(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setZoomControlEnabled(show));
    }

    @Override
    public void setLocationTrackingMode(int mode) {
        getMapAsync(e -> naverMap.setLocationTrackingMode(LocationTrackingMode.values()[mode]));
    }

    @Override
    public void setMapType(NaverMap.MapType value) {
        getMapAsync(e -> naverMap.setMapType(value));
    }

    @Override
    public void setMinZoom(float minZoomLevel) {
        getMapAsync(e -> naverMap.setMinZoom(minZoomLevel));
    }

    @Override
    public void setMaxZoom(float maxZoomLevel) {
        getMapAsync(e -> naverMap.setMaxZoom(maxZoomLevel));
    }

    @Override
    public void setBuildingHeight(float height) {
        getMapAsync(e -> naverMap.setBuildingHeight(height));
    }

    @Override
    public void setLayerGroupEnabled(String layerGroup, boolean enable) {
        getMapAsync(e -> naverMap.setLayerGroupEnabled(layerGroup, enable));
    }

    @Override
    public void setNightModeEnabled(boolean enable) {
        getMapAsync(e -> naverMap.setNightModeEnabled(enable));
    }

    @Override
    public void setLogoMargin(int left, int top, int right, int bottom) {
        getMapAsync(e -> naverMap.getUiSettings().setLogoMargin(left, top, right, bottom));
    }

    @Override
    public void setLogoGravity(int gravity) {
        getMapAsync(e -> naverMap.getUiSettings().setLogoGravity(gravity));
    }

    @Override
    public void setScrollGesturesEnabled(boolean enabled) {
        getMapAsync(e -> naverMap.getUiSettings().setScrollGesturesEnabled(enabled));
    }

    @Override
    public void setZoomGesturesEnabled(boolean enabled) {
        getMapAsync(e -> naverMap.getUiSettings().setZoomGesturesEnabled(enabled));
    }

    @Override
    public void setTiltGesturesEnabled(boolean enabled) {
        getMapAsync(e -> naverMap.getUiSettings().setTiltGesturesEnabled(enabled));
    }

    @Override
    public void setRotateGesturesEnabled(boolean enabled) {
        getMapAsync(e -> naverMap.getUiSettings().setRotateGesturesEnabled(enabled));
    }

    @Override
    public void setStopGesturesEnabled(boolean enabled) {
        getMapAsync(e -> naverMap.getUiSettings().setStopGesturesEnabled(enabled));
    }

    @Override
    public void setLiteModeEnabled(boolean enabled) {
        getMapAsync(e -> naverMap.setLiteModeEnabled(enabled));
    }

    @Override
    public void moveCameraFitBound(LatLngBounds bounds, int left, int top, int right, int bottom) {
        getMapAsync(e -> naverMap.moveCamera(CameraUpdate.fitBounds(bounds, left, top, right, bottom).animate(CameraAnimation.Fly, 500)));
    }

    @Override
    public void addFeature(View child, int index) {
        getMapAsync(e -> {
            if (child instanceof RNNaverMapFeature) {
                RNNaverMapFeature<?> annotation = (RNNaverMapFeature<?>) child;
                annotation.addToMap(this);
                features.add(index, annotation);
                int visibility = annotation.getVisibility();
                annotation.setVisibility(View.INVISIBLE);
                ViewGroup annotationParent = (ViewGroup) annotation.getParent();
                if (annotationParent != null) {
                    annotationParent.removeView(annotation);
                }
                // Add to the parent group
                attacherGroup.addView(annotation);
                annotation.setVisibility(visibility);
            }
        });
    }

    @Override
    public void removeFeatureAt(int index) {
        RNNaverMapFeature<?> feature = features.remove(index);
        feature.removeFromMap();
    }

    @Override
    public int getFeatureCount() {
        return features.size();
    }

    @Override
    public View getFeatureAt(int index) {
        return features.get(index);
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = naverMap.getCameraPosition();

        WritableMap param = Arguments.createMap();
        param.putDouble("latitude", cameraPosition.target.latitude);
        param.putDouble("longitude", cameraPosition.target.longitude);
        param.putDouble("zoom", cameraPosition.zoom);
        param.putArray("contentRegion", ReactUtil.toWritableArray(naverMap.getContentRegion()));
        param.putArray("coveringRegion", ReactUtil.toWritableArray(naverMap.getCoveringRegion()));

        emitEvent("onCameraChange", param);
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        WritableMap param = Arguments.createMap();
        param.putDouble("x", pointF.x);
        param.putDouble("y", pointF.y);
        param.putDouble("latitude", latLng.latitude);
        param.putDouble("longitude", latLng.longitude);

        emitEvent("onMapClick", param);
    }

    @Override
    public void onDestroy() {
        removeAllViews();
        themedReactContext = null;
        locationSource = null;
        naverMap = null;
        attacherGroup = null;
        for (RNNaverMapFeature<?> feature : features)
            feature.removeFromMap();
        features.clear();
        super.onDestroy();
    }

    private void emitEvent(String eventName, WritableMap param) {
        themedReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), eventName, param);
    }

    public NaverMap getMap() {
        return naverMap;
    }
}
