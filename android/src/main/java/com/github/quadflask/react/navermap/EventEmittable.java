package com.github.quadflask.react.navermap;

import com.facebook.react.bridge.WritableMap;

public interface EventEmittable {
    boolean emitEvent(int id, String eventName, WritableMap param);
}
