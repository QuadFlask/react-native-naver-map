#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "NMFGeometry.h"
#import "NMFOverlay.h"

NS_ASSUME_NONNULL_BEGIN

/**
 기본 폴리곤 오버레이 전역 Z 인덱스
 */
const static int NMF_POLYGON_OVERLAY_GLOBAL_Z_INDEX = -200000;

@class NMGPolygon;

/**
 지도에 도형을 나타내는 오버레이.
 */
@interface NMFPolygonOverlay : NMFOverlay

/**
 전역 z 인덱스. 두 오버레이가 겹쳐진 경우, 전역 z 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다.
 0 보다 작으면 지도 심벌에 의해 덮어지며, 0 보다 크거나 같으면 지도 심벌을 덮습니다.
 전역 Z 인덱스는 이종의 오버레이 간에도 유효합니다.
 
 기본값은 `NMF_POLYGON_OVERLAY_GLOBAL_Z_INDEX`입니다.
 */
@property (nonatomic) NSInteger globalZIndex;

/**
 면의 색상.
 
 기본값은 `UIColor.whiteColor`입니다.
 */
@property (nonatomic, copy) UIColor *fillColor;

/**
 테두리의 두께. pt 단위. `0`일 경우 테두리가 그려지지 않습니다.
 
 기본값은 `0`입니다.
 */
@property (nonatomic) NSUInteger outlineWidth;

/**
 테두리의 색상.
 
 기본값은 `UIColor.blackColor`입니다.
 */
@property (nonatomic, copy) UIColor *outlineColor;

/**
 폴리곤 객체와 색상을 지정하여 폴리곤 오버레이를 생성합니다.

 @param polygon 폴리곤 객체.
 @param fillColor 폴리곤을 채울 색상.
 @return `NMFPolygonOverlay` 객체.
 */
+ (instancetype)polygonOverlay:(NMGPolygon *)polygon fillColor:(UIColor *)fillColor;

/**
 폴리곤 객체와 색상을 지정하여 폴리곤 오버레이를 생성합니다.

 @param polygon 폴리곤 객체.
 @return `NMFPolygonOverlay` 객체.
 */
+ (instancetype)polygonOverlay:(NMGPolygon *)polygon;

/**
 폴리곤 오버레이를 생성한 이후 폴리곤을 갱신하기 위한 목적으로 사용할 수 있습니다.

 @param polygon 폴리곤 객체.
 */
- (void)updatePolygon:(NMGPolygon *)polygon;
@end

NS_ASSUME_NONNULL_END
