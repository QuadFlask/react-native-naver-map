package com.github.quadflask.react.navermap

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.github.quadflask.react.navermap.feature.*

class RNNaverMapPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    return listOf(RNNaverMapJavaModule(reactContext))
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return listOf(
      RNNaverMapViewManager(reactContext),
      RNNaverMapViewTextureManager(reactContext),
      RNNaverMapPolylineOverlayManager(reactContext),
      RNNaverMapPathOverlayManager(reactContext),
      RNNaverMapMarkerManager(reactContext),
      RNNaverMapCircleOverlayManager(reactContext),
      RNNaverMapPolygonOverlayManager(reactContext),
      RNNaverMapInfoWindowManager(reactContext)
    )
  }
}
