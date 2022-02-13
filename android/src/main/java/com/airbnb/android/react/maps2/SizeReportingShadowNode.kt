package com.airbnb.android.react.maps2

import com.facebook.react.uimanager.LayoutShadowNode
import com.facebook.react.uimanager.UIViewOperationQueue

class SizeReportingShadowNode : LayoutShadowNode() {
  override fun onCollectExtraUpdates(uiViewOperationQueue: UIViewOperationQueue) {
    super.onCollectExtraUpdates(uiViewOperationQueue)
    uiViewOperationQueue.enqueueUpdateExtraData(reactTag, mapOf(
      "width" to layoutWidth,
      "height" to layoutHeight
    ))
  }
}
