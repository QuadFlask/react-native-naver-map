//
//  RNNaverMapPolygonOverlayManager.m
//  reactNativeNMap
//
//  Created by Flask on 2020/03/26.
//  Copyright © 2020 flask. All rights reserved.
//

#import "RNNaverMapPolygonOverlayManager.h"
#import "RNNaverMapPolygonOverlay.h"
#import <React/RCTUIManager.h>
#import <NMapsGeometry/NMGLatLng.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapPolygonOverlayManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
  RNNaverMapPolygonOverlay *overlay = [RNNaverMapPolygonOverlay new];
  overlay.bridge = self.bridge;
  return overlay;
}

RCT_CUSTOM_VIEW_PROPERTY(coordinates, NSDictionary, RNNaverMapPolygonOverlay) {
    NSDictionary *dic = [RCTConvert NSDictionary:json];

    // process exterior ring
    NSArray *exteriorRing = [RCTConvert NSArray:dic[@"exteriorRing"]];
    NSUInteger size = exteriorRing.count;
    NSMutableArray<NMGLatLng*> *points = [NSMutableArray arrayWithCapacity: size];
    for (int i=0; i<size; i++) {
        [points addObject:[RCTConvert NMGLatLng: exteriorRing[i]]];
    }
    NMGLineString *exRing = [NMGLineString lineStringWithPoints:points];

    // process interior rings
    NSArray<NSArray*> *interiorRings = [RCTConvert NSArrayArray:dic[@"interiorRings"]];
    NSMutableArray<NMGLineString*> *inRings = [[NSMutableArray alloc] init];
    for (NSArray *interiorRing in interiorRings) {
        NSMutableArray<NMGLatLng*> *ring = [[NSMutableArray alloc] init];
        for (id coord in interiorRing) {
            [ring addObject:[RCTConvert NMGLatLng:coord]];
        }
        [inRings addObject:[NMGLineString lineStringWithPoints:ring]];
    }

    // set polygon
    [view setPolygonWithRing:exRing interiorRings:inRings];
}
RCT_EXPORT_VIEW_PROPERTY(outlineWidth, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(color, UIColor)
RCT_EXPORT_VIEW_PROPERTY(outlineColor, UIColor)
RCT_EXPORT_VIEW_PROPERTY(onClick, RCTDirectEventBlock)

@end
