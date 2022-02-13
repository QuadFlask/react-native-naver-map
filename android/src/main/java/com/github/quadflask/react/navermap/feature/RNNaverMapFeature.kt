package com.github.quadflask.react.navermap.feature

import android.content.Context
import android.util.Log
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Overlay

abstract class RNNaverMapFeature<T : Overlay>(
  emitter: EventEmittable,
  context: Context
) : EventEmittableReactView(emitter, context) {
  lateinit var feature: T
    protected set

  open fun addToMap(map: NaverMap) {
    feature.map = map
    Log.e("RNN", "RNNaverMapFeature.addToMap")
  }

  fun removeFromMap() {
    feature.map = null
    Log.e("RNN", "RNNaverMapFeature.removeFromMap")
  }
}
