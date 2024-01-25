//
//  RNNaverMapPolylineOverlayManager.m
//
//  Created by flask on 19/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import "RNNaverMapPolylineOverlayManager.h"
#import "RNNaverMapPolylineOverlay.h"
#import <React/RCTUIManager.h>
#import <NMapsGeometry/NMGLatLng.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapPolylineOverlayManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  RNNaverMapPolylineOverlay *overlay = [RNNaverMapPolylineOverlay new];
  overlay.bridge = self.bridge;
  return overlay;
}

RCT_CUSTOM_VIEW_PROPERTY(coordinates, NSArray<NMGLatLng*>, RNNaverMapPolylineOverlay) {
  NSArray *inputArray = [RCTConvert NSArray:json];
  NSUInteger size = inputArray.count;
  NSMutableArray<NMGLatLng*> *points = [NSMutableArray arrayWithCapacity: size];
  for (int i=0; i<size; i++) {
    [points addObject:[RCTConvert NMGLatLng: inputArray[i]]];
  }
  view.coordinates = points;
}
RCT_EXPORT_VIEW_PROPERTY(strokeWidth, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(strokeColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(onClick, RCTDirectEventBlock)

@end
