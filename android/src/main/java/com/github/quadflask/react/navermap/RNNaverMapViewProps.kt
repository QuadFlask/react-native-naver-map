package com.github.quadflask.react.navermap

import android.view.View
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.NaverMap.MapType

interface RNNaverMapViewProps {
  fun onStop()
  fun setCenter(latLng: LatLng?)
  fun setCenter(latLng: LatLng?, zoom: Double?, tilt: Double?, bearing: Double?)
  fun zoomTo(latLngBounds: LatLngBounds, paddingInPx: Int)
  fun setTilt(tilt: Int)
  fun setBearing(bearing: Int)
  fun setZoom(zoom: Float)
  fun setMapPadding(left: Int, top: Int, right: Int, bottom: Int)
  fun onInitialized()
  fun showsMyLocationButton(show: Boolean)
  fun setCompassEnabled(show: Boolean)
  fun setScaleBarEnabled(show: Boolean)
  fun setZoomControlEnabled(show: Boolean)
  fun setLocationTrackingMode(mode: Int)
  fun setMapType(value: MapType?)
  fun setBuildingHeight(height: Float)
  fun setMinZoom(minZoomLevel: Float)
  fun setMaxZoom(maxZoomLevel: Float)
  fun setExtent(bounds: LatLngBounds?)
  fun setLayerGroupEnabled(layerGroup: String, enable: Boolean)
  fun setNightModeEnabled(enable: Boolean)
  fun setLogoMargin(left: Int, top: Int, right: Int, bottom: Int)
  fun setLogoGravity(gravity: Int)
  fun setScrollGesturesEnabled(enabled: Boolean)
  fun setZoomGesturesEnabled(enabled: Boolean)
  fun setTiltGesturesEnabled(enabled: Boolean)
  fun setRotateGesturesEnabled(enabled: Boolean)
  fun setStopGesturesEnabled(enabled: Boolean)
  fun setLiteModeEnabled(enabled: Boolean)
  fun moveCameraFitBound(bounds: LatLngBounds, left: Int, top: Int, right: Int, bottom: Int)
  fun addFeature(child: View?, index: Int)
  fun removeFeatureAt(index: Int)
  val featureCount: Int

  fun getFeatureAt(index: Int): View?

  companion object {
    val EVENT_NAMES = arrayOf(
      "onInitialized",
      "onCameraChange",
      "onMapClick",
      "onTouch",
      "onOptionChange"
    )
  }
}
