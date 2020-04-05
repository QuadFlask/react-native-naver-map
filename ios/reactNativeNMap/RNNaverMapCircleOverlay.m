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
    _realOverlay = [NMFCircleOverlay circleOverlay:NMGLatLngMake(37.5666102, 126.9783881) radius:100];
  }
  return self;
}

- (void)setCoordinate:(NMGLatLng*) coordinate {
  // FIXME coordinate 변경이 제대로 반영이 안되는 문제가 있어 새로 객체 할당하는 방법으로 임시 수정
  _oldOverlay = _realOverlay;
  _realOverlay = [NMFCircleOverlay circleOverlay:coordinate radius:_realOverlay.radius];
  _realOverlay.fillColor = _oldOverlay.fillColor;
  _realOverlay.outlineColor = _oldOverlay.outlineColor;
  _realOverlay.outlineWidth = _oldOverlay.outlineWidth;
  _realOverlay.zIndex = _oldOverlay.zIndex;
  _realOverlay.mapView = _oldOverlay.mapView;
  _oldOverlay.mapView = nil;
  
  __block RNNaverMapCircleOverlay *this = self;
  _realOverlay.touchHandler = ^BOOL(NMFOverlay *overlay) {
    if (this.onClick != nil) {
      this.onClick(@{});
      return YES;
    }
    return NO;
  };
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
