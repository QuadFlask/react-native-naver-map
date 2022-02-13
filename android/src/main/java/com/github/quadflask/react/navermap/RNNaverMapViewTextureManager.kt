package com.github.quadflask.react.navermap

import com.facebook.react.bridge.ReactApplicationContext
import com.naver.maps.map.NaverMapOptions

class RNNaverMapViewTextureManager(context: ReactApplicationContext) : RNNaverMapViewManager(context) {
  override fun getName() = "RNNaverMapViewTexture"

  override val naverMapViewOptions: NaverMapOptions
    get() = NaverMapOptions()
      .useTextureView(true)
      .translucentTextureSurface(true)
}
