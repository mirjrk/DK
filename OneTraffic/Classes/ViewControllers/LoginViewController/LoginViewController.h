//
//  LoginViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 2/10/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GHWalkThroughView.h"
#import "TPKeyboardAvoidingScrollView.h"
#import "CountriesListViewController.h"

@interface LoginViewController : UIViewController <GHWalkThroughViewDataSource, UITextFieldDelegate, SelectedCountry>

@property (nonatomic, strong) UITextField *usernameTextField;
@property (nonatomic, strong) UITextField *passwordTextField;
@property (nonatomic, strong) UIButton *loginButton;
@property (nonatomic, strong) UIImageView *logoImageView;
@property (nonatomic, strong) UIButton *registerButton;
@property (nonatomic, strong) GHWalkThroughView* ghView;
@property (nonatomic, strong) NSArray* descStrings;
@property (nonatomic, strong) NSArray* titleStrings;
@property (nonatomic, strong) TPKeyboardAvoidingScrollView *scrollView;
@property (nonatomic, strong) UIButton *diallCodeButton;
@property (nonatomic, strong) UIButton *forgotPasswordButton;
@end
