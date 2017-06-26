//
//  QuickMessageCollectionViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 6/14/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "QuickMessageCollectionViewCell.h"
#import "Constant.h"

@implementation QuickMessageCollectionViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.messageLabel.textColor = NEON_GREEN;
    // Initialization code
}

@end
