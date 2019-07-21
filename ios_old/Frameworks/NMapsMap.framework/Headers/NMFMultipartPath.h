#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "NMFOverlay.h"
#import "NMFPathColor.h"

@class NMGLineString;
@class NMFOverlayImage;

NS_ASSUME_NONNULL_BEGIN

/**
 기본 멀티 파트 경로선 전역 Z 인덱스
 */
const static int NMF_MULTI_PART_PATH_OVERLAY_GLOBAL_Z_INDEX = -100000;

/**
 경로선을 여러 파트로 나누어 각각 다른 색상을 부여할 수 있는 특수한 `NMFPath`. 다양한 색상으로 구성된
 경로선을 나타내려면 여러 개의 `NMFPath`를 사용하는 것보다 이 클래스를 사용하는 것이 효율적입니다.
 `MultipartPathOverlay`는 좌표열 파트의 목록와 색상 파트의 목록으로 구성되며, `0`번째 좌표열 파트에
 `0`번째 색상 파트의 색상이 적용됩니다. 따라서 좌표열 파트와 색상 파트의 크기가 동일해야 합니다.
 */
@interface NMFMultipartPath : NMFOverlay

/**
 전역 z 인덱스. 두 오버레이가 겹쳐진 경우, 전역 z 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다.
 0 보다 작으면 지도 심벌에 의해 덮어지며, 0 보다 크거나 같으면 지도 심벌을 덮습니다.
 전역 Z 인덱스는 이종의 오버레이 간에도 유효합니다.
 
 기본값은 `NMF_MULTI_PART_PATH_OVERLAY_GLOBAL_Z_INDEX`입니다.
 */
@property (nonatomic) NSInteger globalZIndex;

/**
 좌표열 파트의 목록. 목록의 크기가 `1` 이상, 각 파트의 크기가 `2` 이상이어야 합니다.
 */
@property (nonatomic, strong) NSArray<NMGLineString *>* coordParts;

/**
 색상 파트의 목록. 목록의 크기가 `1` 이상, 각 파트의 크기가 `2` 이상이어야 합니다.
 */
@property (nonatomic, strong) NSArray<NMFPathColor *> *colorParts;

/**
 두께. pt 단위.
 
 기본값은 `5`입니다.
 */
@property (nonatomic) CGFloat width;

/**
 테두리 두께. pt 단위.
 
 기본값은 `1`입니다.
 */
@property (nonatomic) CGFloat outlineWidth;

/**
 진척률. `0`~`1`로 지정합니다. 경로선에서 `0`~`progress`의 선형은 지나온 경로로 간주되어
 `passedColor`와 `passedOutlineColor`가 사용됩니다.
 `progress`~`1`의 선형은 지나갈 경로로 간주되어 `color`와 `passedColor`가 사용됩니다.
 
 기본값은 `0`입니다.
 */
@property (nonatomic) double progress;

/**
 패턴 이미지의 간격. pt 단위. `0`일 경우 패턴을 표시하지 않습니다.
 
 기본값은 `25`입니다.
 */
@property (nonatomic) NSUInteger patternInterval;

/**
 패턴 이미지. 패턴 이미지의 크기가 경로선의 두께보다 클 경우 경로선의 두께에 맞게 축소됩니다.
 `nil`일 경우 패턴을 표시하지 않습니다.
 
 기본값은 `nil`입니다.
 */
@property (nonatomic, strong, nullable) NMFOverlayImage *patternIcon;

/**
 좌표열 파트의 목록을 지정하여 NMFMultipartPath 객체를 생성합니다.
 
 ```
 NMFMultipartPath *mPath = [NMFMultipartPath multipartPathWithCoordParts:@[
     NMGLatLngMake(37.20, 127.051),
     NMGLatLngMake(37.21, 127.052),
     NMGLatLngMake(37.22, 127.053)
 ]];
 mPath.mapView = mapView;
 ```
 
 @param coordParts 좌표열 파트의 목록.
 @return `NMFMultipartPath` 객체.
 */
+ (instancetype)multipartPathWithCoordParts:(NSArray *)coordParts;
@end

NS_ASSUME_NONNULL_END

