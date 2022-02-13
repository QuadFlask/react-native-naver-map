package com.github.quadflask.react.navermap.feature

import android.content.Context
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.CircleOverlay

class RNNaverMapCircleOverlay(emitter: EventEmittable, context: Context) : ClickableRNNaverMapFeature<CircleOverlay>(emitter, context) {
  init {
    feature = CircleOverlay()
  }

  fun setCenter(center: LatLng) {
    feature.center = center
  }

  fun setRadius(radius: Double) {
    feature.radius = radius
  }

  fun setColor(color: Int) {
    feature.color = color
  }

  fun setOutlineWidth(width: Int) {
    feature.outlineWidth = width
  }

  fun setOutlineColor(color: Int) {
    feature.outlineColor = color
  }

  fun setZIndex(zIndex: Int) {
    feature.zIndex = zIndex
  }
}
