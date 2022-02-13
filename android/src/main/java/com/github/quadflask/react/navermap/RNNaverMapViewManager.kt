package com.github.quadflask.react.navermap

import android.util.Log
import android.view.View
import com.airbnb.android.react.maps2.SizeReportingShadowNode
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.LayoutShadowNode
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.github.quadflask.react.navermap.util.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.util.FusedLocationSource
import java.util.*
import kotlin.math.roundToInt

open class RNNaverMapViewManager(private val appContext: ReactApplicationContext) : ViewGroupManager<RNNaverMapViewContainer>() {
  private val locationSource: FusedLocationSource = FusedLocationSource(appContext.currentActivity!!, 0x1000)

  companion object {
    private const val DEFAULT_PADDING_IN_DP = 56
    private const val ANIMATE_TO_REGION = 1
    private const val ANIMATE_TO_COORDINATE = 2
    private const val ANIMATE_TO_TWO_COORDINATES = 3
    private const val SET_LOCATION_TRACKING_MODE = 4
    private const val ANIMATE_TO_COORDINATES = 6
    private const val SET_LAYER_GROUP_ENABLED = 7
    private val LAYER_GROUPS = Collections.unmodifiableList(
      listOf(
        NaverMap.LAYER_GROUP_BUILDING,
        NaverMap.LAYER_GROUP_TRANSIT,
        NaverMap.LAYER_GROUP_BICYCLE,
        NaverMap.LAYER_GROUP_TRAFFIC,
        NaverMap.LAYER_GROUP_CADASTRAL,
        NaverMap.LAYER_GROUP_MOUNTAIN
      )
    )
  }

  override fun getName() = "RNNaverMapView"

  override fun createViewInstance(reactContext: ThemedReactContext): RNNaverMapViewContainer {
    Log.e("RNN", "RNNaverMapViewManager.createViewInstance")
    return RNNaverMapViewContainer(reactContext, appContext, locationSource, naverMapViewOptions)
  }

  override fun onDropViewInstance(view: RNNaverMapViewContainer) {
    super.onDropViewInstance(view)
    view.onDropViewInstance()
  }

  protected open val naverMapViewOptions: NaverMapOptions?
    get() = NaverMapOptions()

  @ReactProp(name = "center")
  fun setCenter(mapView: RNNaverMapViewContainer, option: ReadableMap?) {
    if (option != null) {
      mapView.setCenter(
        option.toNaverLatLng(),
        option.getDoubleOrNull("zoom"),
        option.getDoubleOrNull("tilt"),
        option.getDoubleOrNull("bearing")
      )
    }
  }

  @ReactProp(name = "showsMyLocationButton", defaultBoolean = false)
  fun showsMyLocationButton(mapView: RNNaverMapViewContainer, show: Boolean) {
    mapView.showsMyLocationButton(show)
  }

  @ReactProp(name = "compass", defaultBoolean = false)
  fun setCompassEnabled(mapView: RNNaverMapViewContainer, show: Boolean) {
    mapView.setCompassEnabled(show)
  }

  @ReactProp(name = "scaleBar", defaultBoolean = false)
  fun setScaleBarEnabled(mapView: RNNaverMapViewContainer, show: Boolean) {
    mapView.setScaleBarEnabled(show)
  }

  @ReactProp(name = "zoomControl", defaultBoolean = false)
  fun setZoomControlEnabled(mapView: RNNaverMapViewContainer, show: Boolean) {
    mapView.setZoomControlEnabled(show)
  }

  @ReactProp(name = "mapType", defaultInt = 0)
  fun setMapType(mapView: RNNaverMapViewContainer, mapType: Int) {
    mapView.setMapType(NaverMap.MapType.values()[mapType])
  }

  @ReactProp(name = "minZoomLevel", defaultFloat = 0f)
  fun setMinZoom(mapView: RNNaverMapViewContainer, minZoomLevel: Float) {
    mapView.setMinZoom(minZoomLevel)
  }

  @ReactProp(name = "maxZoomLevel", defaultFloat = 21.0f)
  fun setMaxZoom(mapView: RNNaverMapViewContainer, maxZoomLevel: Float) {
    mapView.setMaxZoom(maxZoomLevel)
  }

  @ReactProp(name = "extent")
  fun setExtent(mapView: RNNaverMapViewContainer, option: ReadableArray?) {
    mapView.setExtent(option?.toLatLngBounds())
  }

  @ReactProp(name = "buildingHeight", defaultFloat = 1.0f)
  fun setBuildingHeight(mapView: RNNaverMapViewContainer, height: Float) {
    mapView.setBuildingHeight(height)
  }

  @ReactProp(name = "locationTrackingMode", defaultInt = 0)
  fun locationTrackingMode(mapView: RNNaverMapViewContainer, mode: Int) {
    if (mode >= 0) mapView.setLocationTrackingMode(mode)
  }

  @ReactProp(name = "tilt", defaultInt = 0)
  fun setTilt(mapView: RNNaverMapViewContainer, tilt: Int) {
    mapView.setTilt(tilt)
  }

  @ReactProp(name = "bearing", defaultInt = 0)
  fun setBearing(mapView: RNNaverMapViewContainer, bearing: Int) {
    mapView.setBearing(bearing)
  }

  @ReactProp(name = "layerGroup")
  fun setLayerGroupEnabled(mapView: RNNaverMapViewContainer, option: ReadableMap?) {
    for (layerGroup in LAYER_GROUPS)
      mapView.setLayerGroupEnabled(layerGroup, option?.getBoolean(layerGroup) ?: false)
  }

  @ReactProp(name = "nightMode", defaultBoolean = false)
  fun setNightModeEnabled(mapView: RNNaverMapViewContainer, enable: Boolean) {
    mapView.setNightModeEnabled(enable)
  }

  @ReactProp(name = "mapPadding")
  fun setMapPadding(mapView: RNNaverMapViewContainer, padding: ReadableMap?) {
    val rect = padding.toRectF().apply { scale(mapView.resources.displayMetrics.density) }.toRect()
    mapView.setMapPadding(rect.left, rect.top, rect.right, rect.bottom)
  }

  @ReactProp(name = "logoMargin")
  fun setLogoMargin(mapView: RNNaverMapViewContainer, margin: ReadableMap?) {
    val rect = margin.toRectF().apply { scale(mapView.resources.displayMetrics.density) }.toRect()
    mapView.setLogoMargin(rect.left, rect.top, rect.right, rect.bottom)
  }

  @ReactProp(name = "logoGravity")
  fun setLogoGravity(mapView: RNNaverMapViewContainer, gravity: Int) {
    mapView.setLogoGravity(gravity)
  }

  @ReactProp(name = "scrollGesturesEnabled", defaultBoolean = true)
  fun setScrollGesturesEnabled(mapView: RNNaverMapViewContainer, enabled: Boolean) {
    mapView.setScrollGesturesEnabled(enabled)
  }

  @ReactProp(name = "zoomGesturesEnabled", defaultBoolean = true)
  fun setZoomGesturesEnabled(mapView: RNNaverMapViewContainer, enabled: Boolean) {
    mapView.setZoomGesturesEnabled(enabled)
  }

  @ReactProp(name = "tiltGesturesEnabled", defaultBoolean = true)
  fun setTiltGesturesEnabled(mapView: RNNaverMapViewContainer, enabled: Boolean) {
    mapView.setTiltGesturesEnabled(enabled)
  }

  @ReactProp(name = "rotateGesturesEnabled", defaultBoolean = true)
  fun setRotateGesturesEnabled(mapView: RNNaverMapViewContainer, enabled: Boolean) {
    mapView.setRotateGesturesEnabled(enabled)
  }

  @ReactProp(name = "stopGesturesEnabled", defaultBoolean = true)
  fun setStopGesturesEnabled(mapView: RNNaverMapViewContainer, enabled: Boolean) {
    mapView.setStopGesturesEnabled(enabled)
  }

  @ReactProp(name = "liteModeEnabled", defaultBoolean = false)
  fun setLiteModeEnabled(mapView: RNNaverMapViewContainer, enabled: Boolean) {
    mapView.setLiteModeEnabled(enabled)
  }

  override fun receiveCommand(mapView: RNNaverMapViewContainer, commandId: Int, args: ReadableArray?) {
    if (args == null) return

    fun Int.toPx(): Int {
      return (this * mapView.resources.displayMetrics.density).roundToInt()
    }

    when (commandId) {
      ANIMATE_TO_REGION -> {
        val region = args.getMap(0)
        val padding = if (args.size() == 2) args.getInt(1).toPx() else DEFAULT_PADDING_IN_DP.toPx()
        val lat = region.getDouble("latitude")
        val lng = region.getDouble("longitude")
        val latDelta = region.getDouble("latitudeDelta")
        val lngDelta = region.getDouble("longitudeDelta")
        val bounds = LatLngBounds(
          LatLng(lat - latDelta / 2, lng - lngDelta / 2),// southwest
          LatLng(lat + latDelta / 2, lng + lngDelta / 2) // northeast
        )
        mapView.moveCameraFitBound(
          bounds,
          padding,
          padding,
          padding,
          padding
        )
      }
      ANIMATE_TO_TWO_COORDINATES -> {
        val c1 = args.getMap(0).toNaverLatLng()
        val c2 = args.getMap(1).toNaverLatLng()
        val latLngBounds = arrayOf(c1, c2).toLatLngBounds()
        val padding = if (args.size() == 3) args.getInt(2).toPx() else DEFAULT_PADDING_IN_DP.toPx()
        mapView.zoomTo(
          latLngBounds,
          padding
        )
      }
      ANIMATE_TO_COORDINATES -> {
        val latLngBounds = args.getArray(0).toLatLngBounds()
        val edgePadding = args.getMap(1)

        mapView.moveCameraFitBound(
          latLngBounds,
          edgePadding.getInt("left", DEFAULT_PADDING_IN_DP.toPx()),
          edgePadding.getInt("top", DEFAULT_PADDING_IN_DP.toPx()),
          edgePadding.getInt("right", DEFAULT_PADDING_IN_DP.toPx()),
          edgePadding.getInt("bottom", DEFAULT_PADDING_IN_DP.toPx())
        )
      }
      ANIMATE_TO_COORDINATE -> mapView.setCenter(args.getMap(0).toNaverLatLng())
      SET_LOCATION_TRACKING_MODE -> mapView.setLocationTrackingMode(args.getInt(0))
      SET_LAYER_GROUP_ENABLED -> {
        val group = args.getString(0)
        val enable = args.getBoolean(1)
        when (group) {
          NaverMap.LAYER_GROUP_BUILDING -> mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, enable)
          NaverMap.LAYER_GROUP_TRANSIT -> mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, enable)
          NaverMap.LAYER_GROUP_BICYCLE -> mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, enable)
          NaverMap.LAYER_GROUP_TRAFFIC -> mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, enable)
          NaverMap.LAYER_GROUP_CADASTRAL -> mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, enable)
          NaverMap.LAYER_GROUP_MOUNTAIN -> mapView.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, enable)
        }
      }
    }
  }

  override fun getCommandsMap(): Map<String, Int> {
    return mapOf(
      "animateToRegion" to ANIMATE_TO_REGION,
      "animateToCoordinate" to ANIMATE_TO_COORDINATE,
      "animateToTwoCoordinates" to ANIMATE_TO_TWO_COORDINATES,
      "setLocationTrackingMode" to SET_LOCATION_TRACKING_MODE,
      "animateToCoordinates" to ANIMATE_TO_COORDINATES,
      "setLayerGroupEnabled" to SET_LAYER_GROUP_ENABLED
    )
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any>? {
    return mutableMapOf<String, Any>().apply {
      for (eventName in RNNaverMapViewProps.EVENT_NAMES)
        put(eventName, bubbled(eventName))
    }
  }

  override fun createShadowNodeInstance(): LayoutShadowNode {
    // A custom shadow node is needed in order to pass back the width/height of the map to the
    // view manager so that it can start applying camera moves with bounds.
    return SizeReportingShadowNode()
  }

  override fun addView(parent: RNNaverMapViewContainer, child: View, index: Int) {
    parent.addFeature(child, index)
    Log.e("RNN", "RNNaverMapViewManager.addView(child: ${child.javaClass.simpleName}, index: $index)")
  }

  override fun getChildCount(view: RNNaverMapViewContainer): Int {
    return view.featureCount
  }

  override fun getChildAt(view: RNNaverMapViewContainer, index: Int): View {
    return view.getFeatureAt(index)!!
  }

  override fun removeViewAt(parent: RNNaverMapViewContainer, index: Int) {
    parent.removeFeatureAt(index)
    Log.e("RNN", "RNNaverMapViewManager.removeFeatureAt(index: $index)")
  }

  override fun needsCustomLayoutForChildren(): Boolean {
    return true
  }

  private fun bubbled(callbackName: String): Map<String, Any> {
    return mapOf("phasedRegistrationNames" to mapOf("bubbled" to callbackName))
  }
}
