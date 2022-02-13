package com.github.quadflask.react.navermap.feature

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Animatable
import android.net.Uri
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
import com.github.quadflask.react.navermap.util.OverlayImages
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PathOverlay
import kotlin.math.roundToInt

class RNNaverMapPathOverlay(
  emitter: EventEmittable,
  context: Context
) : ClickableRNNaverMapFeature<PathOverlay>(emitter, context) {
  private val imageHolder: DraweeHolder<GenericDraweeHierarchy>

  init {
    feature = PathOverlay()
    imageHolder = DraweeHolder.create(createDraweeHierarchy(), context)
    imageHolder.onAttach()
  }

  private fun createDraweeHierarchy(): GenericDraweeHierarchy {
    return GenericDraweeHierarchyBuilder(resources)
      .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
      .setFadeDuration(0)
      .build()
  }

  fun setCoords(coords: List<LatLng>) {
    feature.coords = coords
  }

  fun setWidth(widthInScreenPx: Float) {
    feature.width = widthInScreenPx.roundToInt()
  }

  fun setZIndex(zIndex: Int) {
    feature.zIndex = zIndex
  }

  fun setProgress(progress: Float) {
    feature.progress = progress.toDouble()
  }

  fun setOutlineWidth(widthInScreenPx: Float) {
    feature.outlineWidth = widthInScreenPx.roundToInt()
  }

  fun setColor(color: Int) {
    feature.color = color
  }

  fun setPassedColor(color: Int) {
    feature.passedColor = color
  }

  fun setOutlineColor(color: Int) {
    feature.outlineColor = color
  }

  fun setPassedOutlineColor(color: Int) {
    feature.passedOutlineColor = color
  }

  fun setPatternInterval(patternInterval: Int) {
    feature.patternInterval = patternInterval
  }

  fun setPattern(uri: String?) {
    // TODO refactor
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
                animatable: Animatable?) {
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
                        OverlayImages.put(uri, overlayImage)
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
          OverlayImages.put(uri, overlayImage1)
          setOverlayImage(overlayImage1)
        }
      }
    }
  }

  private fun setOverlayImage(image: OverlayImage) {
    feature.patternImage = image
  }

  private fun getRidFromName(name: String): Int {
    return context.resources.getIdentifier(name, "drawable", context.packageName)
  }
}
