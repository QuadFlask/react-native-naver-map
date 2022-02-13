package com.github.quadflask.react.navermap.feature

import android.content.Context
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.PolylineOverlay
import com.naver.maps.map.overlay.PolylineOverlay.LineCap
import com.naver.maps.map.overlay.PolylineOverlay.LineJoin
import kotlin.math.roundToInt

class RNNaverMapPolylineOverlay(
  emitter: EventEmittable,
  context: Context
) : ClickableRNNaverMapFeature<PolylineOverlay>(emitter, context) {
  init {
    feature = PolylineOverlay()
  }

  fun setCoords(coords: List<LatLng>) {
    feature.coords = coords
  }

  fun setLineWidth(widthInScreenPx: Float) {
    feature.width = widthInScreenPx.roundToInt()
  }

  fun setLineColor(color: Int) {
    feature.color = color
  }

  fun setCapType(value: LineCap?) {
    feature.setCapType(value)
  }

  fun setJoinType(value: LineJoin?) {
    feature.setJoinType(value)
  }

  fun setPattern(pattern: IntArray) {
    feature.setPattern(*pattern)
  }
}
