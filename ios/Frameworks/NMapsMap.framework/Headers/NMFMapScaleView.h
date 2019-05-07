//
//  NMFMapScaleView.h
//  ios
//
//  Created by Won-Young Son on 2017. 5. 2..
//  Copyright © 2017년 NaverMap. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface NMFMapScaleView : UIView

@property(nonatomic, weak) IBOutlet UIImageView *scaleBar;
@property(nonatomic, weak) IBOutlet UILabel *scaleLabel;
@property(nonatomic, weak) IBOutlet NSLayoutConstraint *scaleBarWidthConstraint;

- (void)displayScaleWith:(double)metersPerPoint;

@end
