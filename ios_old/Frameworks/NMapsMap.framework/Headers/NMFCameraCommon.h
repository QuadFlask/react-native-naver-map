#ifndef NMFCameraCommon_h
#define NMFCameraCommon_h

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "NMFGeometry.h"

#define MINIMUM_TILT 0
#define MAXIMUM_TILT 60

/**
 기본 애니메이션 시간을 의미하는 상수. 애니메이션 시간이 이 값으로 지정되었을 경우
 `NMFMapView.animationDuration`으로 지정된 시간이 적용됩니다.
 */
const static NSTimeInterval NMF_DEFAULT_ANIMATION_DURATION = -1;

#define DEFAULT_INVALID_HEADING     -1
#define DEFAULT_INVALID_ZOOM        -1
#define DEFAULT_INVALID_TILT        -1

#define CLAMP(x, low, high) ({\
__typeof__(x) __x = (x); \
__typeof__(low) __low = (low);\
__typeof__(high) __high = (high);\
__x > __high ? __high : (__x < __low ? __low : __x);\
})

#define WRAP(value, min, max) \
(fmod((fmod((value - min), (max - min)) + (max - min)), (max - min)) + min)

#endif /* NMFCameraCommon_h */


