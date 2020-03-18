//
//  RNNaverMapPolylineOverlay.m
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//
#import "RNNaverMapPolylineOverlay.h"

#import <React/RCTBridge.h>
#import <React/RCTImageLoader.h>
#import <React/RCTUtils.h>
#import <NMapsMap/NMFNaverMapView.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFPolylineOverlay.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapPolylineOverlay {
}

- (instancetype)init
{
  if ((self = [super init])) {
    _realOverlay = [NMFPolylineOverlay new];

    __block RNNaverMapPolylineOverlay *this = self;
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

- (void)setCoordinates:(NSArray<NMGLatLng *>*) coordinates {
  _realOverlay.line = [NMGLineString lineStringWithPoints:coordinates];
}

- (void)setStrokeWidth:(CGFloat) strokeWidth {
  _realOverlay.width = strokeWidth;
}

- (void)setStrokeColor:(UIColor*) strokeColor {
  _realOverlay.color = strokeColor;
}

@end
