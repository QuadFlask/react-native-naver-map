//
//  RNNaverMapMarkerManager.m
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import "RNNaverMapMarkerManager.h"
#import "RNNaverMapMarker.h"
#import <React/RCTUIManager.h>
#import <NMapsMap/NMGLatLng.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapMarkerManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  RNNaverMapMarker *marker = [RNNaverMapMarker new];
  marker.bridge = self.bridge;
  return marker;
}

RCT_EXPORT_VIEW_PROPERTY(coordinate, NMGLatLng)
RCT_EXPORT_VIEW_PROPERTY(width, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(height, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(rotation, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(image, NSString)
RCT_EXPORT_VIEW_PROPERTY(pinColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(anchor, CGPoint)
RCT_EXPORT_VIEW_PROPERTY(onClick, RCTDirectEventBlock)

@end
