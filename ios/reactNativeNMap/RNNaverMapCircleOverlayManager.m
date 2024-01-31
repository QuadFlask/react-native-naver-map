//
//  RNNaverMapCircleOverlayManager.m
//
//  Created by flask on 17/03/2020.
//  Copyright © 2020 flask. All rights reserved.
//

#import "RNNaverMapCircleOverlayManager.h"
#import "RNNaverMapCircleOverlay.h"
#import <React/RCTUIManager.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapCircleOverlayManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  RNNaverMapCircleOverlay *overlay = [RNNaverMapCircleOverlay new];
  overlay.bridge = self.bridge;
  return overlay;
}

RCT_EXPORT_VIEW_PROPERTY(coordinate, NMGLatLng)
RCT_EXPORT_VIEW_PROPERTY(radius, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(outlineWidth, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(outlineColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(zIndex, NSInteger)

@end
