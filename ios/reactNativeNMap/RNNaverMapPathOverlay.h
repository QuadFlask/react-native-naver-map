//
//  RNNaverMapPolylineOverlay.h
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import <React/RCTBridge.h>
#import <React/RCTComponent.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFPath.h>

#import "RCTConvert+NMFMapView.h"

@interface RNNaverMapPathOverlay : UIView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NMFPath *realOverlay;

@property (nonatomic, assign) NSArray<NMGLatLng *> *coordinates;
@property (nonatomic, assign) CGFloat width;
@property (nonatomic, assign) UIColor* color;
@property (nonatomic, assign) CGFloat outlineWidth;
@property (nonatomic, assign) UIColor* passedColor;
@property (nonatomic, assign) UIColor* outlineColor;
@property (nonatomic, assign) UIColor* passedOutlineColor;
@property (nonatomic, assign) NSString* pattern;
@property (nonatomic, assign) CGFloat patternInterval;
@property (nonatomic, copy) RCTDirectEventBlock onClick;

@end
