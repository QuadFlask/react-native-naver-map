package com.github.quadflask.react.navermap.feature

import android.content.Context
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.views.view.ReactViewGroup

abstract class EventEmittableReactView(
  private val emitter: EventEmittable,
  context: Context
) : ReactViewGroup(context) {
  protected fun emitEvent(eventName: String, param: WritableMap?) {
    emitter.emitEvent(context as ReactContext, id, eventName, param)
  }
}
