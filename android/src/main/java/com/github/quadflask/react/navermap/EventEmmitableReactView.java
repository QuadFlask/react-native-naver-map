package com.github.quadflask.react.navermap;

import android.content.Context;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.views.view.ReactViewGroup;

abstract class EventEmmitableReactView extends ReactViewGroup {
    protected EventEmittable emitter;

    public EventEmmitableReactView(EventEmittable emitter, Context context) {
        super(context);
        this.emitter = emitter;
    }

    protected void emitEvent(String eventName, WritableMap param) {
        emitter.emitEvent(getId(), eventName, param);
    }
}