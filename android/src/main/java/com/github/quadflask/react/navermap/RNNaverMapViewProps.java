package com.github.quadflask.react.navermap;

import android.view.View;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.NaverMap;

public interface RNNaverMapViewProps {
    String[] EVENT_NAMES = new String[]{
            "onInitialized",
            "onCameraChange",
            "onMapClick",
            "onTouch"
    };

    void onStop();

    void setCenter(LatLng latLng, Double zoom);

    void setCenter(LatLng latLng, Double zoom, Double tilt, Double bearing);

    void zoomTo(LatLngBounds latLngBounds, int paddingInPx);

    void setTilt(int tilt);

    void setBearing(int bearing);

    void setZoom(float zoom);

    void setMapPadding(int left, int top, int right, int bottom);

    void onInitialized();

    void showsMyLocationButton(boolean show);

    void setCompassEnabled(boolean show);

    void setScaleBarEnabled(boolean show);

    void setZoomControlEnabled(boolean show);

    void setLocationTrackingMode(int mode);

    void setMapType(NaverMap.MapType value);

    void setBuildingHeight(float height);

    void setMinZoom(float minZoomLevel);

    void setMaxZoom(float maxZoomLevel);

    void setLayerGroupEnabled(String layerGroup, boolean enable);

    void setNightModeEnabled(boolean enable);

    void setLogoMargin(int left, int top, int right, int bottom);

    void setLogoGravity(int gravity);

    void setScrollGesturesEnabled(boolean enabled);

    void setZoomGesturesEnabled(boolean enabled);

    void setTiltGesturesEnabled(boolean enabled);

    void setRotateGesturesEnabled(boolean enabled);

    void setStopGesturesEnabled(boolean enabled);

    void setLiteModeEnabled(boolean enabled);

    void moveCameraFitBound(LatLngBounds bounds, int left, int top, int right, int bottom);

    void addFeature(View child, int index);

    void removeFeatureAt(int index);

    int getFeatureCount();

    View getFeatureAt(int index);
}
