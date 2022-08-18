package com.github.quadflask.react.navermap;

import android.graphics.Rect;
import android.view.View;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.naver.maps.map.NaverMapOptions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.quadflask.react.navermap.ReactUtil.getDoubleOrNull;
import static com.github.quadflask.react.navermap.ReactUtil.getInt;
import static com.github.quadflask.react.navermap.ReactUtil.toNaverLatLng;

public class RNNaverMapViewManager extends ViewGroupManager<RNNaverMapViewContainer> {
    private final ReactApplicationContext appContext;

    private static final int ANIMATE_TO_REGION = 1;
    private static final int ANIMATE_TO_COORDINATE = 2;
    private static final int ANIMATE_TO_TWO_COORDINATES = 3;
    private static final int SET_LOCATION_TRACKING_MODE = 4;
    private static final int ANIMATE_TO_COORDINATES = 6;
    private static final int SET_LAYER_GROUP_ENABLED = 7;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private static final List<String> LAYER_GROUPS = Collections.unmodifiableList(Arrays.asList(
            NaverMap.LAYER_GROUP_BUILDING,
            NaverMap.LAYER_GROUP_TRANSIT,
            NaverMap.LAYER_GROUP_BICYCLE,
            NaverMap.LAYER_GROUP_TRAFFIC,
            NaverMap.LAYER_GROUP_CADASTRAL,
            NaverMap.LAYER_GROUP_MOUNTAIN
    ));

    public RNNaverMapViewManager(ReactApplicationContext context) {
        super();
        this.appContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "RNNaverMapView";
    }

    @NonNull
    @Override
    protected RNNaverMapViewContainer createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RNNaverMapViewContainer(reactContext, appContext, getNaverMapViewOptions());
    }

    @Override
    public void onDropViewInstance(@NonNull RNNaverMapViewContainer view) {
        super.onDropViewInstance(view);
        view.onDropViewInstance();
    }

    protected NaverMapOptions getNaverMapViewOptions() {
        return null;
    }

    @ReactProp(name = "center")
    public void setCenter(RNNaverMapViewContainer mapView, @Nullable ReadableMap option) {
        if (option != null) {
            mapView.setCenter(
                    toNaverLatLng(option),
                    getDoubleOrNull(option, "zoom"),
                    getDoubleOrNull(option, "tilt"),
                    getDoubleOrNull(option, "bearing"));
        }
    }

    @ReactProp(name = "showsMyLocationButton", defaultBoolean = false)
    public void showsMyLocationButton(RNNaverMapViewContainer mapView, boolean show) {
        mapView.showsMyLocationButton(show);
    }

    @ReactProp(name = "compass", defaultBoolean = false)
    public void setCompassEnabled(RNNaverMapViewContainer mapView, boolean show) {
        mapView.setCompassEnabled(show);
    }

    @ReactProp(name = "scaleBar", defaultBoolean = false)
    public void setScaleBarEnabled(RNNaverMapViewContainer mapView, boolean show) {
        mapView.setScaleBarEnabled(show);
    }

    @ReactProp(name = "zoomControl", defaultBoolean = false)
    public void setZoomControlEnabled(RNNaverMapViewContainer mapView, boolean show) {
        mapView.setZoomControlEnabled(show);
    }

    @ReactProp(name = "mapType", defaultInt = 0)
    public void setMapType(RNNaverMapViewContainer mapView, int mapType) {
        mapView.setMapType(NaverMap.MapType.values()[mapType]);
    }

    @ReactProp(name = "minZoomLevel", defaultFloat = 0)
    public void setMinZoom(RNNaverMapViewContainer mapView, float minZoomLevel) {
        mapView.setMinZoom(minZoomLevel);
    }

    @ReactProp(name = "maxZoomLevel", defaultFloat = 21.0f)
    public void setMaxZoom(RNNaverMapViewContainer mapView, float maxZoomLevel) {
        mapView.setMaxZoom(maxZoomLevel);
    }

    @ReactProp(name = "buildingHeight", defaultFloat = 1.0f)
    public void setBuildingHeight(RNNaverMapViewContainer mapView, float height) {
        mapView.setBuildingHeight(height);
    }

    @ReactProp(name = "locationTrackingMode", defaultInt = 0)
    public void locationTrackingMode(RNNaverMapViewContainer mapView, int mode) {
        if (mode >= 0)
            mapView.setLocationTrackingMode(mode);
    }

    @ReactProp(name = "tilt", defaultInt = 0)
    public void setTilt(RNNaverMapViewContainer mapView, int tilt) {
        mapView.setTilt(tilt);
    }

    @ReactProp(name = "bearing", defaultInt = 0)
    public void setBearing(RNNaverMapViewContainer mapView, int bearing) {
        mapView.setBearing(bearing);
    }

    @ReactProp(name = "layerGroup")
    public void setLayerGroupEnabled(RNNaverMapViewContainer mapView, @Nullable ReadableMap option) {
        if (option != null) {
            for (String layerGroup : LAYER_GROUPS) {
                mapView.setLayerGroupEnabled(layerGroup, option.getBoolean(layerGroup));
            }
        } else {
            for (String layerGroup : LAYER_GROUPS) {
                mapView.setLayerGroupEnabled(layerGroup, false);
            }
        }
    }

    @ReactProp(name = "nightMode", defaultBoolean = false)
    public void setNightModeEnabled(RNNaverMapViewContainer mapView, boolean enable) {
        mapView.setNightModeEnabled(enable);
    }

    @ReactProp(name = "mapPadding")
    public void setMapPadding(RNNaverMapViewContainer mapView, @Nullable ReadableMap padding) {
        final Rect rect = getRect(padding, mapView.getResources().getDisplayMetrics().density);

        mapView.setMapPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    @ReactProp(name = "logoMargin")
    public void setLogoMargin(RNNaverMapViewContainer mapView, @Nullable ReadableMap margin) {
        final Rect rect = getRect(margin, mapView.getResources().getDisplayMetrics().density);

        mapView.setLogoMargin(rect.left, rect.top, rect.right, rect.bottom);
    }

    @ReactProp(name = "logoGravity")
    public void setLogoGravity(RNNaverMapViewContainer mapView, int gravity) {
        mapView.setLogoGravity(gravity);
    }

    @ReactProp(name = "scrollGesturesEnabled", defaultBoolean = true)
    public void setScrollGesturesEnabled(RNNaverMapViewContainer mapView, boolean enabled) {
        mapView.setScrollGesturesEnabled(enabled);
    }

    @ReactProp(name = "zoomGesturesEnabled", defaultBoolean = true)
    public void setZoomGesturesEnabled(RNNaverMapViewContainer mapView, boolean enabled) {
        mapView.setZoomGesturesEnabled(enabled);
    }

    @ReactProp(name = "tiltGesturesEnabled", defaultBoolean = true)
    public void setTiltGesturesEnabled(RNNaverMapViewContainer mapView, boolean enabled) {
        mapView.setTiltGesturesEnabled(enabled);
    }

    @ReactProp(name = "rotateGesturesEnabled", defaultBoolean = true)
    public void setRotateGesturesEnabled(RNNaverMapViewContainer mapView, boolean enabled) {
        mapView.setRotateGesturesEnabled(enabled);
    }

    @ReactProp(name = "stopGesturesEnabled", defaultBoolean = true)
    public void setStopGesturesEnabled(RNNaverMapViewContainer mapView, boolean enabled) {
        mapView.setStopGesturesEnabled(enabled);
    }

    @ReactProp(name = "liteModeEnabled", defaultBoolean = false)
    public void setLiteModeEnabled(RNNaverMapViewContainer mapView, boolean enabled) {
        mapView.setLiteModeEnabled(enabled);
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
    public void receiveCommand(@NonNull RNNaverMapViewContainer mapView, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case ANIMATE_TO_REGION: {
                final ReadableMap region = args.getMap(0);
                final double lat = region.getDouble("latitude");
                final double lng = region.getDouble("longitude");
                final double latDelta = region.getDouble("latitudeDelta");
                final double lngDelta = region.getDouble("longitudeDelta");

                LatLngBounds bounds = new LatLngBounds(
                        new LatLng(lat - latDelta / 2, lng - lngDelta / 2), // southwest
                        new LatLng(lat + latDelta / 2, lng + lngDelta / 2)  // northeast
                );
                mapView.moveCameraFitBound(bounds, 0, 0, 0, 0);

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

            case SET_LAYER_GROUP_ENABLED:
                String group = args.getString(0);
                Boolean enable = args.getBoolean(1);

                switch (group) {
                    case "building":
                        mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, enable);
                        break;
                    case "transit":
                        mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, enable);
                        break;
                    case "bike":
                        mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, enable);
                        break;
                    case "ctt":
                        mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, enable);
                        break;
                    case "landparcel":
                        mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, enable);
                        break;
                    case "mountain":
                        mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, enable);
                        break;
                }
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
                .put("animateToCoordinates", ANIMATE_TO_COORDINATES)
                .put("setLayerGroupEnabled", SET_LAYER_GROUP_ENABLED)
                .build();
    }

    @Override
    public java.util.Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        final MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        for (String eventName : RNNaverMapViewProps.EVENT_NAMES)
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
    public void addView(RNNaverMapViewContainer parent, View child, int index) {
        parent.addFeature(child, index);
    }

    @Override
    public int getChildCount(RNNaverMapViewContainer view) {
        return view.getFeatureCount();
    }

    @Override
    public View getChildAt(RNNaverMapViewContainer view, int index) {
        return view.getFeatureAt(index);
    }

    @Override
    public void removeViewAt(RNNaverMapViewContainer parent, int index) {
        parent.removeFeatureAt(index);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    private java.util.Map<String, Object> bubbled(String callbackName) {
        return MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", callbackName));
    }
}
