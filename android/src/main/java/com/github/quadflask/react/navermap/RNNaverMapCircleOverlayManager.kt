package com.github.quadflask.react.navermap

import android.graphics.Color
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.quadflask.react.navermap.ReactUtil.toNaverLatLng

class RNNaverMapCircleOverlayManager(reactContext: ReactApplicationContext) : EventEmittableViewGroupManager<RNNaverMapCircleOverlay>(reactContext) {
  override val eventNames = arrayOf("onClick")

  override fun getName() = "RNNaverMapCircleOverlay"

  override fun createViewInstance(reactContext: ThemedReactContext): RNNaverMapCircleOverlay {
    return RNNaverMapCircleOverlay(this, reactContext)
  }

  @ReactProp(name = "coordinate")
  fun setCenter(view: RNNaverMapCircleOverlay, map: ReadableMap?) {
    view.setCenter(toNaverLatLng(map))
  }

  @ReactProp(name = "radius", defaultDouble = 100.0)
  fun setRadius(view: RNNaverMapCircleOverlay, radius: Double) {
    view.setRadius(radius)
  }

  @ReactProp(name = "color", defaultInt = Color.RED, customType = "Color")
  fun setColor(view: RNNaverMapCircleOverlay, color: Int) {
    view.setColor(color)
  }

  @ReactProp(name = "outlineWidth", defaultInt = 0)
  fun setOutlineWidth(view: RNNaverMapCircleOverlay, outlineWidth: Int) {
    view.setOutlineWidth(outlineWidth)
  }

  @ReactProp(name = "outlineColor", defaultInt = Color.BLACK, customType = "Color")
  fun setOutlineColor(view: RNNaverMapCircleOverlay, outlineColor: Int) {
    view.setOutlineColor(outlineColor)
  }

  @ReactProp(name = "zIndex", defaultInt = 0)
  fun setZIndex(view: RNNaverMapCircleOverlay, zIndex: Int) {
    view.setZIndex(zIndex)
  }
}
