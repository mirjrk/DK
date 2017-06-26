//
//  CommunicatorTableViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 3/2/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "CommunicatorTableViewCell.h"
#import "Constant.h"

@implementation CommunicatorTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    self.backgroundColor = [UIColor clearColor];
    
    self.userImageView.clipsToBounds = YES;
    self.userImageView.layer.cornerRadius = self.userImageView.frame.size.height/2;
    self.userImageView.layer.borderColor = NEON_GREEN.CGColor;
    self.userImageView.layer.borderWidth = 1.0f;
    
    self.nameLabel.textColor = NEON_GREEN;
    self.statusLabel.textColor = NEON_GREEN;
    
    self.typeOfRequestLabel.textColor = [UIColor whiteColor];
    self.dateLabel.textColor = [UIColor whiteColor];
    

}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}

@end
