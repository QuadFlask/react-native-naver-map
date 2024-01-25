//
//  RNNaverMapPolylineOverlayManager.m
//
//  Created by flask on 19/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import "RNNaverMapPathOverlayManager.h"
#import "RNNaverMapPathOverlay.h"
#import <React/RCTUIManager.h>
#import <NMapsGeometry/NMGLatLng.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapPathOverlayManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  RNNaverMapPathOverlay *overlay = [RNNaverMapPathOverlay new];
  overlay.bridge = self.bridge;
  return overlay;
}

RCT_CUSTOM_VIEW_PROPERTY(coordinates, NSArray<NMGLatLng*>, RNNaverMapPathOverlay) {
  NSArray *inputArray = [RCTConvert NSArray:json];
  NSUInteger size = inputArray.count;
  NSMutableArray<NMGLatLng*> *points = [NSMutableArray arrayWithCapacity: size];
  for (int i=0; i<size; i++) {
    [points addObject:[RCTConvert NMGLatLng: inputArray[i]]];
  }
  view.coordinates = points;
}
RCT_EXPORT_VIEW_PROPERTY(width, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(outlineWidth, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(passedColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(outlineColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(passedOutlineColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(pattern, NSString)
RCT_EXPORT_VIEW_PROPERTY(patternInterval, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(onClick, RCTDirectEventBlock)

@end
