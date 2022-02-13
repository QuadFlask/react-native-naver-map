package com.github.quadflask.react.navermap

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ThemedReactContext
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.overlay.Align
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object ReactUtil {
  fun parseColorString(hex: String?): Int {
    var hex = hex
    if (hex == null) return Color.BLACK
    if (hex.startsWith("#")) {
      hex = hex.substring(1)
    } else if (hex.startsWith("0x")) {
      hex = hex.substring(2)
    }
    if (hex.length == 6) { // rgb
      val rgb = Integer.valueOf(hex, 16)
      return -0x1000000 or rgb
    } else if (hex.length == 10) { // argb
      val a = Integer.valueOf(hex.substring(0, 2), 16)
      val rgb = Integer.valueOf(hex.substring(2), 16)
      return a shl 24 or rgb
    }
    return Color.BLACK
  }

  fun parseAlign(align: Int, default: Align = Align.Bottom): Align {
    return if (align < 0 || align >= Align.values().size) default else Align.values()[align]
  }

  fun toPointF(map: ReadableMap?, default: PointF = PointF(.5f, 1f)): PointF {
    if (map == null) return default
    val x = if (map.hasKey("x")) map.getDouble("x").toFloat() else default.x
    val y = if (map.hasKey("y")) map.getDouble("y").toFloat() else default.y
    return PointF(x, y)
  }

  fun toNaverLatLng(map: ReadableMap?): LatLng {
    return map?.let {
      val latitude = it.getDouble("latitude")
      val longitude = it.getDouble("longitude")
      return LatLng(latitude, longitude)
    } ?: LatLng.INVALID
  }

  fun toLatLngList(array: ReadableArray?): List<LatLng> {
    return array?.let {
      mutableListOf<LatLng>().apply {
        for (i in 0 until array.size())
          add(toNaverLatLng(array.getMap(i)))
      }
    } ?: emptyList()
  }

  fun toLatLngBounds(a: ReadableMap?, b: ReadableMap?): LatLngBounds {
    val a = toNaverLatLng(a)
    val b = toNaverLatLng(b)

    return LatLngBounds(
      LatLng(min(a.latitude, b.latitude), min(a.longitude, b.longitude)),
      LatLng(max(a.latitude, b.latitude), max(a.longitude, b.longitude))
    )
  }

  fun toWritableMap(latLng: LatLng): WritableMap {
    return WritableNativeMap().apply {
      putDouble("latitude", latLng.latitude)
      putDouble("longitude", latLng.longitude)
    }
  }

  fun toWritableArray(latLngs: Array<LatLng>): WritableArray {
    return WritableNativeArray().apply {
      for (latLng in latLngs)
        pushMap(toWritableMap(latLng))
    }
  }

  fun getInt(option: ReadableMap?, key: String, defaultValue: Int = 0): Int {
    return if (option?.hasKey(key) == true) option.getInt(key) else defaultValue
  }

  fun getDoubleOrNull(option: ReadableMap, key: String): Double? {
    return if (option.hasKey(key)) option.getDouble(key) else null
  }

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
