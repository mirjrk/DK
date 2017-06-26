//
//  SplashViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 1/13/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "LogicHelper.h"
#import "SponsorMechanismView.h"

@interface SplashViewController : UIViewController
@property(nonatomic, retain) UIImageView *backgroundImage;
@property(nonatomic, retain) UIImageView *splashLogo;
@property(nonatomic, retain) UIImageView *logo;
@property(nonatomic, strong) SponsorMechanismView *sponsorView;
@end
