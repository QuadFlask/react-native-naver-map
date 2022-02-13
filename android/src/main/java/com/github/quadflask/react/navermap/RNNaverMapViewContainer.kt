package com.github.quadflask.react.navermap

import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ThemedReactContext
import com.github.quadflask.react.navermap.util.ReactUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.NaverMap.MapType
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.util.FusedLocationSource

class RNNaverMapViewContainer(
  themedReactContext: ThemedReactContext,
  appContext: ReactApplicationContext?,
  locationSource: FusedLocationSource?,
  naverMapOptions: NaverMapOptions?
) : FrameLayout(ReactUtil.getNonBuggyContext(themedReactContext, appContext!!)!!), RNNaverMapViewProps {
  private var mapView: RNNaverMapView?
  private var instanceStateBundle: Bundle? = Bundle()
  private var isAttachedToWindowFlag = false

  init {
    mapView = RNNaverMapView(themedReactContext, appContext, locationSource, naverMapOptions)
    addView(mapView)
    Log.e("RNN", "RNNaverMapViewContainer.init")
  }

  // https://github.com/facebook/react-native/issues/17968#issuecomment-457236577
  private fun setupLayoutHack() {
    Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
      override fun doFrame(frameTimeNanos: Long) {
        manuallyLayoutChildren()
        viewTreeObserver.dispatchOnGlobalLayout()
        if (isAttachedToWindowFlag) Choreographer.getInstance().postFrameCallbackDelayed(this, 500)
      }
    })
  }

  private fun manuallyLayoutChildren() {
    for (i in 0 until childCount) {
      val child = getChildAt(i)
      child.measure(
        MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY)
      )
      child.layout(0, 0, child.measuredWidth, child.measuredHeight)
    }
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    mapView?.getMapAsync { map -> // TODO check async event dispatch
      if (map.uiSettings.isScrollGesturesEnabled) {
        when (ev.action) {
          MotionEvent.ACTION_DOWN,
          MotionEvent.ACTION_UP -> parent.requestDisallowInterceptTouchEvent(true)
        }
      }
    }
    return super.dispatchTouchEvent(ev)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    isAttachedToWindowFlag = true
    mapView?.onCreate(instanceStateBundle)
    mapView?.onStart()
    mapView?.id = id
    setupLayoutHack()
    Log.e("RNN", "RNNaverMapViewContainer.onAttachedToWindow() - featureCount: ${mapView?.featureCount}")
  }

  override fun onDetachedFromWindow() {
    isAttachedToWindowFlag = false
    mapView?.onSaveInstanceState(instanceStateBundle!!)
    super.onDetachedFromWindow()
    Log.e("RNN", "RNNaverMapViewContainer.onDetachedFromWindow")
  }

  fun onDropViewInstance() {
    Log.e("RNN", "RNNaverMapViewContainer.onDropViewInstance - featureCount: ${mapView?.featureCount}")
    mapView?.onStop()
    mapView?.onDestroy()
    removeAllViews()
    instanceStateBundle!!.clear()
    instanceStateBundle = null
    mapView = null
  }

  override fun onStop() {
    Log.e("RNN", "RNNaverMapViewContainer.onStop")
    mapView?.onStop()
  }

  override fun setCenter(latLng: LatLng?) {
    mapView?.setCenter(latLng)
  }

  override fun setCenter(latLng: LatLng?, zoom: Double?, tilt: Double?, bearing: Double?) {
    mapView?.setCenter(latLng, zoom, tilt, bearing)
  }

  override fun zoomTo(latLngBounds: LatLngBounds, paddingInPx: Int) {
    mapView?.zoomTo(latLngBounds, paddingInPx)
  }

  override fun setTilt(tilt: Int) {
    mapView?.setTilt(tilt)
  }

  override fun setBearing(bearing: Int) {
    mapView?.setBearing(bearing)
  }

  override fun setZoom(zoom: Float) {
    mapView?.setZoom(zoom)
  }

  override fun setMapPadding(left: Int, top: Int, right: Int, bottom: Int) {
    mapView?.setMapPadding(left, top, right, bottom)
  }

  override fun onInitialized() {
    mapView?.onInitialized()
  }

  override fun showsMyLocationButton(show: Boolean) {
    mapView?.showsMyLocationButton(show)
  }

  override fun setCompassEnabled(show: Boolean) {
    mapView?.setCompassEnabled(show)
  }

  override fun setScaleBarEnabled(show: Boolean) {
    mapView?.setScaleBarEnabled(show)
  }

  override fun setZoomControlEnabled(show: Boolean) {
    mapView?.setZoomControlEnabled(show)
  }

  override fun setLocationTrackingMode(mode: Int) {
    mapView?.setLocationTrackingMode(mode)
  }

  override fun setMapType(value: MapType?) {
    mapView?.setMapType(value)
  }

  override fun setMinZoom(minZoomLevel: Float) {
    mapView?.setMinZoom(minZoomLevel)
  }

  override fun setMaxZoom(maxZoomLevel: Float) {
    mapView?.setMaxZoom(maxZoomLevel)
  }

  override fun setExtent(bounds: LatLngBounds?) {
    mapView?.setExtent(bounds)
  }

  override fun setBuildingHeight(height: Float) {
    mapView?.setBuildingHeight(height)
  }

  override fun setLayerGroupEnabled(layerGroup: String, enable: Boolean) {
    mapView?.setLayerGroupEnabled(layerGroup, enable)
  }

  override fun setNightModeEnabled(enable: Boolean) {
    mapView?.setNightModeEnabled(enable)
  }

  override fun setLogoMargin(left: Int, top: Int, right: Int, bottom: Int) {
    mapView?.setLogoMargin(left, top, right, bottom)
  }

  override fun setLogoGravity(gravity: Int) {
    mapView?.setLogoGravity(gravity)
  }

  override fun setScrollGesturesEnabled(enabled: Boolean) {
    mapView?.setScrollGesturesEnabled(enabled)
  }

  override fun setZoomGesturesEnabled(enabled: Boolean) {
    mapView?.setZoomGesturesEnabled(enabled)
  }

  override fun setTiltGesturesEnabled(enabled: Boolean) {
    mapView?.setTiltGesturesEnabled(enabled)
  }

  override fun setRotateGesturesEnabled(enabled: Boolean) {
    mapView?.setRotateGesturesEnabled(enabled)
  }

  override fun setStopGesturesEnabled(enabled: Boolean) {
    mapView?.setStopGesturesEnabled(enabled)
  }

  override fun setLiteModeEnabled(enabled: Boolean) {
    mapView?.setLiteModeEnabled(enabled)
  }

  override fun moveCameraFitBound(bounds: LatLngBounds, left: Int, top: Int, right: Int, bottom: Int) {
    mapView?.moveCameraFitBound(bounds, left, top, right, bottom)
  }

  override fun addFeature(child: View?, index: Int) {
    mapView?.addFeature(child, index)
    Log.e("RNN", "RNNaverMapViewContainer.addFeature(child: ${child?.javaClass?.simpleName}, index: $index)")
  }

  override fun removeFeatureAt(index: Int) {
    mapView?.removeFeatureAt(index)
    Log.e("RNN", "RNNaverMapViewContainer.removeFeatureAt(index: $index)")
  }

  override val featureCount: Int
    get() = mapView?.featureCount ?: 0

  override fun getFeatureAt(index: Int): View? {
    return mapView?.getFeatureAt(index)
  }
}
