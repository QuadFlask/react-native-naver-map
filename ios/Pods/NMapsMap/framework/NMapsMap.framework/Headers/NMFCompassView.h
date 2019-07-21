#import <UIKit/UIKit.h>

#define COMPASSVIEW_DEFAULT_SIZE        50
#define COMPASSVIEW_DEFAULT_HEADING     0.0
#define COMPASSVIEW_DEFAULT_TILTING     0.0

NS_ASSUME_NONNULL_BEGIN

@class NMFMapView;
IB_DESIGNABLE @interface NMFCompassView : UIImageView
@property (nonatomic, weak, nullable) NMFMapView *mapView;
@property (nonatomic) BOOL tiltEnabled;

+ (instancetype)compassView;
- (void)updateCompass:(double)heading withTilt:(double)tilt;

@end

NS_ASSUME_NONNULL_END
