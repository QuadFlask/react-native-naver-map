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
}

