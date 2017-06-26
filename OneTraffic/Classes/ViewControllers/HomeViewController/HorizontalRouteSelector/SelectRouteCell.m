//
//  SelectRouteCell.m
//  OneTraffic
//
//  Created by Zesium on 4/15/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "SelectRouteCell.h"
#import "Constant.h"

@implementation SelectRouteCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
- (instancetype) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if ( self ) {
        self.backgroundColor=[UIColor clearColor];
        [self setUpView];
    }
    return self;
}

-(void)setUpView
{
    
    self.destinationImageView = [[UIImageView alloc] initWithFrame:CGRectMake (20, 18.5, 16, 16)];
    self.destinationImageView.userInteractionEnabled=YES;
    
    [self addSubview:  self.destinationImageView];

    
    self.destinationLabel= [[UILabel alloc]  initWithFrame:CGRectMake (44, 16.5, SCREEN_WIDTH-88, 20)];
    self.destinationLabel.textColor=NEON_GREEN;
    self.destinationLabel.font= [UIFont systemFontOfSize:16.0];
    self.destinationLabel.text=@"";
    [self addSubview:  self.destinationLabel];
  
}
@end
