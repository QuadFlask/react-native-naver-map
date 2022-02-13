package com.github.quadflask.react.navermap

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.airbnb.android.react.maps2.ViewAttacherGroup
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.facebook.react.uimanager.ThemedReactContext
import com.github.quadflask.react.navermap.feature.RNNaverMapFeature
import com.github.quadflask.react.navermap.util.ReactUtil
import com.github.quadflask.react.navermap.util.toWritableArray
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.NaverMap.*
import com.naver.maps.map.util.FusedLocationSource

class RNNaverMapView(
  private var themedReactContext: ThemedReactContext?,
  appContext: ReactApplicationContext?,
  private var locationSource: FusedLocationSource?,
  naverMapOptions: NaverMapOptions?
) : MapView(ReactUtil.getNonBuggyContext(themedReactContext, appContext!!)!!, naverMapOptions), OnMapReadyCallback, OnCameraIdleListener, OnMapClickListener, RNNaverMapViewProps {
  private var attacherGroup: ViewAttacherGroup?
  private var lastTouch: Long = 0
  private val features: MutableList<RNNaverMapFeature<*>> = mutableListOf()

  init {
    // Set up a parent view for triggering visibility in subviews that depend on it.
    // Mainly ReactImageView depends on Fresco which depends on onVisibilityChanged() event
    attacherGroup = ViewAttacherGroup(themedReactContext).apply {
      layoutParams = LayoutParams(300, 300).apply {
        width = 300
        height = 300
        leftMargin = 99999999
        topMargin = 99999999
      }
    }
    addView(attacherGroup)
    Log.e("RNN", "RNNaverMapView.init")
  }

  override fun onCreate(bundle: Bundle?) {
    super.onCreate(bundle)
    getMapAsync(this)
  }

  override fun onMapReady(map: NaverMap) {
    map.locationSource = locationSource
    map.onMapClickListener = this
    map.addOnCameraIdleListener(this)
    map.addOnCameraChangeListener { reason: Int, animated: Boolean ->
      if (reason == -1 && System.currentTimeMillis() - lastTouch > 500) { // changed by user
        emitEvent("onTouch", Arguments.createMap().apply {
          putInt("reason", reason)
          putBoolean("animated", animated)
        })
        lastTouch = System.currentTimeMillis()
      }
    }
    map.addOnOptionChangeListener {
      emitEvent("onOptionChange", Arguments.createMap().apply {
        putInt("locationTrackingMode", LocationTrackingMode.values().indexOf(map.locationTrackingMode))
      })
    }
    // instanceStateBundle 로 피쳐들이 복구가 안됨. 임시로 map 다시 세팅
    features.forEach {
      if (it.feature.isAdded) {
        it.removeFromMap()
        it.addToMap(map)
      }
    }

    onInitialized()
    Log.e("RNN", "RNNaverMapView.onMapReady")
  }

  override fun setCenter(latLng: LatLng?) {
    getMapAsync { map ->
      latLng?.let {
        val cameraUpdate = CameraUpdate.scrollTo(it).animate(CameraAnimation.Easing)
        map.moveCamera(cameraUpdate)
      }
    }
  }

  override fun setCenter(latLng: LatLng?, zoom: Double?, tilt: Double?, bearing: Double?) {
    getMapAsync { map ->
      val cam = map.cameraPosition
      val target = latLng ?: cam.target
      val zoomValue = zoom ?: cam.zoom
      val tiltValue = tilt ?: cam.tilt
      val bearingValue = bearing ?: cam.bearing
      map.moveCamera(
        CameraUpdate.toCameraPosition(CameraPosition(target, zoomValue, tiltValue, bearingValue))
          .animate(CameraAnimation.Easing)
      )
    }
  }

  override fun zoomTo(latLngBounds: LatLngBounds, paddingInPx: Int) {
    getMapAsync { map ->
      val cameraUpdate = CameraUpdate.fitBounds(latLngBounds, paddingInPx)
        .animate(CameraAnimation.Easing)
      map.moveCamera(cameraUpdate)
    }
  }

  override fun setTilt(tilt: Int) {
    getMapAsync { map ->
      val cameraPosition = map.cameraPosition
      map.moveCamera(
        CameraUpdate.toCameraPosition(
          CameraPosition(cameraPosition.target, cameraPosition.zoom, tilt.toDouble(), cameraPosition.bearing)
        )
      )
    }
  }

  override fun setBearing(bearing: Int) {
    getMapAsync { map ->
      val cameraPosition = map.cameraPosition
      map.moveCamera(
        CameraUpdate.toCameraPosition(
          CameraPosition(cameraPosition.target, cameraPosition.zoom, cameraPosition.tilt, bearing.toDouble())
        )
      )
    }
  }

  override fun setZoom(zoom: Float) {
    getMapAsync { map -> map.moveCamera(CameraUpdate.zoomTo(zoom.toDouble())) }
  }

  override fun setMapPadding(left: Int, top: Int, right: Int, bottom: Int) {
    getMapAsync { map -> map.setContentPadding(left, top, right, bottom) }
  }

  override fun onInitialized() {
    emitEvent("onInitialized", null)
  }

  override fun showsMyLocationButton(show: Boolean) {
    getMapAsync { map -> map.uiSettings.isLocationButtonEnabled = show }
  }

  override fun setCompassEnabled(show: Boolean) {
    getMapAsync { map -> map.uiSettings.isCompassEnabled = show }
  }

  override fun setScaleBarEnabled(show: Boolean) {
    getMapAsync { map -> map.uiSettings.isScaleBarEnabled = show }
  }

  override fun setZoomControlEnabled(show: Boolean) {
    getMapAsync { map -> map.uiSettings.isZoomControlEnabled = show }
  }

  override fun setLocationTrackingMode(mode: Int) {
    getMapAsync { map -> map.locationTrackingMode = LocationTrackingMode.values()[mode] }
  }

  override fun setMapType(value: MapType?) {
    getMapAsync { map -> map.mapType = value!! }
  }

  override fun setMinZoom(minZoomLevel: Float) {
    getMapAsync { map -> map.minZoom = minZoomLevel.toDouble() }
  }

  override fun setMaxZoom(maxZoomLevel: Float) {
    getMapAsync { map -> map.maxZoom = maxZoomLevel.toDouble() }
  }

  override fun setExtent(bounds: LatLngBounds?) {
    getMapAsync { map -> map.extent = bounds }
  }

  override fun setBuildingHeight(height: Float) {
    getMapAsync { map -> map.buildingHeight = height }
  }

  override fun setLayerGroupEnabled(layerGroup: String, enable: Boolean) {
    getMapAsync { map -> map.setLayerGroupEnabled(layerGroup, enable) }
  }

  override fun setNightModeEnabled(enable: Boolean) {
    getMapAsync { map -> map.isNightModeEnabled = enable }
  }

  override fun setLogoMargin(left: Int, top: Int, right: Int, bottom: Int) {
    getMapAsync { map -> map.uiSettings.setLogoMargin(left, top, right, bottom) }
  }

  override fun setLogoGravity(gravity: Int) {
    getMapAsync { map -> map.uiSettings.logoGravity = gravity }
  }

  override fun setScrollGesturesEnabled(enabled: Boolean) {
    getMapAsync { map -> map.uiSettings.isScrollGesturesEnabled = enabled }
  }

  override fun setZoomGesturesEnabled(enabled: Boolean) {
    getMapAsync { map -> map.uiSettings.isZoomGesturesEnabled = enabled }
  }

  override fun setTiltGesturesEnabled(enabled: Boolean) {
    getMapAsync { map -> map.uiSettings.isTiltGesturesEnabled = enabled }
  }

  override fun setRotateGesturesEnabled(enabled: Boolean) {
    getMapAsync { map -> map.uiSettings.isRotateGesturesEnabled = enabled }
  }

  override fun setStopGesturesEnabled(enabled: Boolean) {
    getMapAsync { map -> map.uiSettings.isStopGesturesEnabled = enabled }
  }

  override fun setLiteModeEnabled(enabled: Boolean) {
    getMapAsync { map -> map.isLiteModeEnabled = enabled }
  }

  override fun moveCameraFitBound(bounds: LatLngBounds, left: Int, top: Int, right: Int, bottom: Int) {
    getMapAsync { map -> map.moveCamera(CameraUpdate.fitBounds(bounds, left, top, right, bottom).animate(CameraAnimation.Fly, 500)) }
  }

  override fun addFeature(child: View?, index: Int) {
    Log.e("RNN", "RNNaverMapView.addFeature(child: ${child?.javaClass?.simpleName}, index: $index)")
    getMapAsync { map ->
      if (child is RNNaverMapFeature<*>) {
        val annotation: RNNaverMapFeature<*> = child
        annotation.addToMap(map)
        features.add(index, annotation)
        val visibility = annotation.visibility
        annotation.visibility = INVISIBLE
        val annotationParent = annotation.parent as ViewGroup?
        annotationParent?.removeView(annotation)
        // Add to the parent group
        attacherGroup!!.addView(annotation)
        annotation.visibility = visibility
        Log.e("RNN", "RNNaverMapView.addFeature(child: ${child.javaClass.simpleName}, index: $index) - DONE ($featureCount)")
      } else {
        Log.e("RNN", "addFeature: not feature: $child")
      }
    }
  }

  override fun removeFeatureAt(index: Int) {
    val feature: RNNaverMapFeature<*> = features.removeAt(index)
    feature.removeFromMap()
    Log.e("RNN", "RNNaverMapView.removeFeatureAt(index: $index)")
  }

  override val featureCount: Int
    get() = features.size

  override fun getFeatureAt(index: Int): View {
    return features[index]
  }

  override fun onCameraIdle() {
    getMapAsync { map ->
      emitEvent("onCameraChange", Arguments.createMap().apply {
        val cameraPosition = map.cameraPosition
        putDouble("latitude", cameraPosition.target.latitude)
        putDouble("longitude", cameraPosition.target.longitude)
        putDouble("zoom", cameraPosition.zoom)
        putArray("contentRegion", map.contentRegion.toWritableArray())
        putArray("coveringRegion", map.coveringRegion.toWritableArray())
      })
    }
  }

  override fun onMapClick(pointF: PointF, latLng: LatLng) {
    emitEvent("onMapClick", Arguments.createMap().apply {
      putDouble("x", pointF.x.toDouble())
      putDouble("y", pointF.y.toDouble())
      putDouble("latitude", latLng.latitude)
      putDouble("longitude", latLng.longitude)
    })
  }

  override fun onDestroy() {
    Log.e("RNN", "RNNaverMapView.onDestroy")
    removeAllViews()
    themedReactContext = null
    locationSource = null
    attacherGroup = null
    for (feature in features) feature.removeFromMap()
    features.clear()
    super.onDestroy()
  }

  private fun emitEvent(eventName: String, param: WritableMap?) {
    themedReactContext
      ?.getJSModule(RCTDeviceEventEmitter::class.java)
      ?.emit(eventName, param)
  }
}
