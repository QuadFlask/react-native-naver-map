//
//  RNNaverMapView.h
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/RCTComponent.h>
#import <React/RCTBridge.h>

#import <NMapsMap/NMFNaverMapView.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFMarker.h>
#import <NMapsMap/NMFCameraUpdate.h>
#import <NMapsMap/NMFMapViewDelegate.h>

#import "RCTConvert+NMFMapView.h"

@interface RNNaverMapView : NMFNaverMapView <NMFMapViewDelegate>

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, copy) RCTDirectEventBlock onInitialized;
@property (nonatomic, copy) RCTDirectEventBlock onCameraChange;
@property (nonatomic, copy) RCTDirectEventBlock onTouch;
@property (nonatomic, copy) RCTDirectEventBlock onMapClick;

@property (nonatomic, assign) BOOL showsCompass;
@property (nonatomic, assign) BOOL zoomEnabled;
@property (nonatomic, assign) BOOL showsMyLocationButton;

@end
