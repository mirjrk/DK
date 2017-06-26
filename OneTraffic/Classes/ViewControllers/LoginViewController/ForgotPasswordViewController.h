//
//  ForgotPasswordViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 9/6/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BasicViewController.h"
#import "CountriesListViewController.h"
#import "TPKeyboardAvoidingScrollView.h"


@interface ForgotPasswordViewController : BasicViewController <SelectedCountry>
@property (nonatomic, strong) UIImageView *logoImageView;
@property (nonatomic, strong) UITextField *usernameTextField;
@property (nonatomic, strong) UIButton *loginButton;
@property (nonatomic, strong) UIButton *diallCodeButton;
@property (nonatomic, strong) TPKeyboardAvoidingScrollView *scrollView;
@end
