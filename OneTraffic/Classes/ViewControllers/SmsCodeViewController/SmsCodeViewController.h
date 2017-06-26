//
//  SmsCodeViewController.h
//  OneTraffic
//
//  Created by Zesium on 12/21/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIHelper.h"
#import "BasicViewController.h"

@interface SmsCodeViewController : BasicViewController<UITextFieldDelegate>

@property(nonatomic,retain)UITextField *verificationCodeTF;
@property(nonatomic,retain)UIButton *signUpButton;
@property(nonatomic,retain)UIButton *resendCodeButton;
@property(nonatomic, strong) UILabel *codeSentLabel;
@end
