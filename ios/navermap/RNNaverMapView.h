//
//  RNNaverMapView.h
//  poolusDriver
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>
#import <React/RCTBridge.h>

#import <NMapsMap/NMFNaverMapView.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFMarker.h>
#import <NMapsMap/NMFCameraUpdate.h>

#import "RCTConvert+NMFMapView.h"

@interface RNNaverMapView : NMFNaverMapView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, copy) RCTBubblingEventBlock onMapReady;
@property (nonatomic, copy) RCTDirectEventBlock onRegionChange;
@property (nonatomic, strong) NSMutableArray *markers;
@property (nonatomic, strong) NSMutableArray *polylines;
@property (nonatomic, strong) NSMutableArray *paths;

@property (nonatomic, assign) BOOL showsCompass;
@property (nonatomic, assign) BOOL zoomEnabled;
@property (nonatomic, assign) BOOL showsMyLocationButton;

@end
