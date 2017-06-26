//
//  SmsCodeViewController.m
//  OneTraffic
//
//  Created by Zesium on 12/21/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import "SmsCodeViewController.h"
#import "Constant.h"
#import "UIHelper.h"
#import "LogicHelper.h"
#import "Communication.h"
#import "SettingsViewController.h"
#import "ProfileViewController.h"
#import "AFNetworkReachabilityManager.h"

@interface SmsCodeViewController ()

@end

@implementation SmsCodeViewController


-(void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view= view;
    self.view.backgroundColor = DARK_BLUE;
    [self createInterface];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTextFields];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
-(void)createInterface
{
    
    self.codeSentLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 80, SCREEN_WIDTH, 60)];
    self.codeSentLabel.textAlignment = NSTextAlignmentCenter;
    self.codeSentLabel.text = NSLocalizedString(@"SentSms", nil);
    self.codeSentLabel.textColor = [UIColor whiteColor];
    self.codeSentLabel.numberOfLines = 0;
    [self.view addSubview:self.codeSentLabel];
    
    
    CGRect frame = CGRectMake(100, CGRectGetMaxY(self.codeSentLabel.frame), SCREEN_WIDTH-200, 50);
    self.verificationCodeTF= [UIHelper createTextFieldWithFrame:frame placeHolder:NSLocalizedString(@"Verification Code", nil) delegate:self];
    [UIHelper customizeTextField:self.verificationCodeTF withPlaceholder:@"Enter code"];
    [self.verificationCodeTF setKeyboardType:UIKeyboardTypeNumberPad];
    
    UIToolbar *numberToolbar = [UIHelper createToolbarWithDoneButtonAndView:self.view];
    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc]initWithTitle:NSLocalizedString(@"Done", nil) style:UIBarButtonItemStyleDone target:self action:@selector(doneClicked:)];
    [doneButton setTintColor:DARK_BLUE];
    numberToolbar.items = [NSArray arrayWithObjects: [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil], doneButton, [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],nil];
    [numberToolbar sizeToFit];
    self.verificationCodeTF.inputAccessoryView = numberToolbar;
    [self.view addSubview:self.verificationCodeTF];
    
    self.signUpButton = [UIButton buttonWithType:UIButtonTypeCustom];
    self.signUpButton.frame=CGRectMake(100,self.verificationCodeTF.frame.origin.y+self.verificationCodeTF.frame.size.height+3*10, SCREEN_WIDTH-200, 50);
    
    [self.signUpButton setBackgroundColor:NEON_GREEN];
    self.signUpButton.clipsToBounds = YES;
    self.signUpButton.layer.cornerRadius = 25.0f;
    self.signUpButton.layer.masksToBounds = YES;
    [self.signUpButton addTarget:self action:@selector(signUpAction) forControlEvents:UIControlEventTouchUpInside];
    
    self.signUpButton.titleLabel.font=[UIFont boldSystemFontOfSize:16];
    [self.signUpButton setTitle:NSLocalizedString(@"Next", ) forState:UIControlStateNormal];
    [self.signUpButton setTitleColor:DARK_BLUE  forState:UIControlStateNormal];
    [self.view addSubview:self.signUpButton];
    
    self.resendCodeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    self.resendCodeButton.frame=CGRectMake(100,self.signUpButton.frame.origin.y+self.signUpButton.frame.size.height+3*10, SCREEN_WIDTH-200, 50);
    self.resendCodeButton.clipsToBounds = YES;
    self.resendCodeButton.layer.cornerRadius = 25.0f;
    self.resendCodeButton.layer.masksToBounds = YES;
    [self.resendCodeButton setBackgroundColor:[UIColor whiteColor]];
    self.resendCodeButton.clipsToBounds = YES;
    
    [self.resendCodeButton addTarget:self action:@selector(resendCode) forControlEvents:UIControlEventTouchUpInside];
    self.resendCodeButton.titleLabel.font=[UIFont systemFontOfSize:16];
    [self.resendCodeButton setTitle:NSLocalizedString(@"ResendCode", nil) forState:UIControlStateNormal];
    [self.resendCodeButton setTitleColor:DARK_BLUE forState:UIControlStateNormal];
    [self.view addSubview:self.resendCodeButton];
    
}


-(void)doneClicked:(id)sender
{
    [self.verificationCodeTF resignFirstResponder];
}

-(BOOL) inputIsValid {
    // trim the ends of our email + username
    self.verificationCodeTF.text = [LogicHelper trimmedString:self.verificationCodeTF.text];
    
    
    NSString *errorTitle = @"Error";
    
    if ( IS_EMPTY_STRING(self.verificationCodeTF.text)){
        [self showSpinner:NO];
        UIAlertController *alert =   [UIHelper showAlertControllerWithTitle:errorTitle andText:@"Code is required - please enter a valid code."];
        [self presentViewController:alert animated:YES completion:nil];
        return NO;
        
    }
    
    if ( [self.verificationCodeTF.text rangeOfString:@" "].location != NSNotFound ) {
        [self showSpinner:NO];
        UIAlertController *alert =  [UIHelper showAlertControllerWithTitle:errorTitle andText:@"Please enter valid code"];
        [self presentViewController:alert animated:YES completion:nil];
        return NO;
        
    }
    if ( [self.verificationCodeTF.text length] != 4) {
        [self showSpinner:NO];
        UIAlertController *alert =   [UIHelper showAlertControllerWithTitle:errorTitle andText:@"Code must be 4 characters long"];
        [self presentViewController:alert animated:YES completion:nil];
        return NO;
    }
    
    
    return YES;
}


-(void)signUpAction{
    
    NSString *userID = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    
    [self showSpinner:YES];
    
    if(self.view.isFirstResponder)
        [self.view endEditing:YES];
    
    if ( ![self inputIsValid] ) { return; }
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc]init];
    
    [parameters setObject:userID forKey:@"userId"];
    [parameters setObject:self.verificationCodeTF.text forKey:@"smsCode"];
    
    if ([[AFNetworkReachabilityManager sharedManager] isReachable]) {
        [Communication sendSMSVerificationCodeWithParameters:parameters successfulBlock:^(NSDictionary *response) {
            
            NSLog(@"response %@", response);
            
            //errorMessage
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self showSpinner:NO];
                });
            });
            
            if([[response objectForKey:ERROR_MESSAGE]integerValue]==0){
                
                [[NSUserDefaults standardUserDefaults] setObject:[response objectForKey:USER_ID] forKey:USER_ID];
                [[NSUserDefaults standardUserDefaults] setObject:[response objectForKey:USER_STATUS] forKey:USER_STATUS];
                [[NSUserDefaults standardUserDefaults] setObject:[response objectForKey:TOKEN] forKey:TOKEN];
                [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"locationSharing"];
                [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"traffic"];
                
                [[NSUserDefaults standardUserDefaults] synchronize];
                
                
                ProfileViewController *pVc= [[ProfileViewController alloc] init];
                pVc.isInitialRegistration = YES;
                [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"notFirstStart"];
                [self.navigationController pushViewController:pVc animated:YES];
                
            }else{
                UIAlertController *alert = [UIHelper showAlertControllerWithTitle:@"Error" andText:NSLocalizedString([[response objectForKey:ERROR_MESSAGE]stringValue],nil)];
                [self presentViewController:alert animated:YES completion:nil];
            }
            
        } errorBlock:^(NSDictionary *error) {
            
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self showSpinner:NO];
                });
            });
            
            
        }];
    }
    else { //There is no internet connection
        [self showSpinner:NO];
        UIAlertController *alert =  [UIHelper showAlertControllerWithTitle:@"Error occurred" andText:@"There is no internet connection."];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
}
-(void)resendCode{
    
    NSString *userID = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    
    [self showSpinner:YES];
    
    if(self.view.isFirstResponder)
        [self.view endEditing:YES];
    
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc]init];
    
    [parameters setObject:userID forKey:@"userId"];
    
    if ([[AFNetworkReachabilityManager sharedManager] isReachable]) {
        [Communication resendSMSVerificationCodeWithParameters:parameters successfulBlock:^(NSDictionary *response) {
            
            NSLog(@"response %@", response);
            
            //errorMessage
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self showSpinner:NO];
                });
            });
            
            if([[response objectForKey:ERROR_MESSAGE]integerValue]!=0){
                
                
                UIAlertController *alert =  [UIHelper showAlertControllerWithTitle:@"Error" andText:NSLocalizedString([[response objectForKey:ERROR_MESSAGE]stringValue],nil)];
                [self presentViewController:alert animated:YES completion:nil];
            }
            
        } errorBlock:^(NSDictionary *error) {
            
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self showSpinner:NO];
                });
            });
            
            
        }];
    }
    else { //There is no internet connection
        [self showSpinner:NO];
        UIAlertController *alert =  [UIHelper showAlertControllerWithTitle:@"Error occurred" andText:@"There is no internet connection."];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
}



#pragma mark - Textfield delegates

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    NSLog(@"textFieldShouldBeginEditing");
    
    return YES;
}

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    NSLog(@"textFieldDidBeginEditing");
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if(range.length + range.location > textField.text.length)
    {
        return NO;
    }
    NSUInteger newLength = [textField.text length] + [string length] - range.length;
    return newLength <= 4;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    NSLog(@"textFieldShouldReturn:");
    
    [textField resignFirstResponder];
    
    return YES;
}

#pragma mark - TextField dependecy on button disabling
- (void)setTextFields
{
    [self.signUpButton setEnabled:NO];
    [self.verificationCodeTF addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventEditingChanged];
}

-(void)textFieldChanged:(UITextField *)sender
{
    [self.signUpButton setEnabled:(self.verificationCodeTF.hasText)];
}



@end
