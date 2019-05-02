package com.github.quadflask.react.navermap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
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
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RNNaverMapView extends MapView implements OnMapReadyCallback, NaverMap.OnCameraIdleListener {
    public static final String[] EVENT_NAMES = new String[]{
            "onInitialized",
            "onCameraChange",
    };

    private ThemedReactContext themedReactContext;
    private FusedLocationSource locationSource;
    private LifecycleEventListener lifecycleListener;
    private NaverMap naverMap;
    private List<OnMapReadyCallback> listeners = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private List<PolylineOverlay> polylineOverlays = new ArrayList<>();
    private Map<Integer, Pair<Marker, InfoWindow>> infoWindows = new HashMap<>();
    private PathOverlay path;

    private static boolean contextHasBug(Context context) {
        return context == null ||
                context.getResources() == null ||
                context.getResources().getConfiguration() == null;
    }

    private static Context getNonBuggyContext(ThemedReactContext reactContext,
                                              ReactApplicationContext appContext) {
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

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        this.naverMap.setLocationSource(locationSource);
        execAfterInit(null);
        this.onInitialized();

        lifecycleListener = new LifecycleEventListener() {
            @Override
            public void onHostResume() {
                onResume();
            }

            @Override
            public void onHostPause() {
                onPause();
            }

            @Override
            public void onHostDestroy() {
                onStop();
//                onDestroy();
                if (locationSource != null)
                    locationSource.deactivate();
                themedReactContext.removeLifecycleEventListener(lifecycleListener);
                themedReactContext = null;
            }
        };
        themedReactContext.addLifecycleEventListener(lifecycleListener);
    }

    private void execAfterInit(OnMapReadyCallback listener) {
        themedReactContext.getCurrentActivity().runOnUiThread(() -> {
            if (naverMap != null) {
                if (listener != null)
                    listener.onMapReady(naverMap);
                for (OnMapReadyCallback onMapReadyCallback : listeners) {
                    onMapReadyCallback.onMapReady(naverMap);
                }
                listeners.clear();
            } else {
                listeners.add(listener);
            }
        });
    }

    public void setCenter(LatLng latLng) {
        execAfterInit(e -> {
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng)
                    .animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        });
    }

    public void setCenter(LatLng latLng, Double zoom, Double tilt, Double bearing) {
        execAfterInit(e -> {
            if (zoom != null && tilt != null && bearing != null) {
                naverMap.moveCamera(CameraUpdate.toCameraPosition(new CameraPosition(latLng, zoom, tilt, bearing))
                        .animate(CameraAnimation.Easing));
            }
        });
    }

    public void zoomTo(LatLngBounds latLngBounds, int paddingInPx) {
        execAfterInit(e -> {
            CameraUpdate cameraUpdate = CameraUpdate.fitBounds(latLngBounds, paddingInPx)
                    .animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        });
    }

    public void setTilt(int tilt) {
        execAfterInit(e -> {
            final CameraPosition cameraPosition = naverMap.getCameraPosition();
            naverMap.moveCamera(CameraUpdate.toCameraPosition(new CameraPosition(cameraPosition.target, cameraPosition.zoom, tilt, cameraPosition.bearing)));
        });
    }

    public void setBearing(int bearing) {
        execAfterInit(e -> {
            final CameraPosition cameraPosition = naverMap.getCameraPosition();
            naverMap.moveCamera(CameraUpdate.toCameraPosition(new CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, bearing)));
        });
    }

    public void setMapPadding(int left, int top, int right, int bottom) {
        execAfterInit(e -> {
            naverMap.setContentPadding(left, top, right, bottom);
        });
    }

    public void setMarkers(List<Marker> markersArray) {
        execAfterInit(e -> {
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

    public void setPolyLine(List<PolylineOverlay> polyLineArray) {
        execAfterInit(e -> {
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
        execAfterInit(e -> {
            if (this.path != null) this.path.setMap(null);
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
        execAfterInit(e -> {
            naverMap.removeOnCameraIdleListener(this);
            naverMap.addOnCameraIdleListener(this);
        });
    }

    public void showsMyLocationButton(boolean show) {
        execAfterInit(e -> naverMap.getUiSettings().setLocationButtonEnabled(show));
    }

    public void setCompassEnabled(boolean show) {
        execAfterInit(e -> naverMap.getUiSettings().setCompassEnabled(show));
    }

    public void setScaleBarEnabled(boolean show) {
        execAfterInit(e -> naverMap.getUiSettings().setScaleBarEnabled(show));
    }

    public void setZoomControlEnabled(boolean show) {
        execAfterInit(e -> naverMap.getUiSettings().setZoomControlEnabled(show));
    }

    public void setLocationTrackingMode(int mode) {
        execAfterInit(e -> naverMap.setLocationTrackingMode(LocationTrackingMode.values()[mode]));
    }

    public void setMapType(NaverMap.MapType value) {
        execAfterInit(e -> naverMap.setMapType(value));
    }

    public void setBuildingHeight(float height) {
        execAfterInit(e -> naverMap.setBuildingHeight(height));
    }

    public void setLayerGroupEnabled(String layerGroup, boolean enable) {
        execAfterInit(e -> naverMap.setLayerGroupEnabled(layerGroup, enable));
    }

    public void setNightModeEnabled(boolean enable) {
        execAfterInit(e -> naverMap.setNightModeEnabled(enable));
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

    private final Map<PathOverlay, RNNaverMapPathOverlay> pathOverlayMap = new HashMap<>();

    public void addFeature(View child, int index) {
        execAfterInit(e -> {
            if (child instanceof RNNaverMapMarker) {
                RNNaverMapMarker annotation = (RNNaverMapMarker) child;
                annotation.addToMap(this);
                features.add(index, annotation);
                markerMap.put(annotation.getFeature(), annotation);
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

    private void emitEvent(String eventName, WritableMap param) {
        themedReactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), eventName, param);
    }

    public NaverMap getMap() {
        return naverMap;
    }
}
