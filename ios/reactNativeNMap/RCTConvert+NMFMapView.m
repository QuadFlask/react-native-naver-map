//
//  RCTConvert+RNNaverMapView.m
//
//  Created by flask on 14/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import <NMapsMap/NMFCameraUpdate.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFCameraPosition.h>
#import <NMapsMap/NMGLatLngBounds.h>

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

+ (NMGLatLngBounds*) NMGLatLngBounds: (id)json
{
    json = [self NSDictionary:json];
    double lat = [self double: json[@"latitude"]];
    double latDelta = [self double: json[@"latitudeDelta"]];
    double lng = [self double: json[@"longitude"]];
    double lngDelta = [self double: json[@"longitudeDelta"]];
    return NMGLatLngBoundsMake(lat - latDelta / 2, lng - lngDelta / 2,  // southwest
                               lat + latDelta / 2, lng + lngDelta / 2); // northeast
}

@end
