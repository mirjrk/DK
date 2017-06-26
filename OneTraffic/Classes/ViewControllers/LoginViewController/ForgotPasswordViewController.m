//
//  ForgotPasswordViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 9/6/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "ForgotPasswordViewController.h"
#import "Constant.h"
#import "UIHelper.h"
#import "LogicHelper.h"
#import "Communication.h"

@interface ForgotPasswordViewController ()

@end

@implementation ForgotPasswordViewController

-(void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view= view;
    self.view.backgroundColor = DARK_BLUE;
    
    [self createInterface];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.loginButton.enabled = NO;
    [self.usernameTextField addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventEditingChanged];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)createInterface
{
    self.scrollView = [[TPKeyboardAvoidingScrollView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    [self.view addSubview:self.scrollView];
    self.logoImageView= [[UIImageView alloc] initWithFrame:CGRectMake(50, 60, SCREEN_WIDTH-100, 100)];
    self.logoImageView.contentMode= UIViewContentModeScaleAspectFit;
    [self.logoImageView setImage:[UIImage imageNamed:@"Logo"]];
    [self.scrollView addSubview:self.logoImageView];
    
    self.diallCodeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.diallCodeButton setFrame:CGRectMake(LEFT_MARGIN*2, 200, 70, 50)];
    [self.diallCodeButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.diallCodeButton setBackgroundColor:[UIColor clearColor]];
    [self.diallCodeButton setTitle:[NSString stringWithFormat:@"+%@", [LogicHelper getCountryCode]] forState:UIControlStateNormal];
    [self.diallCodeButton addTarget:self action:@selector(countryCodeTapped:) forControlEvents:UIControlEventTouchUpInside];
    self.diallCodeButton.layer.borderColor = NEON_GREEN.CGColor;
    self.diallCodeButton.layer.borderWidth = 1.5f;
    self.diallCodeButton.layer.cornerRadius = 25.0f;
    
    self.usernameTextField = [UIHelper createTextFieldWithFrame:CGRectMake(LEFT_MARGIN*2, 200, SCREEN_WIDTH-LEFT_MARGIN*4, 50) placeHolder:nil delegate:nil];
    [UIHelper customizeTextField:self.usernameTextField withPlaceholder:NSLocalizedString(@"Mobile phone", nil)];
    self.usernameTextField.keyboardType = UIKeyboardTypePhonePad;
    self.usernameTextField.leftViewMode = UITextFieldViewModeAlways;
    self.usernameTextField.leftView = self.diallCodeButton;
    UIView *clearViewForAligning = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 70, 50)];
    self.usernameTextField.rightViewMode = UITextFieldViewModeAlways;
    self.usernameTextField.rightView = clearViewForAligning;
    [self.scrollView addSubview:self.usernameTextField];
    
    
    self.loginButton = [UIHelper createSystemButtonWithFrame:CGRectMake(100, self.usernameTextField.frame.origin.y+self.usernameTextField.frame.size.height+30, SCREEN_WIDTH-200, 50) andTitle:NSLocalizedString(@"Reset", nil)];
    [self.loginButton.titleLabel setFont:[UIFont systemFontOfSize:12.0f]];
    self.loginButton.titleLabel.font=[UIFont boldSystemFontOfSize:16];
    
    [self.loginButton addTarget:self action:@selector(loginButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
    self.loginButton.layer.cornerRadius = self.loginButton.frame.size.height/2;
    self.loginButton.layer.masksToBounds = YES;
    [self.loginButton setBackgroundColor:[UIColor grayColor]];
    self.loginButton.clipsToBounds = YES;
    [self.loginButton setTitleColor:DARK_BLUE forState:UIControlStateNormal];
    [self.scrollView addSubview:self.loginButton];
    
}

-(void)countryCodeTapped:(UIButton *)sender
{
    CountriesListViewController *vc = [[CountriesListViewController alloc] init];
    vc.delegate = self;
    [self.view endEditing:YES];
    [self.navigationController pushViewController:vc animated:YES];
}

-(void)countryCode:(NSString *)code
{
    NSString *clear = [code stringByReplacingOccurrencesOfString:@" " withString:@""];
    
    [self.diallCodeButton setTitle:clear forState:UIControlStateNormal];
}

-(void)loginButtonPressed:(UIButton *)sender
{
    NSDictionary *dict = @{@"phoneNumber": [NSString stringWithFormat:@"%@%@", self.diallCodeButton.currentTitle, self.usernameTextField.text]};
    [self showSpinner:YES];
    [Communication forgetPassword:dict withSuccessBlock:^(NSDictionary *response) {
        [self showSpinner:NO];
        NSInteger errorMessage = [[response objectForKey:@"errorMessage"]integerValue];
        if (errorMessage == 0) {
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"Done", nil) message:@"Email with instructions has been sent to you." preferredStyle:UIAlertControllerStyleAlert];
            UIAlertAction *ok = [UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [self.navigationController popViewControllerAnimated:YES];
            }];
            [alert addAction:ok];
            [self presentViewController:alert animated:YES completion:nil];
        }
        else {
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"Done", nil) message:@"Number is not in database!" preferredStyle:UIAlertControllerStyleAlert];
            UIAlertAction *ok = [UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                [self.navigationController popViewControllerAnimated:YES];
            }];
            [alert addAction:ok];
            [self presentViewController:alert animated:YES completion:nil];
        }
        
        
    } errorBlock:^(NSDictionary *error) {
        [self showSpinner:NO];
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"Error", nil) message:NSLocalizedString(@"UnknownError", nil) preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *ok = [UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            [self.navigationController popViewControllerAnimated:YES];
        }];
        [alert addAction:ok];
        [self presentViewController:alert animated:YES completion:nil];
    }];
}

-(void)textFieldChanged:(UITextField *)sender
{
    [self.loginButton setEnabled:self.usernameTextField.hasText];
    if (self.loginButton.enabled == YES) {
        [self.loginButton setBackgroundColor:NEON_GREEN];
    }
    else {
        [self.loginButton setBackgroundColor:[UIColor grayColor]];
    }
}


/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
