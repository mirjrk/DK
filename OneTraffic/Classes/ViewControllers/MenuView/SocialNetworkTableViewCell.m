//
//  SocialNetworkTableViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 3/1/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "SocialNetworkTableViewCell.h"
#import "Constant.h"
#define SCREEN_FACTOR SCREEN_WIDTH/375 

@implementation SocialNetworkTableViewCell

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
    NSLog(@"%f", SCREEN_WIDTH);
    self.emailImage = [[UIImageView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH-50-SCREEN_FACTOR*50, 10, SCREEN_FACTOR*40, SCREEN_FACTOR*40)];
    [self.emailImage setImage:[UIImage imageNamed:@"socialEmail"]];
    self.emailImage.userInteractionEnabled = YES;
    UITapGestureRecognizer *emailTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(emailTapped)];
    emailTap.numberOfTapsRequired = 1;
    [self.emailImage addGestureRecognizer:emailTap];
    [self.contentView addSubview:self.emailImage];
    
    self.twitterImage = [[UIImageView alloc] initWithFrame:CGRectMake(self.emailImage.frame.origin.x- SCREEN_FACTOR*60, 10, SCREEN_FACTOR*40, SCREEN_FACTOR*40)];
    [self.twitterImage setImage:[UIImage imageNamed:@"socialTwitter"]];
    self.twitterImage.userInteractionEnabled = YES;
    UITapGestureRecognizer *twitterTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(twitterTapped)];
    twitterTap.numberOfTapsRequired = 1;
    [self.twitterImage addGestureRecognizer:twitterTap];
    
    [self.contentView addSubview:self.twitterImage];
    
    self.facebookImage = [[UIImageView alloc] initWithFrame:CGRectMake(self.twitterImage.frame.origin.x-SCREEN_FACTOR*60, 10, SCREEN_FACTOR*40, SCREEN_FACTOR*40)];
    [self.facebookImage setImage:[UIImage imageNamed:@"socialFacebook"]];
    [self.facebookImage setUserInteractionEnabled:YES];
    UITapGestureRecognizer *facebookTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(facebookTapped)];
    facebookTap.numberOfTapsRequired = 1;
    [self.facebookImage addGestureRecognizer:facebookTap];
    [self.contentView addSubview:self.facebookImage];
}

-(void)emailTapped
{
    [self.delegate emailSelected];
}

-(void)facebookTapped
{
    [self.delegate facebookSelected];
}

-(void)twitterTapped
{
    [self.delegate twitterSelected];
}
@end
