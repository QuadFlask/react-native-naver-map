//
//  RNNaverMapPolylineOverlay.m
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//
#import "RNNaverMapPathOverlay.h"

#import <React/RCTBridge.h>
#import <React/RCTImageLoader.h>
#import <React/RCTUtils.h>
#import <NMapsMap/NMFNaverMapView.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFPath.h>
#import <NMapsMap/NMFOverlayImage.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapPathOverlay {
  RCTImageLoaderCancellationBlock _reloadImageCancellationBlock;
}

- (instancetype)init
{
  if ((self = [super init])) {
    _realOverlay = [NMFPath new];

    __block RNNaverMapPathOverlay *this = self;
    _realOverlay.touchHandler = ^BOOL(NMFOverlay *overlay) {
      if (this.onClick != nil) {
        this.onClick(@{});
        return YES;
      }
      return NO;
    };
  }
  return self;
}

- (void)setCoordinates:(NSArray<NMGLatLng *>*) coordinates {
  _realOverlay.path = [NMGLineString lineStringWithPoints:coordinates];
}

- (void) setWidth: (CGFloat) width {
  _realOverlay.width = width;
}

- (void) setColor: (UIColor*) color {
  _realOverlay.color = color;
}

- (void) setOutlineWidth: (CGFloat) outlineWidth {
  _realOverlay.outlineWidth = outlineWidth;
}

- (void) setPassedColor: (UIColor*) passedColor {
  _realOverlay.passedColor = passedColor;
}

- (void) setOutlineColor: (UIColor*) outlineColor {
  _realOverlay.outlineColor = outlineColor;
}

- (void) setPassedOutlineColor: (UIColor*) passedOutlineColor {
  _realOverlay.passedOutlineColor = passedOutlineColor;
}

- (void) setPattern: (NSString*) pattern {
  if (_reloadImageCancellationBlock) {
    _reloadImageCancellationBlock();
    _reloadImageCancellationBlock = nil;
  }

  _reloadImageCancellationBlock = [[_bridge moduleForClass:[RCTImageLoader class]] loadImageWithURLRequest:[RCTConvert NSURLRequest:pattern]
                                                                          size:self.bounds.size
                                                                         scale:RCTScreenScale()
                                                                       clipped:YES
                                                                    resizeMode:RCTResizeModeCenter
                                                                 progressBlock:nil
                                                              partialLoadBlock:nil
                                                               completionBlock:^(NSError *error, UIImage *image) {
                                                                 if (error) {
                                                                   NSLog(@"%@", error);
                                                                 }
                                                                 dispatch_async(dispatch_get_main_queue(), ^{
                                                                   self->_realOverlay.patternIcon = [NMFOverlayImage overlayImageWithImage: image];
                                                                 });
                                                               }];
}

- (void) setPatternInterval: (CGFloat) patternInterval {
  _realOverlay.patternInterval = patternInterval;
}

@end
