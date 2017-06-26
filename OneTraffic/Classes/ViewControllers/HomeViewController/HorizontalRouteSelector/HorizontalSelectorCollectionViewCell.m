//
//  HorisontalSelectorCollectionViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 12/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "HorizontalSelectorCollectionViewCell.h"

#import "Constant.h"
@implementation HorizontalSelectorCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.backroundView.layer.borderColor = NEON_GREEN.CGColor;
    self.backgroundColor = DARK_BLUE;
    self.selectedIndicatorView.backgroundColor = [UIColor clearColor];
    self.selectedIndicatorView.alpha = 0.3;
}

@end
