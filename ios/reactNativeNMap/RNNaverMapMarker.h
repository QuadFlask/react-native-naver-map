//
//  RNNaverMapMarker.h
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import <React/RCTBridge.h>
#import <React/RCTComponent.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFMarker.h>

#import "RCTConvert+NMFMapView.h"

@interface RNNaverMapMarker : UIView

@property (nonatomic, weak) RCTBridge *bridge;
@property (nonatomic, strong) NMFMarker *realMarker;

@property (nonatomic, assign) NMGLatLng *coordinate;
@property (nonatomic, assign) CGFloat width;
@property (nonatomic, assign) CGFloat height;
@property (nonatomic, assign) CGFloat rotation;
@property (nonatomic, copy) NSString *image;
@property (nonatomic, strong) UIColor *pinColor;
@property (nonatomic, assign) CGPoint anchor;
@property (nonatomic, copy) RCTDirectEventBlock onClick;

- (void)setCaptionText:(NSString *) text;
- (void)setCaptionTextSize:(CGFloat) size;
- (void)setCaptionColor:(UIColor *) color;
- (void)setCaptionHaloColor:(UIColor *) color;
- (void)setCaptionAligns:(NSArray<NMFAlignType *> *) aligns;

@end
