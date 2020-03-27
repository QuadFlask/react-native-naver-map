//
//  RNNaverMapCircleOverlay.m
//
//  Created by flask on 17/03/2020.
//  Copyright © 2020 flask. All rights reserved.
//

#import "RNNaverMapCircleOverlay.h"

#import <React/RCTBridge.h>
#import <React/RCTUtils.h>
#import <NMapsMap/NMFNaverMapView.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFCircleOverlay.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapCircleOverlay {
}

- (instancetype)init
{
  if ((self = [super init])) {
    _realOverlay = [NMFCircleOverlay circleOverlay:NMGLatLngMake(37.5666102, 126.9783881) radius:500];

    __block RNNaverMapCircleOverlay *this = self;
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

- (void)setCoordinate:(NMGLatLng*) coordinate {
  _realOverlay.center = coordinate;
}

- (void)setRadius:(CGFloat) radius {
  _realOverlay.radius = radius;
}

- (void)setColor:(UIColor*) color {
  _realOverlay.fillColor = color;
}

- (void)setOutlineWidth:(CGFloat) outlineWidth {
  _realOverlay.outlineWidth = outlineWidth;
}

- (void)setOutlineColor:(UIColor*) outlineColor {
  _realOverlay.outlineColor = outlineColor;
}

- (void)setZIndex:(NSInteger) zIndex {
  _realOverlay.zIndex = zIndex;
}

@end
