//
//  AppDelegate.m
//  OneTraffic
//
//  Created by Stefan Andric on 12/21/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import "AppDelegate.h"
#import "RegistrationViewController.h"
#import "HomeViewController.h"
#import "SplashViewController.h"
#import "AFNetworkReachabilityManager.h"
#import "AFNetworkActivityIndicatorManager.h"
#import "TestFairy.h"
#import "NotificationView.h"
#import "SponsorMechanismView.h"

@interface AppDelegate ()

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    //    [self setUpTestFairy];
    [self customizeGlobally];
    [self startMonitoring];
    [application setIdleTimerDisabled: YES]; //This is to disable screen dim and lock
    //As there are certain chance that status bar can be extended, we need to make this kind of dependency
    CGFloat height = [UIApplication sharedApplication].statusBarFrame.size.height;
    if (height == 20) {
        self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    }
    else {
        CGRect frame = [[UIScreen mainScreen] bounds];
        frame.size.height = frame.size.height - height +20;
        frame.origin.y = height-20;
        self.window = [[UIWindow alloc] initWithFrame:frame];
    }
    
    //Setting Splash as main
    SplashViewController *vc = [[SplashViewController alloc]init]; //was registration
    UINavigationController *navController= [[UINavigationController alloc] initWithRootViewController:vc];
    self.window.rootViewController = navController;
    [self.window makeKeyAndVisible];
    
    ///User will enter the app, if he had any badge on the icon of the app, set it to 0.
    [application setApplicationIconBadgeNumber:0];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

//Track status bar height
- (void)application:(UIApplication *)application willChangeStatusBarFrame:(CGRect)newStatusBarFrame {
    CGFloat height = newStatusBarFrame.size.height;
    if (height == 20) {
        self.window.frame = [[UIScreen mainScreen] bounds];
        
    }
    else {
        CGRect frame = [[UIScreen mainScreen] bounds];
        frame.size.height = frame.size.height - height +20;
        frame.origin.y = height-20;
        self.window.frame = frame;
    }
}



- (void)applicationWillEnterForeground:(UIApplication *)application {
    [application setApplicationIconBadgeNumber:0];
    SponsorMechanismView *aView = [[SponsorMechanismView alloc]initWithFrame:CGRectMake(0, -150, SCREEN_WIDTH, 150) isSplash:NO];
    [application.keyWindow addSubview:aView];
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark - Custom methods
/*!
 * @discussion This method is setting up TestFairy with token and unique device name
 * @param /
 * @return /
 */
- (void)setUpTestFairy {
    [TestFairy begin:@"eed98d6001b268fd763a1ea55bb9b387c95eff28"];
    [TestFairy identify:[[UIDevice currentDevice] name]];
}

/*!
 * @discussion This method is customizing globally some values, e.g. picker color.
 * @param /
 * @return /
 */
- (void)customizeGlobally {
    [[UINavigationBar appearance] setTranslucent:NO];
    [[UINavigationBar appearance] setBarTintColor:DARK_BLUE];
    [[UINavigationBar appearance] setTintColor:[UIColor whiteColor]];
    
    [[UIPickerView appearance] setBackgroundColor:DARK_BLUE];
    [[UIDatePicker appearance] setBackgroundColor:DARK_BLUE];
    [[UITextField appearance] setKeyboardAppearance:UIKeyboardAppearanceDark];
}

/*!
 * @discussion This method is triggering AFNetworking to start monitoring for network in purpose of checking internet availiability.
 * @param /
 * @return /
 */
- (void)startMonitoring {
    [AFNetworkActivityIndicatorManager sharedManager].enabled = YES;
    [[AFNetworkReachabilityManager sharedManager] startMonitoring];
}
@end
