package com.github.quadflask.react.navermap;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

public class RNNaverMapView extends MapView implements OnMapReadyCallback, NaverMap.OnCameraIdleListener, NaverMap.OnMapClickListener {
    public static final String[] EVENT_NAMES = new String[]{
            "onInitialized",
            "onCameraChange",
            "onMapClick",
            "onTouch"
    };

    private ThemedReactContext themedReactContext;
    private FusedLocationSource locationSource;
    private LifecycleEventListener lifecycleListener;
    private NaverMap naverMap;
    private List<Marker> markers = new ArrayList<>();
    private List<CircleOverlay> circleOverlays = new ArrayList<>();
    private List<PolylineOverlay> polylineOverlays = new ArrayList<>();
    private Map<Integer, Pair<Marker, InfoWindow>> infoWindows = new HashMap<>();
    private PathOverlay path;

    private static boolean contextHasBug(Context context) {
        return context == null ||
                context.getResources() == null ||
                context.getResources().getConfiguration() == null;
    }

    private static Context getNonBuggyContext(ThemedReactContext reactContext, ReactApplicationContext appContext) {
        Context superContext = reactContext;
        if (!contextHasBug(appContext.getCurrentActivity())) {
            superContext = appContext.getCurrentActivity();
        } else if (contextHasBug(superContext)) {
            // we have the bug! let's try to find a better context to use
            if (!contextHasBug(reactContext.getCurrentActivity())) {
                superContext = reactContext.getCurrentActivity();
            } else if (!contextHasBug(reactContext.getApplicationContext())) {
                superContext = reactContext.getApplicationContext();
            } else {
                // ¯\_(ツ)_/¯
            }
        }
        return superContext;
    }

    public RNNaverMapView(@NonNull ThemedReactContext themedReactContext, ReactApplicationContext appContext, FusedLocationSource locationSource) {
        super(getNonBuggyContext(themedReactContext, appContext));
        this.themedReactContext = themedReactContext;
        this.locationSource = locationSource;
        super.onCreate(null);
        super.onStart();
        getMapAsync(this);

        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren();
                getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    void manuallyLayoutChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
            child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
        }
    }

    long lastTouch = 0;

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        this.naverMap.setLocationSource(locationSource);
        this.naverMap.setOnMapClickListener(this);
        this.naverMap.addOnCameraChangeListener((reason, animated) -> {
            if (reason == -1 && System.currentTimeMillis() - lastTouch > 1000) { // changed by user
                emitEvent("onTouch", null);
                lastTouch = System.currentTimeMillis();
            }
        });
        this.onInitialized();

        Log.e("RNNaverMapView.onMapReady", "onMapReady");

        lifecycleListener = new LifecycleEventListener() {
            @Override
            public void onHostResume() {
                // onResume();
            }

            @Override
            public void onHostPause() {
                // onPause();
            }

            @Override
            public void onHostDestroy() {
                // onStop();
                // onDestroy();
                if (locationSource != null)
                    locationSource.deactivate();
                themedReactContext.removeLifecycleEventListener(lifecycleListener);
                themedReactContext = null;
            }
        };
        themedReactContext.addLifecycleEventListener(lifecycleListener);
    }

    public void setCenter(LatLng latLng) {
        getMapAsync(e -> {
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        });
    }

    public void setCenter(LatLng latLng, Double zoom, Double tilt, Double bearing) {
        getMapAsync(e -> {
            if (zoom != null && tilt != null && bearing != null) {
                naverMap.moveCamera(CameraUpdate.toCameraPosition(new CameraPosition(latLng, zoom, tilt, bearing))
                        .animate(CameraAnimation.Easing));
            }
        });
    }

    public void zoomTo(LatLngBounds latLngBounds, int paddingInPx) {
        getMapAsync(e -> {
            CameraUpdate cameraUpdate = CameraUpdate.fitBounds(latLngBounds, paddingInPx)
                    .animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        });
    }

    public void setTilt(int tilt) {
        getMapAsync(e -> {
            final CameraPosition cameraPosition = naverMap.getCameraPosition();
            naverMap.moveCamera(CameraUpdate.toCameraPosition(
                    new CameraPosition(cameraPosition.target, cameraPosition.zoom, tilt, cameraPosition.bearing)));
        });
    }

    public void setBearing(int bearing) {
        getMapAsync(e -> {
            final CameraPosition cameraPosition = naverMap.getCameraPosition();
            naverMap.moveCamera(CameraUpdate.toCameraPosition(
                    new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, bearing)));
        });
    }

    public void setMapPadding(int left, int top, int right, int bottom) {
        getMapAsync(e -> {
            naverMap.setContentPadding(left, top, right, bottom);
        });
    }

    public void setMarkers(List<Marker> markersArray) {
        getMapAsync(e -> {
            for (Marker marker : markers) {
                marker.setMap(null);
            }
            markers.clear();

            if (markersArray != null && markersArray.size() > 0) {
                markers = markersArray;
                for (Marker marker : markers) {
                    marker.setMap(naverMap);
                }
            }
        });
    }

    public void setCircle(List<CircleOverlay> circleArray) {
        getMapAsync(e -> {
            for (CircleOverlay circleOverlay : circleOverlays) {
                circleOverlay.setMap(null);
            }
            circleOverlays.clear();

            if (circleArray != null && circleArray.size() > 0) {
                circleOverlays = circleArray;
                for (CircleOverlay circleOverlay : circleOverlays) {
                    circleOverlay.setMap(naverMap);
                }
            }
        });
    }

    public void setPolyLine(List<PolylineOverlay> polyLineArray) {
        getMapAsync(e -> {
            for (PolylineOverlay polylineOverlay : polylineOverlays) {
                polylineOverlay.setMap(null);
            }
            polylineOverlays.clear();

            if (polyLineArray != null && polyLineArray.size() > 0) {
                polylineOverlays = polyLineArray;
                for (PolylineOverlay polylineOverlay : polylineOverlays) {
                    polylineOverlay.setMap(naverMap);
                }
            }
        });
    }

    public void setPath(PathOverlay path) {
        getMapAsync(e -> {
            if (this.path != null)
                this.path.setMap(null);
            if (path != null) {
                this.path = path;
                this.path.setMap(naverMap);
            }
        });
    }

    public void onInitialized() {
        emitEvent("onInitialized", null);
    }

    public void watchCameraChange() {
        getMapAsync(e -> {
            naverMap.removeOnCameraIdleListener(this);
            naverMap.addOnCameraIdleListener(this);
        });
    }

    public void showsMyLocationButton(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setLocationButtonEnabled(show));
    }

    public void setCompassEnabled(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setCompassEnabled(show));
    }

    public void setScaleBarEnabled(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setScaleBarEnabled(show));
    }

    public void setZoomControlEnabled(boolean show) {
        getMapAsync(e -> naverMap.getUiSettings().setZoomControlEnabled(show));
    }

    public void setLocationTrackingMode(int mode) {
        getMapAsync(e -> naverMap.setLocationTrackingMode(LocationTrackingMode.values()[mode]));
    }

    public void setMapType(NaverMap.MapType value) {
        getMapAsync(e -> naverMap.setMapType(value));
    }

    public void setBuildingHeight(float height) {
        getMapAsync(e -> naverMap.setBuildingHeight(height));
    }

    public void setLayerGroupEnabled(String layerGroup, boolean enable) {
        getMapAsync(e -> naverMap.setLayerGroupEnabled(layerGroup, enable));
    }

    public void setNightModeEnabled(boolean enable) {
        getMapAsync(e -> naverMap.setNightModeEnabled(enable));
    }

    public void openInfoWindow(int id, LatLng at, final String string) {
        final Pair<Marker, InfoWindow> oldInfoWindow = infoWindows.get(id);
        if (oldInfoWindow != null) {
            closeInfoWindow(id);
        }
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(themedReactContext) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return string;
            }
        });
        infoWindow.setPosition(at);
        infoWindow.open(naverMap);
        infoWindows.put(id, Pair.create(null, infoWindow));
    }

    public void closeInfoWindow(int id) {
        final Pair<Marker, InfoWindow> infoWindow = infoWindows.get(id);
        if (infoWindow != null) {
            if (infoWindow.first != null)
                infoWindow.first.setMap(null);
            infoWindow.second.close();
            infoWindow.second.setMap(null);
            infoWindows.remove(id);
        }
    }

    private final List<RNNaverMapFeature> features = new ArrayList<>();
    private final Map<Marker, RNNaverMapMarker> markerMap = new HashMap<>();
    private final Map<PolylineOverlay, RNNaverMapPolylineOverlay> polylineMap = new HashMap<>();
    private final Map<CircleOverlay, RNNaverMapCircleOverlay> circleMap = new HashMap<>();

    private final Map<PathOverlay, RNNaverMapPathOverlay> pathOverlayMap = new HashMap<>();

    public void addFeature(View child, int index) {
        getMapAsync(e -> {
            if (child instanceof RNNaverMapMarker) {
                RNNaverMapMarker annotation = (RNNaverMapMarker) child;
                annotation.addToMap(this);
                features.add(index, annotation);
                markerMap.put(annotation.getFeature(), annotation);
            } else if (child instanceof RNNaverMapCircleOverlay) {
                RNNaverMapCircleOverlay annotation = (RNNaverMapCircleOverlay) child;
                annotation.addToMap(this);
                features.add(index, annotation);
                circleMap.put(annotation.getFeature(), annotation);
            } else if (child instanceof RNNaverMapPolylineOverlay) {
                RNNaverMapPolylineOverlay annotation = (RNNaverMapPolylineOverlay) child;
                annotation.addToMap(this);
                features.add(index, annotation);
                polylineMap.put(annotation.getFeature(), annotation);
            } else if (child instanceof RNNaverMapPathOverlay) {
                RNNaverMapPathOverlay annotation = (RNNaverMapPathOverlay) child;
                annotation.addToMap(this);
                features.add(index, annotation);
                pathOverlayMap.put(annotation.getFeature(), annotation);
            }
        });
    }

    public void removeFeatureAt(int index) {
        RNNaverMapFeature feature = features.remove(index);
        if (feature instanceof RNNaverMapMarker) {
            markerMap.remove(feature.getFeature());
        } else if (feature instanceof RNNaverMapCircleOverlay) {
            circleMap.remove(feature.getFeature());
        } else if (feature instanceof RNNaverMapPolylineOverlay) {
            polylineMap.remove(feature.getFeature());
        } else if (feature instanceof RNNaverMapPathOverlay) {
            pathOverlayMap.remove(feature.getFeature());
        }
        feature.removeFromMap();
    }

    public int getFeatureCount() {
        return features.size();
    }

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

    private void emitEvent(String eventName, WritableMap param) {
        themedReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), eventName, param);
    }

    public NaverMap getMap() {
        return naverMap;
    }
}
