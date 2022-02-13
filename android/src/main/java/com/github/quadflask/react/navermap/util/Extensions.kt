package com.github.quadflask.react.navermap.util

import android.graphics.Color
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import com.facebook.react.bridge.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.overlay.Align
import kotlin.math.roundToInt

fun String?.hex2Color(): Int {
  var hex = this
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

fun Int.toAlign(): Align? {
  return if (this < 0 || this >= Align.values().size) null else Align.values()[this]
}

fun ReadableMap?.toPointF(default: PointF = PointF(.5f, 1f)): PointF {
  if (this == null) return default
  val x = getDoubleOrNull("x")?.toFloat() ?: default.x
  val y = getDoubleOrNull("y")?.toFloat() ?: default.y
  return PointF(x, y)
}

fun ReadableMap?.toNaverLatLng(): LatLng {
  if (this == null) return LatLng.INVALID
  return LatLng(
    getDouble("latitude"),
    getDouble("longitude")
  )
}

fun ReadableArray?.toLatLngList(): List<LatLng> {
  return this?.let {
    mutableListOf<LatLng>().apply {
      for (i in 0 until size())
        add(getMap(i).toNaverLatLng())
    }
  } ?: emptyList()
}

fun ReadableArray.toLatLngBounds(): LatLngBounds {
  return LatLngBounds.Builder().apply {
    for (i in 0 until size())
      include(getMap(i).toNaverLatLng())
  }.build()
}

fun Array<LatLng>.toLatLngBounds(): LatLngBounds {
  return LatLngBounds.Builder().also {
    for (i in 0 until size)
      this[i].run { it.include(this@toLatLngBounds[i]) }
  }.build()
}

fun Array<LatLng>.toWritableArray(): WritableArray {
  return WritableNativeArray().also {
    for (latLng in this)
      it.pushMap(latLng.toWritableMap())
  }
}

fun LatLng.toWritableMap(): WritableMap {
  return WritableNativeMap().apply {
    putDouble("latitude", latitude)
    putDouble("longitude", longitude)
  }
}

fun ReadableMap.getDoubleOrNull(key: String): Double? {
  return if (this.hasKey(key)) this.getDouble(key) else null
}

fun ReadableMap.getInt(key: String, defaultValue: Int = 0): Int {
  return if (hasKey(key)) getInt(key) else defaultValue
}

fun ReadableMap?.toRectF(): RectF {
  return this?.let {
    RectF(
      getDoubleOrNull("left")?.toFloat() ?: 0f,
      getDoubleOrNull("top")?.toFloat() ?: 0f,
      getDoubleOrNull("right")?.toFloat() ?: 0f,
      getDoubleOrNull("bottom")?.toFloat() ?: 0f
    )
  } ?: RectF()
}

fun RectF.scale(scale: Float) {
  left *= scale
  top *= scale
  right *= scale
  bottom *= scale
}

fun RectF.toRect(): Rect {
  return Rect(
    left.roundToInt(),
    top.roundToInt(),
    right.roundToInt(),
    bottom.roundToInt()
  )
}
