package com.github.quadflask.react.navermap.feature

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.events.RCTEventEmitter

abstract class EventEmittableViewGroupManager<T : ViewGroup?>(reactContext: ReactApplicationContext) : ViewGroupManager<T>(), EventEmittable {
  protected val metrics: DisplayMetrics by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      val metrics = DisplayMetrics()
      (reactContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        .defaultDisplay
        .getRealMetrics(metrics)
      metrics
    } else {
      reactContext.resources.displayMetrics
    }
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any>? {
    return mutableMapOf<String, Any>().apply {
      for (eventName in eventNames)
        put(eventName, bubbled(eventName))
    }
  }

  open val eventNames = emptyArray<String>()

  private fun bubbled(callbackName: String?): Map<String, Any> {
    return mapOf("phasedRegistrationNames" to mapOf("bubbled" to callbackName))
  }

  override fun emitEvent(context: ReactContext?, id: Int, eventName: String?, param: WritableMap?): Boolean {
    context?.getJSModule(RCTEventEmitter::class.java)?.receiveEvent(id, eventName, param)
    return false
  }
}
