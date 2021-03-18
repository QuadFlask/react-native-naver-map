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
@property (nonatomic, assign) NSInteger zIndex;
@property (nonatomic, assign) BOOL hidden;
@property (nonatomic, assign) CGFloat angle;
@property (nonatomic, assign) BOOL flatEnabled;
@property (nonatomic, assign) BOOL iconPerspectiveEnabled;
@property (nonatomic, assign) BOOL isHideCollidedSymbols;
@property (nonatomic, assign) BOOL isHideCollidedMarkers;
@property (nonatomic, assign) BOOL isHideCollidedCaptions;
@property (nonatomic, assign) BOOL isForceShowIcon;
@property (nonatomic, assign) CGFloat rotation;
@property (nonatomic, copy) NSString *image;
@property (nonatomic, strong) UIColor *pinColor;
@property (nonatomic, assign) CGFloat alpha;
@property (nonatomic, assign) CGPoint anchor;
@property (nonatomic, copy) RCTDirectEventBlock onClick;

- (void)setCaptionText:(NSString *) text;
- (void)setCaptionTextSize:(CGFloat) size;
- (void)setCaptionColor:(UIColor *) color;
- (void)setCaptionHaloColor:(UIColor *) color;
- (void)setCaptionAligns:(NSArray<NMFAlignType *> *) aligns;
- (void)setCaptionOffset:(CGFloat) offset;
- (void)setCaptionRequestedWidth:(CGFloat) captionWidth;
- (void)setCaptionMinZoom:(double) minZoom;
- (void)setCaptionMaxZoom:(double) maxZoom;
- (void)setSubCaptionText:(NSString *) subText;
- (void)setSubCaptionTextSize:(CGFloat) subTextSize;
- (void)setSubCaptionColor:(UIColor *) subColor;
- (void)setSubCaptionHaloColor:(UIColor *) subHaloColor;
- (void)setSubCaptionRequestedWidth:(CGFloat) subCaptionWidth;
- (void)setSubCaptionMinZoom:(double) subMinZoom;
- (void)setSubCaptionMaxZoom:(double) subMaxZoom;
@end
