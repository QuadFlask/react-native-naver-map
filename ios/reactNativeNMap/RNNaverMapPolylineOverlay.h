//
//  RNNaverMapPolylineOverlay.h
//  poolusDriver
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <React/RCTBridge.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFPolylineOverlay.h>

#import "RCTConvert+NMFMapView.h"

@interface RNNaverMapPolylineOverlay : UIView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NMFPolylineOverlay *realOverlay;

@property (nonatomic, assign) NSArray<NMGLatLng *> *coordinates;
@property (nonatomic, assign) CGFloat strokeWidth;
@property (nonatomic, strong) UIColor *strokeColor;

@end
