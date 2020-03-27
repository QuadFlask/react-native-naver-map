//
//  RNNaverMapPolygonOverlayManager.m
//  reactNativeNMap
//
//  Created by Flask on 2020/03/26.
//  Copyright © 2020 flask. All rights reserved.
//

#import "RNNaverMapPolygonOverlayManager.h"
#import "RNNaverMapPolygonOverlay.h"
#import <React/RCTUIManager.h>
#import <NMapsMap/NMGLatLng.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapPolygonOverlayManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  RNNaverMapPolygonOverlay *overlay = [RNNaverMapPolygonOverlay new];
  overlay.bridge = self.bridge;
  return overlay;
}

RCT_CUSTOM_VIEW_PROPERTY(coordinates, NSArray<NMGLatLng*>, RNNaverMapPolygonOverlay) {
  NSArray *inputArray = [RCTConvert NSArray:json];
  NSUInteger size = inputArray.count;
  NSMutableArray<NMGLatLng*> *points = [NSMutableArray arrayWithCapacity: size];
  for (int i=0; i<size; i++) {
    [points addObject:[RCTConvert NMGLatLng: inputArray[i]]];
  }
  view.coordinates = points;
}
RCT_EXPORT_VIEW_PROPERTY(outlineWidth, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(outlineColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(onClick, RCTDirectEventBlock)

@end
