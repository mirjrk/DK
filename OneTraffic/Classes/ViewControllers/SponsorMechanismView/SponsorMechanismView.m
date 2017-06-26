//
//  SponsorMechanismView.m
//  OneTraffic
//
//  Created by Stefan Andric on 7/6/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "SponsorMechanismView.h"
#import "Constant.h"

@implementation SponsorMechanismView


- (id)initWithFrame:(CGRect)frame isSplash:(BOOL)isSplash {
    
    self = [super initWithFrame:frame];
    if (self) {
        self.isSplash = isSplash;
        [self createInterface];
    }
    return self;
}

-(void)createInterface
{
    if (self.isSplash == YES) {
        self.backgroundColor = [UIColor clearColor];
        
        self.sponsorImage = [[UIImageView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH/2-30, 20, 60, 60)];
    }
    else {
        self.backgroundColor = DARK_BLUE;
        [self setupTimer];
        
        [UIView animateWithDuration:0.5
                              delay:0
                            options: UIViewAnimationOptionCurveEaseOut
                         animations:^{
                             self.frame = CGRectMake(0, 0, SCREEN_WIDTH, 150);
                             self.sponsorImage = [[UIImageView alloc] initWithFrame:CGRectMake(20, 20, 60, 60)];
                         }
                         completion:^(BOOL finished){
                             
                         }];
        
    }
    
    
    [self addSubview:self.sponsorImage];
    
    if ([[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfSponsorDisplayed"] == (id)[NSNull null] || [[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfSponsorDisplayed"] integerValue] == 0) {
        [self.sponsorImage setImage:[UIImage imageNamed:@"if.png"]];
        int incrementValue = 0;
        [self incrementValueLogic:incrementValue];
    }
    else if([[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfSponsorDisplayed"] integerValue] == 1) {
        [self.sponsorImage setImage:[UIImage imageNamed:@"circleK"]];
        int incrementValue = 1;
        [self incrementValueLogic:incrementValue];
    }
    else if([[[NSUserDefaults standardUserDefaults] valueForKey:@"numberOfSponsorDisplayed"] integerValue] == 2) {
        [self.sponsorImage setImage:[UIImage imageNamed:@"if.png"]];
        int incrementValue = 2;
        [self incrementValueLogic:incrementValue];
    }
    
    
    
}

-(void)setupTimer
{
    [NSTimer scheduledTimerWithTimeInterval:2.0
                                     target:self
                                   selector:@selector(removeFromWindow)
                                   userInfo:nil
                                    repeats:NO];
}

-(void)removeFromWindow
{
    [UIView animateWithDuration:0.5
                          delay:0
                        options: UIViewAnimationOptionCurveEaseOut
                     animations:^{
                         self.frame = CGRectMake(0, -150, SCREEN_WIDTH, 100);
                     }
                     completion:^(BOOL finished){
                         [self removeFromSuperview];
                     }];
    
}

-(void)incrementValueLogic:(int)incrementValue
{
    if (incrementValue == 2) {
        incrementValue = 0;
    }
    else {
        incrementValue++;
    }
    
    [[NSUserDefaults standardUserDefaults] setInteger:incrementValue forKey:@"numberOfSponsorDisplayed"];
}

@end
