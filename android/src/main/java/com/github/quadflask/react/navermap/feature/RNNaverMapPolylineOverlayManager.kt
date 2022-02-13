package com.github.quadflask.react.navermap.feature

import android.graphics.Color
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.quadflask.react.navermap.util.toLatLngList
import com.naver.maps.map.overlay.PolylineOverlay

class RNNaverMapPolylineOverlayManager(reactContext: ReactApplicationContext) : EventEmittableViewGroupManager<RNNaverMapPolylineOverlay?>(reactContext) {
  override val eventNames = arrayOf("onClick")

  override fun getName() = "RNNaverMapPolylineOverlay"

  override fun createViewInstance(reactContext: ThemedReactContext): RNNaverMapPolylineOverlay {
    return RNNaverMapPolylineOverlay(this, reactContext)
  }

  @ReactProp(name = "coordinates")
  fun setCoordinate(view: RNNaverMapPolylineOverlay, coordinates: ReadableArray?) {
    view.setCoords(coordinates.toLatLngList())
  }

  @ReactProp(name = "strokeWidth", defaultFloat = 1f)
  fun setStrokeWidth(view: RNNaverMapPolylineOverlay, widthInPoints: Float) {
    view.setLineWidth(widthInPoints * metrics.density)
  }

  @ReactProp(name = "strokeColor", defaultInt = Color.RED, customType = "Color")
  fun setStrokeColor(view: RNNaverMapPolylineOverlay, color: Int) {
    view.setLineColor(color)
  }

  @ReactProp(name = "capType", defaultInt = 0)
  fun setCapType(view: RNNaverMapPolylineOverlay, capType: Int) {
    view.setCapType(PolylineOverlay.LineCap.values()[capType])
  }

  @ReactProp(name = "joinType", defaultInt = 0)
  fun setJoinType(view: RNNaverMapPolylineOverlay, joinType: Int) {
    view.setJoinType(PolylineOverlay.LineJoin.values()[joinType])
  }

  @ReactProp(name = "pattern")
  fun setPattern(view: RNNaverMapPolylineOverlay, pattern: ReadableArray) {
    val patternArray = IntArray(pattern.size())
    for (i in patternArray.indices) patternArray[i] = pattern.getInt(i)
    view.setPattern(patternArray)
  }
}
