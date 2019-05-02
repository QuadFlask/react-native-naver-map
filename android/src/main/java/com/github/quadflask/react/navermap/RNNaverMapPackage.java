package com.github.quadflask.react.navermap;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.List;

public class RNNaverMapPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.asList(
                new RNNaverMapJavaModule(reactContext)
        );
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.asList(
                new RNNaverMapViewManager(reactContext),
                new RNNaverMapPolylineOverlayManager(reactContext),
                new RNNaverMapPathOverlayManager(reactContext),
                new RNNaverMapMarkerManager(reactContext)
        );
    }
}

