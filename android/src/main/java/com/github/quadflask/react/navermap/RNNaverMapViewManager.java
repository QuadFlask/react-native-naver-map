package com.github.quadflask.react.navermap;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;

import com.airbnb.android.react.maps.SizeReportingShadowNode;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import static com.github.quadflask.react.navermap.ReactUtil.getInt;
import static com.github.quadflask.react.navermap.ReactUtil.toNaverLatLng;

public class RNNaverMapViewManager extends ViewGroupManager<RNNaverMapView> {
    private final ReactApplicationContext appContext;
    private static FusedLocationSource locationSource;

    private static final int ANIMATE_TO_REGION = 1;
    private static final int ANIMATE_TO_COORDINATE = 2;
    private static final int ANIMATE_TO_TWO_COORDINATES = 3;
    private static final int SET_LOCATION_TRACKING_MODE = 4;
    private static final int WATCH_CAMERA_CHANGE = 5;
    private static final int ANIMATE_TO_COORDINATES = 6;

    public RNNaverMapViewManager(ReactApplicationContext context) {
        super();
        this.appContext = context;
        locationSource = new FusedLocationSource(context.getCurrentActivity(), 0x1000);
    }

    public static void onCreate(Activity activity) {
        locationSource = new FusedLocationSource(activity, 0x1000);
    }

    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        return locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @NonNull
    @Override
    public String getName() {
        return "RNNaverMapView";
    }

    @NonNull
    @Override
    protected RNNaverMapView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNNaverMapView(reactContext, appContext, locationSource);
    }

    @ReactProp(name = "center")
    public void setCenter(RNNaverMapView mapView, @Nullable ReadableMap option) {
        if (option != null) {
            if (option.hasKey("zoom") && option.hasKey("tilt") && option.hasKey("bearing")) {
                mapView.setCenter(toNaverLatLng(option), option.getDouble("zoom"), option.getDouble("tilt"), option.getDouble("bearing"));
            } else {
                mapView.setCenter(toNaverLatLng(option));
            }
        }
    }

    @ReactProp(name = "showsMyLocationButton", defaultBoolean = false)
    public void showsMyLocationButton(RNNaverMapView mapView, boolean show) {
        mapView.showsMyLocationButton(show);
    }

    @ReactProp(name = "compass", defaultBoolean = false)
    public void setCompassEnabled(RNNaverMapView mapView, boolean show) {
        mapView.setCompassEnabled(show);
    }

    @ReactProp(name = "scaleBar", defaultBoolean = false)
    public void setScaleBarEnabled(RNNaverMapView mapView, boolean show) {
        mapView.setScaleBarEnabled(show);
    }

    @ReactProp(name = "zoomControl", defaultBoolean = false)
    public void setZoomControlEnabled(RNNaverMapView mapView, boolean show) {
        mapView.setZoomControlEnabled(show);
    }

    @ReactProp(name = "mapType", defaultInt = 0)
    public void setMapType(RNNaverMapView mapView, int mapType) {
        mapView.setMapType(NaverMap.MapType.values()[mapType]);
    }

    @ReactProp(name = "buildingHeight", defaultFloat = 1.0f)
    public void setBuildingHeight(RNNaverMapView mapView, float height) {
        mapView.setBuildingHeight(height);
    }

    @ReactProp(name = "locationTrackingMode", defaultInt = 0)
    public void locationTrackingMode(RNNaverMapView mapView, int mode) {
        if (mode >= 0)
            mapView.setLocationTrackingMode(mode);
    }

    @ReactProp(name = "tilt", defaultInt = 0)
    public void setTilt(RNNaverMapView mapView, int tilt) {
        mapView.setTilt(tilt);
    }

    @ReactProp(name = "bearing", defaultInt = 0)
    public void setBearing(RNNaverMapView mapView, int bearing) {
        mapView.setBearing(bearing);
    }

    @ReactProp(name = "layerGroup")
    public void setLayerGroupEnabled(RNNaverMapView mapView, @Nullable ReadableMap option) {
        final List<String> layerGroups = Arrays.asList(
                NaverMap.LAYER_GROUP_BUILDING,
                NaverMap.LAYER_GROUP_TRANSIT,
                NaverMap.LAYER_GROUP_BICYCLE,
                NaverMap.LAYER_GROUP_TRAFFIC,
                NaverMap.LAYER_GROUP_CADASTRAL,
                NaverMap.LAYER_GROUP_MOUNTAIN
        );
        if (option != null) {
            for (String layerGroup : layerGroups) {
                mapView.setLayerGroupEnabled(layerGroup, option.getBoolean(layerGroup));
            }
        } else {
            for (String layerGroup : layerGroups) {
                mapView.setLayerGroupEnabled(layerGroup, false);
            }
        }
    }

    @ReactProp(name = "nightMode", defaultBoolean = false)
    public void setNightModeEnabled(RNNaverMapView mapView, boolean enable) {
        mapView.setNightModeEnabled(enable);
    }

    @ReactProp(name = "mapPadding")
    public void setMapPadding(RNNaverMapView mapView, @Nullable ReadableMap padding) {
        final Rect rect = getRect(padding, mapView.getResources().getDisplayMetrics().density);

        mapView.setMapPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    @ReactProp(name = "logoMargin")
    public void setLogoMargin(RNNaverMapView mapView, @Nullable ReadableMap margin) {
        final Rect rect = getRect(margin, mapView.getResources().getDisplayMetrics().density);

        mapView.setLogoMargin(rect.left, rect.top, rect.right, rect.bottom);
    }

    @ReactProp(name = "logoGravity")
    public void setLogoGravity(RNNaverMapView mapView, int gravity) {
        mapView.setLogoGravity(gravity);
    }

    private Rect getRect(@Nullable ReadableMap padding, float density) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        if (padding != null) {
            if (padding.hasKey("left")) {
                left = Math.round(((float) padding.getDouble("left") * density));
            }
            if (padding.hasKey("top")) {
                top = Math.round(((float) padding.getDouble("top") * density));
            }
            if (padding.hasKey("right")) {
                right = Math.round(((float) padding.getDouble("right") * density));
            }
            if (padding.hasKey("bottom")) {
                bottom = Math.round(((float) padding.getDouble("bottom") * density));
            }
        }

        return new Rect(left, top, right, bottom);
    }

    @Override
    public void receiveCommand(@NonNull RNNaverMapView mapView, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case ANIMATE_TO_REGION: {
                final ReadableMap region = args.getMap(0);
                final double lat = region.getDouble("latitude");
                final double lng = region.getDouble("longitude");
                final double latDelta = region.getDouble("latitudeDelta");
                final double lngDelta = region.getDouble("longitudeDelta");

                break;
            }
            case ANIMATE_TO_TWO_COORDINATES: {
                float density = mapView.getResources().getDisplayMetrics().density;

                final ReadableMap region1 = args.getMap(0);
                final ReadableMap region2 = args.getMap(1);
                final int padding = args.size() == 3 ? args.getInt(2) : 56;

                final double lat1 = region1.getDouble("latitude");
                final double lng1 = region1.getDouble("longitude");
                final double lat2 = region2.getDouble("latitude");
                final double lng2 = region2.getDouble("longitude");

                mapView.zoomTo(new LatLngBounds(
                                new LatLng(Math.max(lat1, lat2), Math.min(lng1, lng2)),
                                new LatLng(Math.min(lat1, lat2), Math.max(lng1, lng2))),
                        Math.round(padding * density));

                break;
            }
            case ANIMATE_TO_COORDINATES:
                ReadableArray coordinatesArray = args.getArray(0);
                ReadableMap edgePadding = args.getMap(1);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < coordinatesArray.size(); i++) {
                    builder.include(toNaverLatLng(coordinatesArray.getMap(i)));
                }

                int left = getInt(edgePadding, "left", 0);
                int top = getInt(edgePadding, "top", 0);
                int right = getInt(edgePadding, "right", 0);
                int bottom = getInt(edgePadding, "bottom", 0);

                mapView.moveCameraFitBound(builder.build(), left, top, right, bottom);

                break;
            case ANIMATE_TO_COORDINATE:
                mapView.setCenter(toNaverLatLng(args.getMap(0)));
                break;

            case SET_LOCATION_TRACKING_MODE:
                mapView.setLocationTrackingMode(args.getInt(0));
                break;

            case WATCH_CAMERA_CHANGE:
                mapView.watchCameraChange();
                break;
        }
    }

    @Override
    public java.util.Map<String, Integer> getCommandsMap() {
        return MapBuilder.<String, Integer>builder()
                .put("animateToRegion", ANIMATE_TO_REGION)
                .put("animateToCoordinate", ANIMATE_TO_COORDINATE)
                .put("animateToTwoCoordinates", ANIMATE_TO_TWO_COORDINATES)
                .put("setLocationTrackingMode", SET_LOCATION_TRACKING_MODE)
                .put("watchCameraChange", WATCH_CAMERA_CHANGE)
                .put("animateToCoordinates", ANIMATE_TO_COORDINATES)
                .build();
    }

    @Override
    public java.util.Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        final MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        for (String eventName : RNNaverMapView.EVENT_NAMES)
            builder.put(eventName, bubbled(eventName));
        return builder.build();
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        // A custom shadow node is needed in order to pass back the width/height of the map to the
        // view manager so that it can start applying camera moves with bounds.
        return new SizeReportingShadowNode();
    }

    @Override
    public void addView(RNNaverMapView parent, View child, int index) {
        parent.addFeature(child, index);
    }

    @Override
    public int getChildCount(RNNaverMapView view) {
        return view.getFeatureCount();
    }

    @Override
    public View getChildAt(RNNaverMapView view, int index) {
        return view.getFeatureAt(index);
    }

    @Override
    public void removeViewAt(RNNaverMapView parent, int index) {
        parent.removeFeatureAt(index);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    @Override
    public void onDropViewInstance(RNNaverMapView view) {
        view.onStop();
//        view.onDestroy();
        super.onDropViewInstance(view);
    }

    private java.util.Map<String, Object> bubbled(String callbackName) {
        return MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", callbackName));
    }

    private void getDp(ReadableMap option, String key, Consumer<Integer> consumer) {
        getInt(option, key, number -> {
            float density = appContext.getResources().getDisplayMetrics().density;
            consumer.accept(Math.round(density * number));
        });
    }
}
