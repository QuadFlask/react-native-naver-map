package com.github.quadflask.react.navermap

import android.content.Context
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Overlay

open class ClickableRNNaverMapFeature<T : Overlay>(
  emitter: EventEmittable,
  context: Context
) : RNNaverMapFeature<T>(emitter, context), Overlay.OnClickListener {
  override fun addToMap(map: NaverMap) {
    super.addToMap(map)
    feature.onClickListener = this
  }

  override fun onClick(overlay: Overlay): Boolean {
    emitEvent("onClick", null)
    return true
  }
}
