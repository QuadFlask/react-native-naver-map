package com.github.quadflask.react.navermap

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.quadflask.react.navermap.ReactUtil.parseAlign
import com.github.quadflask.react.navermap.ReactUtil.parseColorString
import com.github.quadflask.react.navermap.ReactUtil.toNaverLatLng
import com.naver.maps.map.overlay.Marker
import kotlin.math.roundToInt

open class RNNaverMapMarkerManager(reactContext: ReactApplicationContext) : EventEmittableViewGroupManager<RNNaverMapMarker?>(reactContext) {
  override val eventNames = arrayOf("onClick")

  override fun getName() = "RNNaverMapMarker"

  override fun createViewInstance(reactContext: ThemedReactContext): RNNaverMapMarker {
    return RNNaverMapMarker(this, reactContext)
  }

  @ReactProp(name = "coordinate")
  fun setCoordinate(view: RNNaverMapMarker, map: ReadableMap?) {
    view.setCoordinate(toNaverLatLng(map))
  }

  @ReactProp(name = "anchor")
  fun setAnchor(view: RNNaverMapMarker, map: ReadableMap?) {
    view.setAnchor(ReactUtil.toPointF(map, Marker.DEFAULT_ANCHOR))
  }

  @ReactProp(name = "image")
  fun setImage(view: RNNaverMapMarker, source: String?) {
    view.setImage(source)
  }

  @ReactProp(name = "pinColor", defaultInt = Color.RED, customType = "Color")
  fun setPinColor(view: RNNaverMapMarker, pinColor: Int) {
    view.setIconTintColor(pinColor)
  }

  @ReactProp(name = "rotation", defaultFloat = 0.0f)
  fun setMarkerRotation(view: RNNaverMapMarker, rotation: Float) {
    view.rotation = rotation
  }

  @ReactProp(name = "flat", defaultBoolean = false)
  fun setFlat(view: RNNaverMapMarker, flat: Boolean) {
    view.setFlat(flat)
  }

  @ReactProp(name = "width", defaultFloat = 64f)
  fun setWidth(view: RNNaverMapMarker, width: Float) {
    val widthInScreenPx: Int = (metrics.density * width).roundToInt()
    view.width = widthInScreenPx
  }

  @ReactProp(name = "height", defaultFloat = 64f)
  fun setHeight(view: RNNaverMapMarker, height: Float) {
    val heightInScreenPx: Int = (metrics.density * height).roundToInt()
    view.height = heightInScreenPx
  }

  @ReactProp(name = "animated", defaultBoolean = false)
  fun setAnimated(view: RNNaverMapMarker, animated: Boolean) {
    view.setAnimated(animated)
  }

  @ReactProp(name = "easing", defaultInt = -1)
  fun setEasing(view: RNNaverMapMarker, easingFunction: Int) {
    view.setEasing(easingFunction)
  }

  @ReactProp(name = "duration", defaultInt = 500)
  fun setDuration(view: RNNaverMapMarker, duration: Int) {
    view.setDuration(duration)
  }

  @ReactProp(name = "alpha", defaultFloat = 1f)
  fun setAlpha(view: RNNaverMapMarker, alpha: Float) {
    view.alpha = alpha
  }

  @ReactProp(name = "zIndex", defaultInt = 0)
  fun setZIndex(view: RNNaverMapMarker, zIndex: Int) {
    view.setZIndex(zIndex)
  }

  @ReactProp(name = "caption")
  fun setCaption(view: RNNaverMapMarker, map: ReadableMap?) {
    if (map == null || !map.hasKey("text")) {
      view.removeCaption()
      return
    }
    val text = map.getString("text") ?: ""
    val textSize = if (map.hasKey("textSize")) map.getInt("textSize") else 16
    val color = if (map.hasKey("color")) parseColorString(map.getString("color")) else Color.BLACK
    val haloColor = if (map.hasKey("haloColor")) parseColorString(map.getString("haloColor")) else Color.WHITE
    val align = if (map.hasKey("align")) parseAlign(map.getInt("align")) else Marker.DEFAULT_CAPTION_ALIGNS[0]
    view.setCaption(text, textSize, color, haloColor, align)
  }

  // react component render listener
  @ReactProp(name = "tick", defaultInt = 0)
  fun setTick(view: RNNaverMapMarker, tick: Int) {
    Log.e("RNN", "onTick $tick")
    view.updateCustomView()
    Handler(Looper.getMainLooper()).postDelayed({ view.updateCustomView() }, 500) // temporary
  }

  override fun addView(parent: RNNaverMapMarker?, child: View?, index: Int) {
    parent?.addView(child!!, index)
  }

  override fun removeView(parent: RNNaverMapMarker?, view: View?) {
    parent?.removeView(view!!)
  }

/*
  override fun createShadowNodeInstance(): LayoutShadowNode {
    // A custom shadow node is needed in order to pass back the width/height of the map to the
    // view manager so that it can start applying camera moves with bounds.
    return SizeReportingShadowNode()
  }

  // TODO test SizeReportingShadowNode
  override fun updateExtraData(view: RNNaverMapMarker, extraData: Any) {
    // This method is called from the shadow node with the width/height of the rendered
    // marker view.
    val data = extraData as Map<String, Float>
    val width = data["width"]!!
    val height = data["height"]!!
    Log.e("RNN", "RNNaverMapMarkerManager.updateExtraData(view: ${view.id}, data: ${width.toInt()}x${height.toInt()})")
    //view.update(width.toInt(), height.toInt())
  }
*/
}
