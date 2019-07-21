#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <UIKit/UIKit.h>
#import "NMFOverlay.h"
#import "NMFMarkerConstants.h"
#import "NMFInfoWindow.h"

@class NMGLatLng;
@class NMFOverlayImage;

NS_ASSUME_NONNULL_BEGIN

/**
 너비 또는 높이가 자동임을 나타내는 상수. 너비 또는 높이가 자동일 경우 아이콘 이미지의 크기에 맞춰집니다.
 */
const static int NMF_MARKER_SIZE_AUTO = 0;

/**
 기본 마커 전역 Z 인덱스
 */
const static int NMF_MARKER_GLOBAL_Z_INDEX = 200000;

/**
 파란색 마커 이미지.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_BLUE;
/**
 회색 마커 이미지.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_GRAY;
/**
 초록색 마커 이미지.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_GREEN;
/**
 밝은파란색 마커 이미지.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_LIGHTBLUE;
/**
 핑크색 마커 이미지.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_PINK;
/**
 빨간색 마커 이미지.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_RED;
/**
 노란색 마커 이미지.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_YELLOW;
/**
 검은색 마커 이미지. 색상을 덧입히기에 적합합니다.
 */
extern const NMFOverlayImage *NMF_MARKER_IMAGE_BLACK;

/**
 아이콘과 캡션을 이용해 지도 위의 한 지점을 표시하는 오버레이.
 */
@interface NMFMarker : NMFOverlay

/**
 전역 z 인덱스. 두 오버레이가 겹쳐진 경우, 전역 z 인덱스가 큰 오버레이가 작은 오버레이를 덮습니다.
 0 보다 작으면 지도 심벌에 의해 덮어지며, 0 보다 크거나 같으면 지도 심벌을 덮습니다.
 전역 Z 인덱스는 이종의 오버레이 간에도 유효합니다.
 
 기본값은 `NMF_MARKER_GLOBAL_Z_INDEX`입니다.
 */
@property (nonatomic) NSInteger globalZIndex;

/**
 사용자가 임의로 지정할 수 있는 태그. 마커를 그루핑하거나 구분하기 위한 목적으로 사용할 수 있습니다.
 */
@property (nonatomic) NSUInteger tag;

/**
 아이콘.
 */
@property (nonatomic, strong) NMFOverlayImage *iconImage;

/**
 아이콘에 덧입힐 색상. 덧입힐 색상을 지정하면 덧입힐 색상이 아이콘 이미지의 색상과 가산 혼합됩니다. 단, 덧입힐 색상의
 알파는 무시됩니다.
 
 기본값은 `UIColor.clearColor`입니다.
 */
@property (nonatomic, strong) UIColor *iconTintColor;

/**
 아이콘의 너비. pt 단위. `NMF_MARKER_SIZE_AUTO`일 경우 이미지의 너비를 따릅니다.
 */
@property (nonatomic) CGFloat width;

/**
 아이콘의 높이. pt 단위. `NMF_MARKER_SIZE_AUTO`일 경우 이미지의 높이를 따릅니다.
 */
@property (nonatomic) CGFloat height;

/**
 아이콘에 원근 효과를 적용할지 여부. 원근 효과를 적용할 경우 가까운 아이콘은 크게, 먼 아이콘은 작게 표시됩니다.
 
 기본값은 `NO`입니다.
 */
@property (nonatomic) BOOL iconPerspectiveEnabled;

/**
 캡션에 원근 효과를 적용할지 여부. 원근 효과를 적용할 경우 가까운 캡션은 크게, 먼 캡션은 작게 표시됩니다.
 
 기본값은 `NO`입니다.
 */
@property (nonatomic) BOOL captionPerspectiveEnabled;

/**
 마커를 평평하게 설정할지 여부. 마커가 평평할 경우 지도가 회전하거나 기울어지면 마커 이미지도 함께 회전하거나
 기울어집니다. 단, 마커가 평평하더라도 이미지의 크기는 항상 동일하게 유지됩니다.
 
 기본값은 `NO`입니다.
 */
@property (nonatomic, getter=isFlat) BOOL flat;

 /**
 마커와 지도 심벌이 겹칠 경우 지도 심벌을 숨길지 여부.
  
 기본값은 `NO`입니다.
 */
@property (nonatomic) BOOL isHideCollidedSymbols;

/**
 마커와 다른 마커가 겹칠 경우 다른 마커를 숨길지 여부.
 
 기본값은 `NO`입니다.
 */
@property (nonatomic) BOOL isHideCollidedMarkers;

/**
 마커와 다른 마커의 캡션이 겹칠 경우 다른 마커의 캡션을 숨길지 여부.
 
 기본값은 `NO`입니다.
 */
@property (nonatomic) BOOL isHideCollidedCaptions;

/**
 마커가 `isHideCollidedMarkers`이 `YES`인 다른 마커와 겹치더라도 아이콘을 무조건 표시할지 여부.
 
 기본값은 `NO`입니다.
 */
@property (nonatomic) BOOL isForceShowIcon;

/**
 좌표. 마커를 지도에 추가하기 전에 반드시 이 속성에 값을 지정해야 합니다.
 */
@property (nonatomic, strong) NMGLatLng *position;

/**
 불투명도. `0`일 경우 완전히 투명, `1`일 경우
 완전히 불투명함을 의미합니다.
 
 기본값은 `1`입니다.
 */
@property (nonatomic) CGFloat alpha;

/**
 앵커. 앵커는 아이콘 이미지에서 기준이 되는 지점을 의미합니다. 앵커로 지정된 지점이 정보 창의 좌표에
 위치합니다. 값의 범위는 `(0, 0)`~`(1, 1)`이며, `(0, 0)`일 경우 이미지의 왼쪽 위,
 `(1, 1)`일 경우 이미지의 오른쪽 아래를 의미합니다.
 
 기본값은 `(0.5, 1)`입니다.
 */
@property (nonatomic) CGPoint anchor;

/**
 아이콘의 각도. 도 단위. 각도를 지정하면 아이콘이 해당 각도만큼 시계 방향으로 회전합니다.
 
 기본값은 `0`입니다.
 */
@property (nonatomic) CGFloat angle;

/**
 캡션 텍스트. 빈 문자열일 경우 캡션이 표시되지 않습니다.
 
 기본값은 빈 문자열입니다.
 */
@property (nonatomic, copy) NSString *captionText;

/**
 캡션의 텍스트 색상.
 
 기본값은 `UIColor.blackColor`입니다.
 */
@property (nonatomic, strong) UIColor *captionColor;

/**
 캡션의 외곽 색상.
 
 기본값은 `UIColor.whiteColor`입니다.
 */
@property (nonatomic, strong) UIColor *captionHaloColor;

/**
 캡션의 텍스트 크기. pt 단위.
 
 기본값은 `12`입니다.
 */
@property(nonatomic) CGFloat captionTextSize;

/**
 캡션의 너비. pt 단위. 지정할 경우 한 줄의 너비가 희망 너비를 초과하는 캡션 텍스트가 자동으로 개행됩니다.
 자동 개행은 어절 단위로 이루어지므로, 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수 있습니다.
 `0`일 경우 너비를 제한하지 않습니다.
 
 기본값은 `0`입니다.
 */
@property(nonatomic) CGFloat captionRequestedWidth;

/**
 보조 캡션의 텍스트. 보조 캡션은 주 캡션의 아래에 나타납니다. 빈 문자열일 경우 보조 캡션이 표시되지 않습니다.
 
 기본값은 빈 문자열입니다.
 */
@property (nonatomic, copy) NSString *subCaptionText;

/**
 보조 캡션의 텍스트 색상.
 
 기본값은 `UIColor.blackColor`입니다.
 */
@property (nonatomic, strong) UIColor *subCaptionColor;

/**
 보조 캡션의 외곽 색상.
 
 기본값은 `UIColor.whiteColor`입니다.
 */
@property (nonatomic, strong) UIColor *subCaptionHaloColor;

/**
 보조 캡션의 텍스트 크기. pt 단위.
 
 기본값은 `12`입니다.
 */
@property(nonatomic) CGFloat subCaptionTextSize;

/**
 보조 캡션의 너비. pt 단위. 지정할 경우 한 줄의 너비가 희망 너비를 초과하는 캡션 텍스트가 자동으로 개행됩니다.
 자동 개행은 어절 단위로 이루어지므로, 하나의 어절이 길 경우 캡션의 너비가 희망 너비를 초과할 수 있습니다.
 `0`일 경우 너비를 제한하지 않습니다.
 
 기본값은 `0`입니다.
 */
@property(nonatomic) CGFloat subCaptionRequestedWidth;

/**
 캡션 아이콘의 정렬 방향.
 
 기본값은 `NMFAlignBottom`입니다.
 */
@property(nonatomic) NMFAlign captionAlign;

/**
 아이콘과 캡션 간의 여백.
 
 기본값은 `0`입니다.
 */
@property(nonatomic) CGFloat captionOffset;

/**
 마커에 열려 있는 정보 창.
*/
@property (nonatomic, strong, readonly) NMFInfoWindow *infoWindow;

/**
 위치를 지정하여 마커를 생성합니다.

 @param position 좌표.
 @return `NMFMarker` 객체.
 */
+(instancetype)markerWithPosition:(NMGLatLng *)position;

/**
 위치와 아이콘을 지정하여 마커를 생성합니다.
 
 @param position 좌표.
 @param iconImage 아이콘.
 @return `NMFMarker` 객체.
 */
+(instancetype)markerWithPosition:(NMGLatLng *)position iconImage:(NMFOverlayImage *)iconImage;

@end

NS_ASSUME_NONNULL_END
