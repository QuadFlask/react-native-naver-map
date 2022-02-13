package com.github.quadflask.react.navermap

import android.content.Context
import android.graphics.PointF
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker

class RNNaverMapInfoWindow(
  emitter: EventEmittable,
  context: Context
) : ClickableRNNaverMapFeature<InfoWindow>(emitter, context) {
  private var align: Align = Align.Top
  private var open: Boolean = false
  private var marker: Marker? = null

  init {
    feature = InfoWindow()
  }

  fun setText(text: String?) {
    if (text == null) {
      setOpen(false)
    } else {
      feature.adapter = object : InfoWindow.DefaultTextAdapter(context) {
        override fun getText(infoWindow: InfoWindow): CharSequence {
          return text
        }
      }
    }
  }

  fun setCoordinate(latLng: LatLng) {
    feature.position = latLng
    refresh()
  }

  fun setAlign(align: Align) {
    this.align = align
    refresh()
  }

  fun setAnchor(anchor: PointF) {
    feature.anchor = anchor
  }

  fun setOffsetX(v: Int) {
    feature.offsetX = v
  }

  fun setOffsetY(v: Int) {
    feature.offsetY = v
  }

  override fun setAlpha(alpha: Float) {
    feature.alpha = alpha
  }

  fun setMarker(marker: Marker?) {
    Log.e("RNN", "RNNaverMapInfoWindow.setMarker: " + marker)
    this.marker = marker
    refresh()
  }

  fun setOpen(open: Boolean) {
    Log.e("RNN", "RNNaverMapInfoWindow.setOpen: " + open)
    this.open = open
    if (open) {
      if (marker != null) {
        feature.open(marker!!, align)
      } else {
        feature.map?.let { feature.open(it) }
      }
    } else {
      feature.close()
    }
  }

  fun refresh() {
    setOpen(open)
  }
}
