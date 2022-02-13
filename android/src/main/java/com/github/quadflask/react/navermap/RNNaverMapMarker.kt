package com.github.quadflask.react.navermap

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Property
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.animation.*
import com.facebook.common.references.CloseableReference
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.view.DraweeHolder
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.CloseableStaticBitmap
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.github.quadflask.react.navermap.OverlayImages.put
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import java.lang.Double.isNaN

class RNNaverMapMarker(
  emitter: EventEmittable,
  context: Context
) : ClickableRNNaverMapFeature<Marker>(emitter, context) {
  private val imageHolder: DraweeHolder<GenericDraweeHierarchy>
  private var animated = false
  private var duration = 500
  private var easingFunction: TimeInterpolator? = null
  private var customView: View? = null
  private var infoWindow: RNNaverMapInfoWindow? = null
  private val onLayoutChangeListener = OnLayoutChangeListener { v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int ->
    Log.e("RNN", "RNNaverMapMarker.onLayoutChangeListener: $left, $top, $right, $bottom")
    updateCustomView()
  }

  init {
    feature = Marker()
    imageHolder = DraweeHolder.create(createDraweeHierarchy(), context)
    imageHolder.onAttach()
  }

  private fun createDraweeHierarchy(): GenericDraweeHierarchy {
    return GenericDraweeHierarchyBuilder(resources)
      .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
      .setFadeDuration(0)
      .build()
  }

  fun setCoordinate(latLng: LatLng) {
    if (animated && duration > 0) setCoordinateAnimated(latLng, duration) else feature.position = latLng
  }

  fun setCoordinateAnimated(finalPosition: LatLng, duration: Int) {
    if (isNaN(feature.position.latitude)) {
      feature.position = finalPosition
    } else {
      ObjectAnimator.ofObject(
        feature,
        Property.of(Marker::class.java, LatLng::class.java, "position"),
        TypeEvaluator(ReactUtil::interpolate),
        finalPosition
      ).also {
        it.duration = duration.toLong()
        if (easingFunction != null) it.interpolator = easingFunction
      }.start()
    }
  }

  fun setAnimated(animated: Boolean) {
    this.animated = animated
  }

  fun setEasing(easing: Int?) {
    easingFunction = when (easing) {
      0 -> LinearInterpolator()
      1 -> AccelerateDecelerateInterpolator()
      2 -> AccelerateInterpolator()
      3 -> DecelerateInterpolator()
      4 -> BounceInterpolator()
      else -> null
    }
  }

  fun setDuration(duration: Int) {
    if (duration >= 0) {
      this.duration = duration
    }
  }

  override fun setAlpha(alpha: Float) {
    feature.alpha = alpha
  }

  fun setZIndex(zIndex: Int) {
    feature.zIndex = zIndex
  }

  fun setAnchor(anchor: PointF) {
    feature.anchor = anchor
  }

  fun setIconTintColor(color: Int) {
    feature.iconTintColor = color
  }

  fun setFlat(flat: Boolean) {
    feature.isFlat = flat
  }

  override fun setRotation(rotation: Float) {
    feature.angle = rotation
  }

  fun setWidth(width: Int) {
    feature.width = width
    updateCustomView()
  }

  fun setHeight(height: Int) {
    feature.height = height
    updateCustomView()
  }

  fun setCaption(text: String, textSize: Int, color: Int, haloColor: Int, vararg aligns: Align?) {
    feature.captionText = text
    feature.captionTextSize = textSize.toFloat()
    feature.captionColor = color
    feature.captionHaloColor = haloColor
    feature.setCaptionAligns(*aligns)
  }

  fun removeCaption() {
    feature.captionText = ""
  }

  fun setImage(uri: String?) {
    if (uri != null) {
      val overlayImage = OverlayImages[uri]
      if (overlayImage != null) {
        setOverlayImage(overlayImage)
      } else {
        if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("file://") || uri.startsWith("asset://")) {
          val imageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(uri))
            .build()
          val dataSource = Fresco.getImagePipeline()
            .fetchDecodedImage(imageRequest, this)
          val controller: DraweeController = Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest)
            .setControllerListener(object : BaseControllerListener<ImageInfo?>() {
              override fun onFinalImageSet(
                id: String,
                imageInfo: ImageInfo?,
                animatable: Animatable?
              ) {
                var imageReference: CloseableReference<CloseableImage>? = null
                var overlayImage: OverlayImage? = null
                try {
                  imageReference = dataSource.result
                  if (imageReference != null) {
                    val image = imageReference.get()
                    if (image is CloseableStaticBitmap) {
                      var bitmap = image.underlyingBitmap
                      if (bitmap != null) {
                        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                        overlayImage = OverlayImage.fromBitmap(bitmap)
                        put(uri, overlayImage)
                      }
                    }
                  }
                } finally {
                  dataSource.close()
                  if (imageReference != null) {
                    CloseableReference.closeSafely(imageReference)
                  }
                }
                overlayImage?.let { setOverlayImage(it) }
              }
            })
            .setOldController(imageHolder.controller)
            .build()
          imageHolder.setController(controller)
        } else {
          val rid = getRidFromName(uri)
          val overlayImage1 = OverlayImage.fromResource(rid)
          put(uri, overlayImage1)
          setOverlayImage(overlayImage1)
        }
      }
    }
  }

  private fun setOverlayImage(image: OverlayImage?) {
    image?.let { feature.icon = it }
  }

  private fun getRidFromName(name: String): Int {
    return context.resources.getIdentifier(name, "drawable", context.packageName)
  }

  override fun addToMap(map: NaverMap) {
    super.addToMap(map)
    updateCustomView()
    infoWindow?.refresh()
    Handler(Looper.getMainLooper()).postDelayed({ updateCustomView() }, 500) // temporary
  }

  override fun addView(view: View, index: Int) {
    Log.e("RNN", "addView: class name:" + view::class.java)
    super.addView(view, index)
    if (view is RNNaverMapInfoWindow) {
      if (infoWindow == null) {
        infoWindow = view
        view.setMarker(feature)
      }
    } else {
      setCustomView(view)
    }
  }

  override fun removeView(view: View) {
    Log.e("RNN", "removeView: class name:" + view::class.java)
    super.removeView(view)
    if (view is RNNaverMapInfoWindow) {
      infoWindow = null
      view.setOpen(false)
      view.setMarker(null)
    } else {
      removeCustomView()
    }
  }

  override fun removeViewAt(index: Int) {
    Log.e("RNN", "removeViewAt: " + index)
    removeView(getChildAt(index))
  }

  private fun setCustomView(view: View) {
    customView = view
    addOnLayoutChangeListener(onLayoutChangeListener)
    updateCustomView()
  }

  private fun removeCustomView() {
    customView = null
    removeOnLayoutChangeListener(onLayoutChangeListener)
    setOverlayImage(null)
  }

  fun updateCustomView() {
    if (customView != null) {
      Log.e("RNN", "RNNaverMapMarker.updateCustomView")
      setOverlayImage(OverlayImage.fromView(this))
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    Log.e("RNN", "RNNaverMapMarker.onMeasure($widthMeasureSpec, $heightMeasureSpec), size: ${feature.width}x${feature.height}")
    super.onMeasure(MeasureSpec.AT_MOST, MeasureSpec.AT_MOST)
    this.setMeasuredDimension(feature.width, feature.height)
  }

  override fun requestLayout() {
    super.requestLayout()
    if (childCount == 0 && customView != null) {
      customView = null
      updateCustomView()
    }
  }
}
