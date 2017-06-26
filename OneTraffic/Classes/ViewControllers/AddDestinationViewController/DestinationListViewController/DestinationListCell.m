//
//  DestinationListCell.m
//  OneTraffic
//
//  Created by Zesium on 2/17/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "DestinationListCell.h"
#import "Constant.h"

@implementation DestinationListCell

- (void)awakeFromNib {
    [super awakeFromNib];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}
- (instancetype) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if ( self ) {
        [self setUpView];
    }
    return self;
}

-(void)setUpView
{
    
    self.destImageView = [[UIImageView alloc]initWithFrame:CGRectMake(20, 15, 50, 50)];
    [self.destImageView setImage:[UIImage imageNamed:@"Home"]];
    [self.contentView addSubview:self.destImageView];
    
    self.nameLabel = [[UILabel alloc]initWithFrame:CGRectMake(80, 15, SCREEN_WIDTH-50-80, 50)];
    [self.nameLabel setTextColor:[UIColor whiteColor]];
    [self.nameLabel setFont:[UIFont systemFontOfSize:22.0f]];
    self.nameLabel.textAlignment = NSTextAlignmentLeft;
    [self.contentView addSubview:self.nameLabel];
    
    
    
}

@end
