//
//  RNNaverMapMarker.m
//
//  Created by flask on 18/04/2019.
//  Copyright © 2019 flask. All rights reserved.
//

#import "RNNaverMapMarker.h"

#import <React/RCTBridge.h>
#import <React/RCTImageLoader.h>
#import <React/RCTUtils.h>
#import <NMapsMap/NMFNaverMapView.h>
#import <NMapsMap/NMGLatLng.h>
#import <NMapsMap/NMFMarker.h>
#import <NMapsMap/NMFOverlayImage.h>

#import "RCTConvert+NMFMapView.h"

@implementation RNNaverMapMarker {
  RCTImageLoaderCancellationBlock _reloadImageCancellationBlock;
  __weak UIImageView *_iconImageView;
  UIView *_iconView;
}

- (instancetype)init
{
  if ((self = [super init])) {
    _realMarker = [NMFMarker new];
  }
  return self;
}

- (void)setCoordinate:(NMGLatLng*) coordinate {
  _realMarker.position = coordinate;
}

- (void)setRotation:(CGFloat) rotation {
  _realMarker.angle = rotation;
}

- (void)setPinColor:(UIColor *)pinColor {
  _pinColor = pinColor;
//  _realMarker.iconTintColor = pinColor;
}

- (void)setAnchor:(CGPoint)anchor {
  _anchor = anchor;
  _realMarker.anchor = anchor;
}

- (void)setMapVie:(NMFMapView*) mapView {
  _realMarker.mapView = mapView;
}

- (void)setImage:(NSString *)image
{
  _image = image;

  if (_reloadImageCancellationBlock) {
    _reloadImageCancellationBlock();
    _reloadImageCancellationBlock = nil;
  }

  NSLog(@"image : %@", image);

  if (!_image) {
    if (_iconImageView) [_iconImageView removeFromSuperview];
    return;
  }

  _reloadImageCancellationBlock = [_bridge.imageLoader loadImageWithURLRequest:[RCTConvert NSURLRequest:_image]
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
                                                                   if (self->_iconImageView) [self->_iconImageView removeFromSuperview];
                                                                   self->_realMarker.iconImage = [NMFOverlayImage overlayImageWithImage: image];
                                                                 });
                                                               }];
}

@end
