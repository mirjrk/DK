//
//  EventTableViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 4/1/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "EventTableViewCell.h"
#import "Constant.h"

#define IMAGE_LABEL_SPACING 5
#define borderThickness 1.0f
@implementation EventTableViewCell

- (void)awakeFromNib
{
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

- (instancetype) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setUpView];
    }
    return self;
}

/*
-(void)setUpView
{
    self.commentatorImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, 10, 40, 40)];
    [self.commentatorImage setImage:[UIImage imageNamed:@"Logo"]];
    self.commentatorImage.contentMode = UIViewContentModeScaleAspectFit;
    self.commentatorImage.layer.cornerRadius = 20;
    self.commentatorImage.layer.masksToBounds = YES;
    self.commentatorImage.layer.borderWidth=0.7;
    self.commentatorImage.layer.borderColor = NEON_GREEN.CGColor;
    [self.contentView addSubview:self.commentatorImage];

    self.commentatorLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.commentatorImage.frame.origin.x+self.commentatorImage.frame.size.width+IMAGE_LABEL_SPACING, self.commentatorImage.frame.origin.y, SCREEN_WIDTH-self.commentatorImage.frame.size.width-self.commentatorImage.frame.origin.x, 30)];
    self.commentatorLabel.textColor = NEON_GREEN;
    [self.contentView addSubview:self.commentatorLabel];

    self.commentTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.commentatorImage.frame.origin.x+self.commentatorImage.frame.size.width+IMAGE_LABEL_SPACING, self.commentatorLabel.frame.origin.y+self.commentatorLabel.frame.size.height, SCREEN_WIDTH-self.commentatorImage.frame.size.width-self.commentatorImage.frame.origin.x, 60)];
    self.commentTextLabel.textColor = [UIColor whiteColor];
    self.commentTextLabel.numberOfLines = 0;
    self.commentTextLabel.font = [UIFont systemFontOfSize:16.0f];
//    self.commentText.userInteractionEnabled = NO;

    [self.contentView addSubview:self.commentTextLabel];

    self.appropriateButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.appropriateButton setImage:[UIImage imageNamed:@"appropriateButton"] forState:UIControlStateNormal];
    [self.appropriateButton setFrame:CGRectMake(self.commentatorImage.frame.origin.x+self.commentatorImage.frame.size.width+IMAGE_LABEL_SPACING, self.commentTextLabel.frame.origin.y+self.commentTextLabel.frame.size.height+IMAGE_LABEL_SPACING, 30, 30)];
    [self.contentView addSubview:self.appropriateButton];

    self.approriateLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.appropriateButton.frame.origin.x+self.appropriateButton.frame.size.width+IMAGE_LABEL_SPACING, self.appropriateButton.frame.origin.y, 60, 30)];
    [self.approriateLabel setTextColor:[UIColor whiteColor]];
    self.approriateLabel.font = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]];
    self.approriateLabel.text = @"(0)";
    [self.contentView addSubview:self.approriateLabel];

    self.inappropriateLabel = [[UILabel alloc] initWithFrame:CGRectMake(SCREEN_WIDTH-70, self.approriateLabel.frame.origin.y, 60, 30)];
    [self.inappropriateLabel setTextColor:[UIColor whiteColor]];
    self.inappropriateLabel.font = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]];
    self.inappropriateLabel.text = @"(0)";
    [self.contentView addSubview:self.inappropriateLabel];

    self.inappropriateButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.inappropriateButton setImage:[UIImage imageNamed:@"inappropriateButton"] forState:UIControlStateNormal];
    [self.inappropriateButton setFrame:CGRectMake(self.inappropriateLabel.frame.origin.x-40, self.inappropriateLabel.frame.origin.y, 30, 30)];
    [self.contentView addSubview:self.inappropriateButton];
}
*/

-(void)setUpView
{
    self.contentViewComment = [[UIView alloc] initWithFrame:CGRectMake(20, 74, SCREEN_WIDTH-40, 60)];
    self.contentViewComment.backgroundColor = [UIColor clearColor];
    self.contentViewComment.layer.cornerRadius = 30;
    self.contentViewComment.layer.masksToBounds = YES;
    self.contentViewComment.layer.borderColor = NEON_GREEN.CGColor;
    self.contentViewComment.layer.borderWidth = borderThickness;
    [self addSubview:self.contentViewComment];
    
    self.commentatorLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, self.contentViewComment.frame.size.width, 30)];
    self.commentatorLabel.textColor = NEON_GREEN;
    self.commentatorLabel.textAlignment = NSTextAlignmentCenter;
    [self.contentViewComment addSubview:self.commentatorLabel];
    
    self.commentTextLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 30, self.contentViewComment.frame.size.width-20, 22)];
    self.commentTextLabel.textColor = [UIColor whiteColor];
    self.commentTextLabel.numberOfLines = 0;
    self.commentTextLabel.font = [UIFont systemFontOfSize:16.0f];
    self.commentTextLabel.textAlignment = NSTextAlignmentCenter;
    [self.contentViewComment addSubview:self.commentTextLabel];
    
    self.commentatorImage = [[UIImageView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH/2-30, 22, 60, 60)];
    [self.commentatorImage setImage:[UIImage imageNamed:@"Logo"]];
    self.commentatorImage.contentMode = UIViewContentModeScaleAspectFill;
    self.commentatorImage.layer.cornerRadius = 30;
    self.commentatorImage.layer.masksToBounds = YES;
    self.commentatorImage.layer.borderWidth = borderThickness;
    self.commentatorImage.layer.borderColor = NEON_GREEN.CGColor;
    [self addSubview:self.commentatorImage];
    
    self.appropriateButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.appropriateButton setImage:[UIImage imageNamed:@"appropriateButton"] forState:UIControlStateNormal];
    [self.appropriateButton setFrame:CGRectMake(SCREEN_WIDTH/4-15, self.contentViewComment.frame.origin.y-32, 30, 30)];
    [self.contentView addSubview:self.appropriateButton];

    self.inappropriateButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.inappropriateButton setImage:[UIImage imageNamed:@"inappropriateButton"] forState:UIControlStateNormal];
    [self.inappropriateButton setFrame:CGRectMake(SCREEN_WIDTH/4*3-15, self.contentViewComment.frame.origin.y-32, 30, 30)];
    [self.contentView addSubview:self.inappropriateButton];
    
}

@end
