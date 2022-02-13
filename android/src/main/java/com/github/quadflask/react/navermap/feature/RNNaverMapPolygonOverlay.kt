package com.github.quadflask.react.navermap.feature

import android.content.Context
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PolygonOverlay
import kotlin.math.roundToInt

class RNNaverMapPolygonOverlay(emitter: EventEmittable, context: Context) : ClickableRNNaverMapFeature<PolygonOverlay>(emitter, context) {
  init {
    feature = PolygonOverlay()
  }

  fun setCoords(coords: List<LatLng?>) {
    feature.coords = coords
  }

  fun setColor(color: Int) {
    feature.color = color
  }

  fun setOutlineWidth(widthInScreenPx: Float) {
    feature.outlineWidth = widthInScreenPx.roundToInt()
  }

  fun setOutlineColor(color: Int) {
    feature.outlineColor = color
  }

  fun setHoles(holes: List<List<LatLng?>?>) {
    feature.holes = holes
  }
}
