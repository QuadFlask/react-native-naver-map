#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "NMFGeometry.h"
#import "NMFOverlay.h"

@class NMFOverlayImage;

NS_ASSUME_NONNULL_BEGIN

/**
 기본 경로선 전역 Z 인덱스
 */
const static int NMF_PATH_OVERLAY_GLOBAL_Z_INDEX = -100000;

/**
 지도에 경로선을 나타내는 오버레이. 하나의 선을 나타낸다는 측면에서는 `NMFPolylineOverlay`와 유사하나, 다음과
 같이 경로선에 특화된 특징이 있습니다.
 - 테두리와 패턴 이미지를 적용할 수 있습니다.
 - 지도를 기울이더라도 두께가 일정하게 유지됩니다.
 - 자기교차(self-intersection)가 일어나더라도 테두리, 패턴 이미지가 자연스럽게 나타납니다.
 - 진척률을 지정할 수 있으며, 지나온/지나갈 경로에 각각 다른 색상과 테두리를을 지정할 수 있습니다.
 - 점선 패턴, 끝 지점/연결점의 모양은 지정할 수 없습니다.
 */
@interface NMFPath : NMFOverlay

/**
 전역 z 인덱스. 두 오버레이가 겹쳐진 경우, 전역 z 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다.
 0 보다 작으면 지도 심벌에 의해 덮어지며, 0 보다 크거나 같으면 지도 심벌을 덮습니다.
 전역 Z 인덱스는 이종의 오버레이 간에도 유효합니다.
 
 기본값은 `NMF_PATH_OVERLAY_GLOBAL_Z_INDEX`입니다.
 */
@property (nonatomic) NSInteger globalZIndex;

/**
 경로선의 색상. 경로선의 색상은 반투명일 수 없으며, 완전히 투명하거나 완전히 불투명해야 합니다. 색상의
 알파가 `0`이 아닌 경우 완전히 불투명한 것으로 간주됩니다. 색상이 투명할 경우 테두리도 그려지지 않습니다.
 
 기본값은 `UIColor.whiteColor`입니다.
 */
@property (nonatomic, strong) UIColor *color;

/**
 경로선의 테두리 색상. 경로선의 테두리 색상은 반투명일 수 없으며, 완전히 투명하거나 완전히 불투명해야
 합니다. 색상의 알파가 `0`이 아닌 경우 완전히 불투명한 것으로 간주됩니다.
 
 기본값은 `UIColor.blackColor`입니다.
 */
@property (nonatomic, strong) UIColor *outlineColor;

/**
 지나온 경로선의 색상. 지나온 경로선의 색상은 반투명일 수 없으며, 완전히 투명하거나 완전히 불투명해야
 합니다. 색상의 알파가 `0`이 아닌 경우 완전히 불투명한 것으로 간주됩니다. 색상이 투명할 경우 테두리도
 그려지지 않습니다.
 
 기본값은 `UIColor.whiteColor`입니다.
 */
@property (nonatomic, strong) UIColor *passedColor;

/**
 지나온 경로선의 테두리 색상. 지나온 경로선의 테두리 색상은 반투명일 수 없으며, 완전히 투명하거나
 완전히 불투명해야 합니다. 색상의 알파가 `0`이 아닌 경우 완전히 불투명한 것으로 간주됩니다. 색상이 투명할
 경우 테두리도 그려지지 않습니다.
 
 기본값은 `UIColor.blackColor`입니다.
 */
@property (nonatomic, strong) UIColor *passedOutlineColor;

/**
 두께. pt 단위.
 
 기본값은 `5`입니다.
 */
@property (nonatomic) CGFloat width;

/**
 테두리 두께. pt 단위. `0`일 경우 테두리가 그려지지 않습니다.
 
 기본값은 `1`입니다.
 */
@property (nonatomic) CGFloat outlineWidth;

/**
 좌표열. `points`의 크기는 `2` 이상이어야 합니다.
 */
@property (nonatomic, strong) NSArray<NMGLatLng *> *points;

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
 */
@property (nonatomic, strong, nullable) NMFOverlayImage *patternIcon;

/**
 좌표열을 지정하여 경로선 오버레이를 생성합니다. `coords`의 크기는 `2` 이상이어야 합니다.
 
 사용 예시
 ```
 NMFLatLng *p1 = [[NMFLatLng alloc]initWithLat:37.20 lng:127.051]];
 NMFLatLng *p2 = [[NMFLatLng alloc]initWithLat:37.21 lng:127.052]];
 NMFLatLng *p3 = [[NMFLatLng alloc]initWithLat:37.22 lng:127.053]];
 NMFPath *route = [NMFPath pathWithPoints:@[p1, p2, p3]];
 route.mapView = mapView;
 ```
 
 @param coords 좌표열.
 @return `NMFPath` 객체.
 */
+ (instancetype)pathWithPoints:(NSArray *)coords;
@end

NS_ASSUME_NONNULL_END

