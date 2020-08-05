//
//  RNNaverMapPolygonOverlay.m
//  reactNativeNMap
//
//  Created by Flask on 2020/03/26.
//  Copyright © 2020 flask. All rights reserved.
//

#import "RNNaverMapPolygonOverlay.h"

#import <React/RCTBridge.h>
#import <React/RCTImageLoader.h>
#import <React/RCTUtils.h>
#import <NMapsMap/NMFNaverMapView.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFPolygonOverlay.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapPolygonOverlay {
}

- (instancetype)init
{
  if ((self = [super init])) {
    _realOverlay = [NMFPolygonOverlay new];

    __block RNNaverMapPolygonOverlay *this = self;
    _realOverlay.touchHandler = ^BOOL(NMFOverlay *overlay) {
      if (this.onClick != nil) {
        this.onClick(@{});
        return YES;
      }
      return NO;
    };
  }
  return self;
}

- (void)setPolygonWithRing:(NMGLineString*) exteriorRing interiorRings:(NSArray<NMGLineString*>*) interiorRings {
  _realOverlay.polygon = [NMGPolygon polygonWithRing:exteriorRing interiorRings:interiorRings];
}

- (void)setOutlineWidth:(CGFloat) outlineWidth {
  _realOverlay.outlineWidth = outlineWidth;
}

- (void)setOutlineColor:(UIColor*) outlineColor {
  _realOverlay.outlineColor = outlineColor;
}

- (void)setColor:(UIColor*) color {
  _realOverlay.fillColor = color;
}

@end
