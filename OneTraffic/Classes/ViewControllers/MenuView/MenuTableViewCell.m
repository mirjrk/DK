//
//  MenuTableViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 4/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "MenuTableViewCell.h"
#import "Constant.h"

#define SCREEN_FACTOR SCREEN_WIDTH/375
#define BUTTON_SIZE SCREEN_FACTOR*40
#define MENU_WIDTH SCREEN_WIDTH-50

@implementation MenuTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

-(instancetype) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setUpView];
    }
    return self;
}

-(void)setUpView
{
    float menuWidth = SCREEN_WIDTH-50; //Constants not always returning real sizes
    
    self.firstButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.firstButton setBackgroundImage:[UIImage imageNamed:@"SettingsIcon"] forState:UIControlStateNormal];
    [self.firstButton setFrame:CGRectMake(menuWidth/8-BUTTON_SIZE/2, 50-BUTTON_SIZE/2, BUTTON_SIZE, BUTTON_SIZE)];
    [self.contentView addSubview:self.firstButton];
    
    self.secondButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.secondButton setBackgroundImage:[UIImage imageNamed:@"HelpIcon"] forState:UIControlStateNormal];
    [self.secondButton setFrame:CGRectMake(menuWidth/8*3-BUTTON_SIZE/2, 50-BUTTON_SIZE/2, BUTTON_SIZE, BUTTON_SIZE)];
    [self.contentView addSubview:self.secondButton];
    
    self.thirdButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.thirdButton setBackgroundImage:[UIImage imageNamed:@"TermsIcon"] forState:UIControlStateNormal];
    [self.thirdButton setFrame:CGRectMake(menuWidth/8*5-BUTTON_SIZE/2, 50-BUTTON_SIZE/2, BUTTON_SIZE, BUTTON_SIZE)];
    [self.contentView addSubview:self.thirdButton];
    
    self.forthButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.forthButton setBackgroundImage:[UIImage imageNamed:@"LogoutIcon"] forState:UIControlStateNormal];
    [self.forthButton setFrame:CGRectMake(menuWidth/8*7-BUTTON_SIZE/2, 50-BUTTON_SIZE/2, BUTTON_SIZE, BUTTON_SIZE)];
    [self.contentView addSubview:self.forthButton];
    
}

@end
