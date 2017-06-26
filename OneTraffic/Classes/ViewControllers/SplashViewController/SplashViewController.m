//
//  SplashViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 1/13/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "SplashViewController.h"
#import "Constant.h"
#import "HomeViewController.h"
#import "RegistrationViewController.h"
#import "AppDelegate.h"
#import "LoginViewController.h"
#import "Communication.h"
#import "PasswordStrength.h"
#import "LogicHelper.h"


@interface SplashViewController ()

@end

@implementation SplashViewController


-(void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view= view;
    
    [self createInterface];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    //Preload images to file
    if ([[NSUserDefaults standardUserDefaults] boolForKey:@"notFirstStart"] == YES) {
        [LogicHelper getSharingLocationImages];
    }
    self.splashLogo.image = [UIImage animatedImageNamed:@"Splash-"
                                               duration:3.0f];
    [self performSelector:@selector(changeViewController) withObject:nil afterDelay:3.0f];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    [self.navigationController.navigationBar setHidden:YES];
}

- (void)createInterface
{
    self.backgroundImage = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.backgroundImage.backgroundColor = DARK_BLUE;
    [self.view addSubview:self.backgroundImage];
    
    self.splashLogo = [[UIImageView alloc]initWithFrame:CGRectMake(SCREEN_WIDTH/2-60, SCREEN_HEIGHT/2-120, 120, 120)];
    [self.view addSubview:self.splashLogo];
    
    self.logo = [[UIImageView alloc]initWithFrame:CGRectMake(SCREEN_WIDTH/2-100, self.splashLogo.frame.origin.y+self.splashLogo.frame.size.height, 200, 93)];
    [self.logo setImage:[UIImage imageNamed:@"Logo"]];
    [self.view addSubview:self.logo];
    
    self.sponsorView = [[SponsorMechanismView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(self.logo.frame)+10, SCREEN_WIDTH, 140) isSplash:YES];
    if (IS_IPHONE_4_OR_LESS) {
        //        self.sponsorView.sponsorText.frame = CGRectMake(0, CGRectGetMaxY(self.sponsorView.sponsorImage.frame), SCREEN_WIDTH, 50);
    }
    
    [self.view addSubview:self.sponsorView];
}

/*!
 * @discussion This method is main logic for presenting user screen. If user is already logged he will be leaded to Home.
 * @param notFirstStart
 * @return /
 */
- (void)changeViewController
{
    
    //    If this is not first start, lead user to home screen.
    if ([[NSUserDefaults standardUserDefaults] boolForKey:@"notFirstStart"] == YES) {
        AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
        HomeViewController *vc = [[HomeViewController alloc] init];
        UINavigationController *navigationController = [[UINavigationController alloc]initWithRootViewController:vc];
        appDelegate.window.rootViewController = navigationController;
        [appDelegate.window makeKeyAndVisible];
    }
    
    //  Lead user to login, or signup if he's new
    else {
        AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
        LoginViewController *vc = [[LoginViewController alloc] init];
        UINavigationController *navigationController = [[UINavigationController alloc]initWithRootViewController:vc];
        appDelegate.window.rootViewController = navigationController;
        [appDelegate.window makeKeyAndVisible];
        [[NSUserDefaults standardUserDefaults] setObject:@"km" forKey:@"metrics"];
        [[NSUserDefaults standardUserDefaults] setObject:@"24" forKey:@"timeFormat"];
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"alertSound"];
        
    }
    
}

@end
