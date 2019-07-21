#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import "NMapsGeometry.h"

NS_INLINE CGFloat ScreenScaleFactor() {
    static dispatch_once_t onceToken;
    static CGFloat screenFactor;
    
    dispatch_once(&onceToken, ^{
        screenFactor = [UIScreen instancesRespondToSelector:@selector(nativeScale)] ?
                       [[UIScreen mainScreen] nativeScale] : [[UIScreen mainScreen] scale];
    });
    
    return screenFactor;
};

/**
 지오메트리 관련 유틸리티를 제공하는 클래스.
 */
@interface NMFGeometryUtils : NSObject

/**
 `NMGLatLng` 배열로 구성된 경로선에서 대상 좌표에 가장 근접한 지점의 진척률을 반환합니다.
 
 @param latLngs `NMGLatLng` 배열로 구성된 경로선.
 @param targetLatLng 대상 좌표.
 @return 진척률.
 */
+(double)progressWithLatLngs:(NSArray<NMGLatLng *> * _Nonnull)latLngs targetLatLng:(NMGLatLng * _Nonnull)targetLatLng;

/**
 `NMGLineString` 배열로 구성된 경로선에서 대상 좌표에 가장 근접한 지점의 진척률을 반환합니다.
 
 @param lineStrings `NMGLineString` 배열로 구성된 경로선.
 @param targetLatLng 대상 좌표.
 @return 진척률.
 */
+(double)progressWithLineStrings:(NSArray<NMGLineString *> * _Nonnull)lineStrings targetLatLng:(NMGLatLng * _Nonnull)targetLatLng;

@end
