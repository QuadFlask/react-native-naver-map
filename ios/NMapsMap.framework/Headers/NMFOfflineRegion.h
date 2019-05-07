#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

/**
 An object conforming to the `NMFOfflineRegion` protocol determines which
 resources are required by an `NMFOfflinePack` object. At present, only
 instances of `NMFTilePyramidOfflineRegion` may be used as `NMFOfflinePack`
 regions, but additional conforming implementations may be added in the future.
 */
@protocol NMFOfflineRegion <NSObject>

@end

NS_ASSUME_NONNULL_END
