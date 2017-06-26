//
//  CountriesTableViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 4/20/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "CountriesTableViewCell.h"
#import "Constant.h"

@implementation CountriesTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (instancetype) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if ( self ) {
        [self setUpView];
    }
    return self;
}

-(void)setUpView
{
    self.countryNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(5, 5, SCREEN_WIDTH-70, 34)];
    self.countryNameLabel.textColor = NEON_GREEN;
    [self addSubview:self.countryNameLabel];
    
    self.countryCodeLabel = [[UILabel alloc] initWithFrame:CGRectMake(SCREEN_WIDTH-70, 5, 65, 34)];
    self.countryCodeLabel.textAlignment = NSTextAlignmentRight;
    self.countryCodeLabel.textColor = NEON_GREEN;
    [self addSubview:self.countryCodeLabel];
}

@end
