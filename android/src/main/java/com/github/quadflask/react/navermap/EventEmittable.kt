package com.github.quadflask.react.navermap

import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap

interface EventEmittable {
  fun emitEvent(context: ReactContext?, id: Int, eventName: String?, param: WritableMap?): Boolean
}
