//
//  RCTConvert+RNNaverMapView.m
//  poolusDriver
//
//  Created by flask on 14/04/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <NMapsMap/NMFCameraUpdate.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFCameraPosition.h>

#import "RCTConvert+NMFMapView.h"

@implementation RCTConvert(NMFMapView)

+ (NMFCameraUpdate*) NMFCameraUpdate: (id)json
{
  json = [self NSDictionary:json];
  return [NMFCameraUpdate cameraUpdateWithScrollTo:NMGLatLngMake([self double: json[@"latitude"]], [self double:json[@"longitude"]])
                                            zoomTo:[self double: json[@"zoom"]]];
}

+ (NMFCameraUpdate*) NMFCameraUpdateWith:(id)json
{
  NMGLatLng *position = NMGLatLngMake([self double: json[@"latitude"]], [self double:json[@"longitude"]]);
  double zoom = [self double: json[@"zoom"]];
  double tilt = [self double: json[@"tilt"]];
  double bearing = [self double: json[@"bearing"]];
  
  NMFCameraPosition* cameraPosition = [NMFCameraPosition cameraPosition:position zoom:zoom tilt:tilt heading:bearing];
  NMFCameraUpdate *cameraUpdate = [NMFCameraUpdate cameraUpdateWithPosition: cameraPosition];
  cameraUpdate.animation = NMFCameraUpdateAnimationEaseIn;
  return cameraUpdate;
}

+ (NMGLatLng*) NMGLatLng: (id)json
{
  json = [self NSDictionary:json];
  return NMGLatLngMake([self double: json[@"latitude"]], [self double:json[@"longitude"]]);
}

@end
