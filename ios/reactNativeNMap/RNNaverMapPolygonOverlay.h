//
//  RNNaverMapPolygonOverlay.h
//  reactNativeNMap
//
//  Created by Flask on 2020/03/26.
//  Copyright © 2020 flask. All rights reserved.
//

#import <React/RCTBridge.h>
#import <React/RCTComponent.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFPolygonOverlay.h>

#import "RCTConvert+NMFMapView.h"

@interface RNNaverMapPolygonOverlay : UIView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NMFPolygonOverlay *realOverlay;

@property (nonatomic, strong) UIColor *color;
@property (nonatomic, strong) UIColor *outlineColor;
@property (nonatomic, assign) CGFloat outlineWidth;
@property (nonatomic, copy) RCTDirectEventBlock onClick;

- (void)setPolygonWithRing:(NMGLineString*) exteriorRing interiorRings:(NSArray<NMGLineString*>*) interiorRings;

@end
