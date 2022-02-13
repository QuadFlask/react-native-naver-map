package com.github.quadflask.react.navermap

import android.graphics.Color
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.quadflask.react.navermap.ReactUtil.toLatLngList
import com.naver.maps.geometry.LatLng

class RNNaverMapPolygonOverlayManager(reactContext: ReactApplicationContext) : EventEmittableViewGroupManager<RNNaverMapPolygonOverlay>(reactContext) {
  override val eventNames = arrayOf("onClick")

  override fun getName() = "RNNaverMapPolygonOverlay"

  override fun createViewInstance(reactContext: ThemedReactContext): RNNaverMapPolygonOverlay {
    return RNNaverMapPolygonOverlay(this, reactContext)
  }

  @ReactProp(name = "coordinates")
  fun setCoordinate(view: RNNaverMapPolygonOverlay, coordinates: ReadableArray?) {
    view.setCoords(toLatLngList(coordinates))
  }

  @ReactProp(name = "outlineWidth", defaultFloat = 1f)
  fun setOutlineWidth(view: RNNaverMapPolygonOverlay, widthInPoints: Float) {
    val widthInScreenPx = metrics.density * widthInPoints
    view.setOutlineWidth(widthInScreenPx)
  }

  @ReactProp(name = "outlineColor", defaultInt = Color.RED, customType = "Color")
  fun setOutlineColor(view: RNNaverMapPolygonOverlay, color: Int) {
    view.setOutlineColor(color)
  }

  @ReactProp(name = "color", defaultInt = Color.RED, customType = "Color")
  fun setColor(view: RNNaverMapPolygonOverlay, color: Int) {
    view.setColor(color)
  }

  @ReactProp(name = "holes")
  fun setHoles(view: RNNaverMapPolygonOverlay, holes: ReadableArray) {
    val holesArray = ArrayList<List<LatLng>>(holes.size())
    for (i in 0 until holes.size()) {
      holes.getArray(i).let {
        if (it.size() >= 3) holesArray.add(toLatLngList(it))
      }
    }
    view.setHoles(holesArray)
  }
}
