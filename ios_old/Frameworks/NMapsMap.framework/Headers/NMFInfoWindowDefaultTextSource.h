//
//  NMFInfoWindowDefaultTextSource.h
//  dynamic
//
//  Created by Won-Young Son on 29/10/2018.
//  Copyright © 2018 NaverMap. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIkit.h>

@protocol NMFOverlayImageDataSource;

NS_ASSUME_NONNULL_BEGIN

@interface NMFInfoWindowDefaultTextSource : NSObject <NMFOverlayImageDataSource>

+ (instancetype)dataSource;

@property(nonatomic, readwrite) NSString *title;

@end

NS_ASSUME_NONNULL_END
