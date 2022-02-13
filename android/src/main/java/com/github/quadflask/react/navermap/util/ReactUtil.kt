package com.github.quadflask.react.navermap.util

import android.content.Context
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ThemedReactContext
import com.naver.maps.geometry.LatLng

object ReactUtil {
  fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
    val lat = (b.latitude - a.latitude) * fraction + a.latitude
    val lng = (b.longitude - a.longitude) * fraction + a.longitude
    return LatLng(lat, lng)
  }

  private fun contextHasBug(context: Context?): Boolean {
    return context == null || context.resources == null || context.resources.configuration == null
  }

  fun getNonBuggyContext(reactContext: ThemedReactContext?, appContext: ReactApplicationContext): Context? {
    var superContext: Context? = reactContext
    if (!contextHasBug(appContext.currentActivity)) {
      superContext = appContext.currentActivity
    } else if (contextHasBug(superContext)) {
      // we have the bug! let's try to find a better context to use
      if (!contextHasBug(reactContext?.currentActivity)) {
        superContext = reactContext?.currentActivity
      } else if (!contextHasBug(reactContext?.applicationContext)) {
        superContext = reactContext?.applicationContext
      } else {
        // ¯\_(ツ)_/¯
      }
    }
    return superContext
  }
}
