//
//  RCTConvert+RNNaverMapView.h
//
//  Created by flask on 14/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import <React/RCTConvert.h>

#import <NMapsMap/NMFMapView.h>
#import <NMapsMap/NMFOverlay.h>

@interface RCTConvert(NMFMapView)

+ (NMFCameraUpdate *) NMFCameraUpdate: (id) json;
+ (NMFCameraUpdate *) NMFCameraUpdateWith: (id) json;
+ (NMGLatLng *) NMGLatLng: (id)json;
+ (NMGLatLngBounds *) NMGLatLngBounds: (id)json;
+ (NMFAlignType *) NMFAlignType: (id)json;

@end
