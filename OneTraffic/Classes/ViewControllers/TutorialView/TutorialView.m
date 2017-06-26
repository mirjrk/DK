//
//  TutorialView.m
//  OneTraffic
//
//  Created by Stefan Andric on 1/5/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "TutorialView.h"
#import "Constant.h"

@implementation TutorialView

- (id)initWithFrame:(CGRect)frame andType:(NSString *)type {
    self = [super initWithFrame:frame];
    if (self) {
        self.type = type;
        [self createInterface];
    }
    return self;
}

- (void)createInterface {
    self.backgroundColor = DARK_BLUE_TRANSPARENT;
    //    UIView *tutorial = [[UIView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH/2-100, SCREEN_HEIGHT/2-150, 200, 300)];
    //    tutorial.backgroundColor = [UIColor whiteColor];
    //    [self addSubview:tutorial];
    
    UIImageView *tutorialImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, 70, SCREEN_WIDTH-40, SCREEN_HEIGHT-140)];
    tutorialImage.image = [UIImage imageNamed:self.type];
    tutorialImage.contentMode = UIViewContentModeScaleAspectFit;
    [self addSubview:tutorialImage];
    
    UIButton *closeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [closeButton addTarget:self action:@selector(closeButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    closeButton.frame = CGRectMake(CGRectGetMaxX(tutorialImage.frame)-60, tutorialImage.frame.origin.y, 40, 40);
    [closeButton setTitle:@"X" forState:UIControlStateNormal];
    [closeButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self addSubview:closeButton];
    
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:self.type];
    
}

- (void)closeButtonTapped:(UIButton *)sender {
    [self removeFromSuperview];
}

/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect {
 // Drawing code
 }
 */

@end
