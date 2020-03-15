package com.github.quadflask.react.navermap;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;

public abstract class EventEmittableViewGroupManager<T extends ViewGroup> extends ViewGroupManager<T>
        implements EventEmittable {
    private final ReactApplicationContext appContext;
    private final DisplayMetrics metrics;

    public EventEmittableViewGroupManager(ReactApplicationContext reactContext) {
        super();
        this.appContext = reactContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay()
                    .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }

    @Override
    public java.util.Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        final MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        for (String eventName : getEventNames())
            builder.put(eventName, bubbled(eventName));
        return builder.build();
    }

    String[] getEventNames() {
        return new String[0];
    }

    private java.util.Map<String, Object> bubbled(String callbackName) {
        return MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", callbackName));
    }

    @Override
    public boolean emitEvent(int id, String eventName, WritableMap param) {
        appContext.getJSModule(RCTEventEmitter.class).receiveEvent(id, eventName, param);
        return false;
    }
}
