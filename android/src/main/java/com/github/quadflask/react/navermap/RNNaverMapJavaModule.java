package com.github.quadflask.react.navermap;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class RNNaverMapJavaModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "RNNaverMapView";
    private static ReactApplicationContext reactContext = null;

    public RNNaverMapJavaModule(ReactApplicationContext context) {
        super(context);

        reactContext = context;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    private static void emitDeviceEvent(String eventName, @Nullable WritableMap eventData) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, eventData);
    }
}

