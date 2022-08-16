package com.github.quadflask.react.navermap;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

public class RNNaverMapJavaModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "RNNaverMapView";

    public RNNaverMapJavaModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void getPointLatLng(final int tag, ReadableMap coordinate, Promise promise) {
        final ReactApplicationContext context = getReactApplicationContext();
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        ReadableMap center = coordinate.getMap("center");
        ReadableMap screen = coordinate.getMap("screen");
        double width = screen.getDouble("width") * 4;
        double height = screen.getDouble("height") * 4;

        final LatLng centerCoord = new LatLng(
                center.hasKey("latitude") ? center.getDouble("latitude") : 0.0,
                center.hasKey("longitude") ? center.getDouble("longitude") : 0.0
        );


        uiManager.addUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                RNNaverMapViewContainer mapView = (RNNaverMapViewContainer) nativeViewHierarchyManager.resolveView(tag);
                RNNaverMapView view = mapView.getMap();
                if (view == null) {
                    promise.reject("AirMapView not found");
                    return;
                }
                if (view.getMap() == null) {
                    promise.reject("AirMapView.map is not valid");
                    return;
                }
                Projection projection = view.getMap().getProjection();
                PointF point = projection.toScreenLocation(centerCoord);

                LatLng topLeftCoord = projection.fromScreenLocation(
                        new PointF(point.x - (float)width,point.y - (float)height)
                );

                LatLng topRightCoord = projection.fromScreenLocation(
                        new PointF(point.x + (float)width,point.y - (float)height)
                );

                LatLng bottomRightCoord = projection.fromScreenLocation(
                        new PointF(point.x + (float)width,point.y + (float)height)
                );

                LatLng bottomLeftCoord = projection.fromScreenLocation(
                        new PointF(point.x - (float)width,point.y + (float)height)
                );

                WritableMap screenCoords = new WritableNativeMap();
                WritableArray topLeft = new WritableNativeArray();
                WritableArray topRight = new WritableNativeArray();
                WritableArray bottomRight = new WritableNativeArray();
                WritableArray bottomLeft = new WritableNativeArray();

                topLeft.pushDouble(topLeftCoord.latitude);
                topLeft.pushDouble(topLeftCoord.longitude);

                topRight.pushDouble(topRightCoord.latitude);
                topRight.pushDouble(topRightCoord.longitude);

                bottomRight.pushDouble(bottomRightCoord.latitude);
                bottomRight.pushDouble(bottomRightCoord.longitude);

                bottomLeft.pushDouble(bottomLeftCoord.latitude);
                bottomLeft.pushDouble(bottomLeftCoord.longitude);

                screenCoords.putArray("topLeftCoord",topLeft);
                screenCoords.putArray("topRightCoord",topRight);
                screenCoords.putArray("bottomLeftCoord",bottomLeft);
                screenCoords.putArray("bottomRightCoord",bottomRight);

                promise.resolve(screenCoords);
            }
        });
    }
}

