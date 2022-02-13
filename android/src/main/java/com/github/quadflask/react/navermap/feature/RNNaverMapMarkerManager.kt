package com.github.quadflask.react.navermap.feature

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.quadflask.react.navermap.util.*
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
    view.setCoordinate(map.toNaverLatLng())
  }

  @ReactProp(name = "anchor")
  fun setAnchor(view: RNNaverMapMarker, map: ReadableMap?) {
    view.setAnchor(map.toPointF(Marker.DEFAULT_ANCHOR))
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
    view.width = (width * metrics.density).roundToInt()
  }

  @ReactProp(name = "height", defaultFloat = 64f)
  fun setHeight(view: RNNaverMapMarker, height: Float) {
    view.height = (height * metrics.density).roundToInt()
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
    if (map == null || !map.hasKey("text") || map.getString("text") == null) {
      view.removeCaption()
      return
    }
    view.setCaption(
      map.getString("text") ?: "",
      map.getInt("textSize", 16),
      map.getString("color")?.hex2Color() ?: Color.BLACK,
      map.getString("haloColor")?.hex2Color() ?: Color.WHITE,
      map.getInt("align").toAlign() ?: Marker.DEFAULT_CAPTION_ALIGNS[0]
    )
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
