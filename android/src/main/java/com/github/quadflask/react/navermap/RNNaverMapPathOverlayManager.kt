package com.github.quadflask.react.navermap

import android.graphics.Color
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.quadflask.react.navermap.ReactUtil.toLatLngList
import kotlin.math.roundToInt

class RNNaverMapPathOverlayManager(reactContext: ReactApplicationContext) : EventEmittableViewGroupManager<RNNaverMapPathOverlay>(reactContext) {
  override val eventNames = arrayOf("onClick")

  override fun getName() = "RNNaverMapPathOverlay"

  override fun createViewInstance(reactContext: ThemedReactContext): RNNaverMapPathOverlay {
    return RNNaverMapPathOverlay(this, reactContext)
  }

  @ReactProp(name = "coordinates")
  fun setCoordinate(view: RNNaverMapPathOverlay, coordinates: ReadableArray?) {
    view.setCoords(toLatLngList(coordinates))
  }

  @ReactProp(name = "width", defaultFloat = 1f)
  fun setStrokeWidth(view: RNNaverMapPathOverlay, widthInPoints: Float) {
    val widthInScreenPx = metrics.density * widthInPoints
    view.setWidth(widthInScreenPx)
  }

  @ReactProp(name = "zIndex", defaultInt = 0)
  fun setProgress(view: RNNaverMapPathOverlay, zIndex: Int) {
    view.setZIndex(zIndex)
  }

  @ReactProp(name = "color", defaultInt = Color.RED, customType = "Color")
  fun setColor(view: RNNaverMapPathOverlay, color: Int) {
    view.setColor(color)
  }

  @ReactProp(name = "outlineWidth", defaultFloat = 1f)
  fun setOutlineWidth(view: RNNaverMapPathOverlay, widthInPoints: Float) {
    val widthInScreenPx = metrics.density * widthInPoints
    view.setOutlineWidth(widthInScreenPx)
  }

  @ReactProp(name = "passedColor", defaultInt = Color.RED, customType = "Color")
  fun setPassedColor(view: RNNaverMapPathOverlay, color: Int) {
    view.setPassedColor(color)
  }

  @ReactProp(name = "outlineColor", defaultInt = Color.RED, customType = "Color")
  fun setOutlineColor(view: RNNaverMapPathOverlay, color: Int) {
    view.setOutlineColor(color)
  }

  @ReactProp(name = "passedOutlineColor", defaultInt = Color.RED, customType = "Color")
  fun setPassedOutlineColor(view: RNNaverMapPathOverlay, color: Int) {
    view.setPassedOutlineColor(color)
  }

  @ReactProp(name = "pattern")
  fun setPatternInterval(view: RNNaverMapPathOverlay, image: String?) {
    view.setPattern(image)
  }

  @ReactProp(name = "patternInterval", defaultFloat = 1f)
  fun setPatternInterval(view: RNNaverMapPathOverlay, widthInPoints: Float) {
    val widthInScreenPx = (metrics.density * widthInPoints).roundToInt()
    view.setPatternInterval(widthInScreenPx)
  }

  @ReactProp(name = "progress", defaultFloat = 0f)
  fun setProgress(view: RNNaverMapPathOverlay, progress: Float) {
    view.setProgress(progress)
  }
}
