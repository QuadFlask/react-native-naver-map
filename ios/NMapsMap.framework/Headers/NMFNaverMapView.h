#import <UIKit/UIKit.h>

@class NMFMapView;
@protocol NMFMapViewDelegate;

/**
 위치 추적 모드를 나타내는 열거형.
 */
typedef NS_ENUM(NSUInteger, NMFMyPositionMode) {
    /**
     위치 추적을 사용하지 않는 모드. `NMFLocationOverlay`는 움직이지 않습니다.
     */
    NMFMyPositionDisabled = 0,
    
    /**
     위치는 추적하지만 지도는 움직이지 않는 모드. `NMFLocationOverlay`가 사용자의 위치를 따라 움직이나 지도는
     움직이지 않습니다.
     */
    NMFMyPositionNormal = 1,
    
    /**
     위치를 추적하면서 카메라도 따라 움직이는 모드. `NMFLocationOverlay`와 카메라의 좌표가 사용자의 위치를 따라
     움직입니다. API나 제스처를 사용해 지도를 임의로 움직일 경우 모드가 `NMFMyPositionNormal`로 바뀝니다.
     */
    NMFMyPositionDirection = 2,

    /**
     위치를 추적하면서 카메라의 좌표와 헤딩도 따라 움직이는 모드. `NMFLocationOverlay`와 카메라의 좌표,
     헤딩이 사용자의 위치, 사용자가 바라보고 있는 방향을 따라 움직입니다. API나 제스처를 사용해 지도를 임의로 움직일
     경우 모드가 `NMFMyPositionNormal`로 바뀝니다.
     */
    NMFMyPositionCompass = 3
};

/**
 지도의 컨트롤을 내장한 지도 뷰 클래스. 컨트롤을 비활성화하더라도 API를 호출하면 여전히 카메라를 움직일 수 있습니다.
 */
@interface NMFNaverMapView : UIView

/**
 지도 뷰 객체.
 
 @see `NMFMapView`
 */
@property(nonatomic, readonly, nonnull) NMFMapView *mapView;

/**
 지도 뷰에 관련된 업데이트 및 비동기 작업의 결과를 알려주는 델리게이트.
 
 @see `NMFMapViewDelegate`
 */
@property(nonatomic, weak, nullable) IBOutlet id<NMFMapViewDelegate> delegate;

/**
 나침반이 활성화되어 있는지 여부.
 
 기본값은 `YES`입니다.
 */
@property(nonatomic, assign) IBInspectable BOOL showCompass;

/**
 축척 바가 활성화되어 있는지 여부.
 
 기본값은 `YES`입니다.
 */
@property(nonatomic, assign) IBInspectable BOOL showScaleBar;

/**
 줌 컨트롤이 활성화되어 있는지 여부.
 
 기본값은 `YES`입니다.
 */
@property(nonatomic, assign) IBInspectable BOOL showZoomControls;

/**
 실내지도 패널이 활성화되어 있는지 여부.
 
 기본값은 `NO`입니다.
 */
@property(nonatomic, assign) IBInspectable BOOL showIndoorLevelPicker;

/**
 현 위치 버튼이 활성화되어 있는지 여부.
 
 기본값은 `NO`입니다.
 */
@property(nonatomic, assign) IBInspectable BOOL showLocationButton;

/**
 위치 추적 모드.
 
 @see `NMFMyPositionMode`
 */
@property(nonatomic) NMFMyPositionMode positionMode;

/**
 현재 지도의 스냅숏을 `UIImage`로 촬영합니다.
 
 @param complete 지도 스냅숏이 촬영되면 실행되는 블록 메서드.
 */
- (void)takeSnapShot:(void (^_Nullable)(UIImage * _Nonnull))complete;

@end
