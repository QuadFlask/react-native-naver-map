package com.airbnb.android.react.maps2

import android.content.Context
import android.graphics.Rect
import android.os.Build
import com.facebook.react.views.view.ReactViewGroup

class ViewAttacherGroup(context: Context?) : ReactViewGroup(context) {
  // This should make it more performant, avoid trying to hard to overlap layers with opacity.
  override fun hasOverlappingRendering(): Boolean {
    return false
  }

  init {
    setWillNotDraw(true)
    visibility = VISIBLE
    alpha = 0.0f
    removeClippedSubviews = false
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      clipBounds = Rect(0, 0, 0, 0)
    }
    overflow = "hidden" // Change to ViewProps.HIDDEN until RN 0.57 is base
  }
}
