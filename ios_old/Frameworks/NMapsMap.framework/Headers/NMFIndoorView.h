//
//  NMFIndoorView.h
//  dynamic
//
//  Created by Won-Young Son on 2018. 1. 8..
//  Copyright © 2018년 NaverMap. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

/**
 하나의 실내지도를 나타내는 불변 클래스.
 */
@interface NMFIndoorView : NSObject

/**
 구역 ID.
 */
@property(nonatomic, readonly) NSString *zoneId;

/**
 층 ID.
 */
@property(nonatomic, readonly) NSString *levelId;

@property(nonatomic, readonly) NSUInteger hash;

/**
 구역 ID와 층 ID로 객체를 생성하는 생성자.
 
 @param zoneId 구역 ID.
 @param levelId 층 ID.
 */
+ (NMFIndoorView *)indoorView:(NSString *)zoneId
                  WithLevelId:(NSString *)levelId;

@end
NS_ASSUME_NONNULL_END
