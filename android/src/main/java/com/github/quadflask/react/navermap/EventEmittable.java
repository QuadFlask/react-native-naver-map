package com.github.quadflask.react.navermap;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;

public interface EventEmittable {
    boolean emitEvent(ReactContext context, int id, String eventName, WritableMap param);
}
