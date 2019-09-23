//
//  RNNaverMapView.m
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import "RNNaverMapView.h"
#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>
#import <React/RCTBridge.h>
#import <React/UIView+React.h>

#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFMarker.h>
#import <NMapsMap/NMFCameraUpdate.h>

#import "RCTConvert+NMFMapView.h"
#import "RNNaverMapMarker.h"
#import "RNNaverMapPolylineOverlay.h"
#import "RNNaverMapPathOverlay.h"

@interface RNNaverMapView()
@end

@implementation RNNaverMapView
{
  NSMutableArray<UIView *> *_reactSubviews;
}

- (instancetype)init
{
  if ((self = [super init])) {
    _reactSubviews = [NSMutableArray new];
    _markers = [NSMutableArray array];
    _polylines = [NSMutableArray array];
    _paths = [NSMutableArray array];
  }
  return self;
}

- (void)insertReactSubview:(id<RCTComponent>)subview atIndex:(NSInteger)atIndex {
  // Our desired API is to pass up markers/overlays as children to the mapview component.
  // This is where we intercept them and do the appropriate underlying mapview action.
  if ([subview isKindOfClass:[RNNaverMapMarker class]]) {
    RNNaverMapMarker *marker = (RNNaverMapMarker*)subview;
    marker.realMarker.mapView = self.mapView;
    [self.markers addObject:marker];
  } else if ([subview isKindOfClass:[RNNaverMapPolylineOverlay class]]) {
    RNNaverMapPolylineOverlay *overlay = (RNNaverMapPolylineOverlay*)subview;
    overlay.realOverlay.mapView = self.mapView;
    [self.polylines addObject:overlay];
  } else if ([subview isKindOfClass:[RNNaverMapPathOverlay class]]) {
    RNNaverMapPathOverlay *overlay = (RNNaverMapPathOverlay*)subview;
    overlay.realOverlay.mapView = self.mapView;
    [self.paths addObject:overlay];
  } else {
    NSArray<id<RCTComponent>> *childSubviews = [subview reactSubviews];
    for (int i = 0; i < childSubviews.count; i++) {
      [self insertReactSubview:(UIView *)childSubviews[i] atIndex:atIndex];
    }
  }
  [_reactSubviews insertObject:(UIView *)subview atIndex:(NSUInteger) atIndex];
}

- (void)removeReactSubview:(id<RCTComponent>)subview {
  // similarly, when the children are being removed we have to do the appropriate
  // underlying mapview action here.
  if ([subview isKindOfClass:[RNNaverMapMarker class]]) {
    RNNaverMapMarker *marker = (RNNaverMapMarker*)subview;
    marker.realMarker.mapView = nil;
    [self.markers removeObject:marker];
  } else if ([subview isKindOfClass:[RNNaverMapPolylineOverlay class]]) {
    RNNaverMapPolylineOverlay *overlay = (RNNaverMapPolylineOverlay*)subview;
    overlay.realOverlay.mapView = nil;
    [self.polylines removeObject:overlay];
  } else if ([subview isKindOfClass:[RNNaverMapPathOverlay class]]) {
    RNNaverMapPathOverlay *overlay = (RNNaverMapPathOverlay*)subview;
    overlay.realOverlay.mapView = nil;
    [self.paths removeObject:overlay];
  } else {
    NSArray<id<RCTComponent>> *childSubviews = [subview reactSubviews];
    for (int i = 0; i < childSubviews.count; i++) {
      [self removeReactSubview:(UIView *)childSubviews[i]];
    }
  }
  [_reactSubviews removeObject:(UIView *)subview];
}
- (NSArray<id<RCTComponent>> *)reactSubviews {
  return _reactSubviews;
}

@end
