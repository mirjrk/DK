//
//  LoginViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 2/10/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "LoginViewController.h"
#import "Constant.h"
#import "UIHelper.h"
#import "RegistrationViewController.h"
#import "Communication.h"
#import "HomeViewController.h"
#import "AppDelegate.h"
#import "ForgotPasswordViewController.h"


#define SPACING 10

@interface LoginViewController ()

@end

@implementation LoginViewController

-(void)loadView {
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view= view;
    self.view.backgroundColor = DARK_BLUE;
    //    if ([[NSUserDefaults standardUserDefaults] boolForKey:@"tutorialShowed"] == NO) { //isFirstStart
    //        [self showTutorial2];
    //    }
    
    [self createInterface];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.usernameTextField.delegate = self;
    self.passwordTextField.delegate = self;
    [self.loginButton setEnabled:NO];
    [self setTextFields];
    // Do any additional setup after loading the view.
}

-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:YES];
    [self.navigationController.navigationBar setHidden:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Custom
-(void)createInterface
{
    self.scrollView = [[TPKeyboardAvoidingScrollView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    
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
    
    self.passwordTextField = [UIHelper createTextFieldWithFrame:CGRectMake(LEFT_MARGIN*2, self.usernameTextField.frame.origin.y+self.usernameTextField.frame.size.height+SPACING, SCREEN_WIDTH-LEFT_MARGIN*4, 50) placeHolder:nil delegate:nil];
    [UIHelper customizeTextField:self.passwordTextField withPlaceholder:NSLocalizedString(@"Password", nil)];
    self.passwordTextField.secureTextEntry = YES;
    [self.scrollView addSubview:self.passwordTextField];
    
    self.forgotPasswordButton = [UIHelper createSystemButtonWithFrame:CGRectMake(SCREEN_WIDTH/2-75, CGRectGetMaxY(self.passwordTextField.frame)+5, 150, 30) andTitle:NSLocalizedString(@"Forgot your password?", nil)]; //CGRectGetMaxX(self.passwordTextField.frame)-150
    [self.forgotPasswordButton addTarget:self action:@selector(forgotpasswordButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    [self.forgotPasswordButton setTitleColor:NEON_GREEN forState:UIControlStateNormal];
    [self.scrollView addSubview:self.forgotPasswordButton];
    
    self.loginButton = [UIHelper createSystemButtonWithFrame:CGRectMake(100, self.passwordTextField.frame.origin.y+self.passwordTextField.frame.size.height+5*SPACING, SCREEN_WIDTH-200, 50) andTitle:NSLocalizedString(@"LogIn", nil)];
    [self.loginButton.titleLabel setFont:[UIFont systemFontOfSize:12.0f]];
    self.loginButton.titleLabel.font=[UIFont boldSystemFontOfSize:16];
    
    [self.loginButton addTarget:self action:@selector(loginButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
    self.loginButton.layer.cornerRadius = self.loginButton.frame.size.height/2;
    self.loginButton.layer.masksToBounds = YES;
    [self.loginButton setBackgroundColor:[UIColor grayColor]];
    self.loginButton.clipsToBounds = YES;
    [self.loginButton setTitleColor:DARK_BLUE forState:UIControlStateNormal];
    [self.scrollView addSubview:self.loginButton];
    
    self.registerButton = [UIHelper createSystemButtonWithFrame:CGRectMake(self.passwordTextField.frame.origin.x, self.loginButton.frame.origin.y+self.loginButton.frame.size.height+SPACE_MARGIN, self.passwordTextField.frame.size.width, 40) andTitle:NSLocalizedString(@"Create new", nil)];
    self.registerButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentCenter;
    [self.registerButton setTitleColor:NEON_GREEN forState:UIControlStateNormal];
    [self.registerButton addTarget:self action:@selector(signUpButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
    [self.scrollView addSubview:self.registerButton];
    
    [self.scrollView setContentSize:CGSizeMake(SCREEN_WIDTH, self.registerButton.frame.origin.y+self.registerButton.frame.size.height+self.navigationController.navigationBar.frame.size.height)];
    [self.view addSubview:self.scrollView];
    
}

-(void)loginButtonPressed:(UIButton *)sender {
    [self showSpinner:YES];
    NSDictionary *parameters = @{@"phoneNumber":[NSString stringWithFormat:@"%@%@", self.diallCodeButton.currentTitle, self.usernameTextField.text],
                                 @"password":self.passwordTextField.text
                                 };
    
    
    if ([[AFNetworkReachabilityManager sharedManager] isReachable]) {
        
        if (![self.diallCodeButton.currentTitle isEqualToString:@"+"]) {
            
            [Communication logInWithParameters:parameters successBlock:^(NSDictionary *response) {
                dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                    // Do something...
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self showSpinner:NO];
                    });
                });
                
                
                if([[response objectForKey:ERROR_MESSAGE]integerValue]==0){
                    
                    NSString *userId = [response objectForKey:USER_ID];
                    NSString *token = [response objectForKey:TOKEN];
                    
                    [[NSUserDefaults standardUserDefaults] setObject:userId forKey:USER_ID];
                    [[NSUserDefaults standardUserDefaults] setObject:token forKey:TOKEN];
                    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"notFirstStart"];
                    
                    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"locationSharing"];
                    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"traffic"];
                    
                    [[NSUserDefaults standardUserDefaults] synchronize];
                    [LogicHelper getSharingLocationImages];
                    
                    AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
                    HomeViewController *vc = [[HomeViewController alloc] init];
                    UINavigationController *navigationController = [[UINavigationController alloc]initWithRootViewController:vc];
                    appDelegate.window.rootViewController = navigationController;
                    [appDelegate.window makeKeyAndVisible];
                }
                else {
                    NSString *errorCode = [[response objectForKey:ERROR_MESSAGE] stringValue];
                    UIAlertController *alert = [UIHelper showAlertControllerWithTitle:NSLocalizedString(@"Error", nil) andText:NSLocalizedString(errorCode, nil)];
                    [self presentViewController:alert animated:YES completion:nil];
                }
            } errorBlock:^(NSDictionary *error) {
                dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                    // Do something...
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self showSpinner:NO];
                    });
                });
                
                NSLog(@"%@", error);
            }];
        }
        else {
            [self showSpinner:NO];
            UIAlertController *alert = [UIHelper showAlertControllerWithTitle:@"Error" andText:@"Enter valid diall code"];
            [self presentViewController:alert animated:YES completion:nil];
        }
        
    }
    else { //There is no internet connection
        [self showSpinner:NO];
        UIAlertController *alert = [UIHelper showAlertControllerWithTitle:@"Error occurred" andText:@"There is no internet connection."];
        [self presentViewController:alert animated:YES completion:nil];
    }
}

-(void)signUpButtonPressed:(UIButton *)sender
{
    RegistrationViewController *vc = [[RegistrationViewController alloc] init];
    [self.view endEditing:YES];
    [self.navigationController pushViewController:vc animated:YES];
}
-(void)showTutorial2
{
    
    _ghView = [[GHWalkThroughView alloc] initWithFrame:self.view.bounds];
    [_ghView setDataSource:self];
    UILabel* welcomeLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 50)];
    welcomeLabel.text = nil;
    welcomeLabel.font = [UIFont systemFontOfSize:35];
    welcomeLabel.textColor = [UIColor whiteColor];
    welcomeLabel.textAlignment = NSTextAlignmentCenter;
    
    [_ghView setFloatingHeaderView:welcomeLabel];
    [self.ghView setWalkThroughDirection:GHWalkThroughViewDirectionHorizontal];
    
    self.descStrings = [NSArray arrayWithObjects:@"Whole route on one screen. All put together. See lineup event chronologically.", nil];
    
    [self.ghView showInView:self.navigationController.view animateDuration:0.0];
    
    [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"tutorialShowed"];
    [[NSUserDefaults standardUserDefaults]synchronize];
    
}

#pragma mark - GHDataSource

-(NSInteger) numberOfPages
{
    return 1;
}

- (void) configurePage:(GHWalkThroughPageCell *)cell atIndex:(NSInteger)index
{
    
    if(index==0){
        
        UILabel* welcomeLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 300, 50)];
        welcomeLabel.text = NSLocalizedString(@"SetSomething", nil);
        welcomeLabel.font = [UIFont systemFontOfSize:35];
        welcomeLabel.textColor = [UIColor whiteColor];
        welcomeLabel.textAlignment = NSTextAlignmentCenter;
        
        [_ghView setFloatingHeaderView:welcomeLabel];
    }
    
    cell.title = [NSString stringWithFormat:@"Shorter route %ld", index+1];
    cell.titleImage = [UIImage imageNamed:[NSString stringWithFormat:@"tutorial%ld", index+1]];
    cell.desc = [self.descStrings objectAtIndex:index];
}

- (UIImage*) bgImageforPage:(NSInteger)index
{
    NSString* imageName =[NSString stringWithFormat:@"backgroundImage"];
    UIImage* image = [UIImage imageNamed:imageName];
    return image;
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    // Workaround for the jumping text bug.
    [textField resignFirstResponder];
    [textField layoutIfNeeded];
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.view endEditing:YES];
    
    return YES;
}

- (void)setTextFields
{
    [self.usernameTextField addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventEditingChanged];
    [self.passwordTextField addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventEditingChanged];
}

-(void)textFieldChanged:(UITextField *)sender
{
    [self.loginButton setEnabled:(self.usernameTextField.hasText && self.passwordTextField.hasText)];
    if (self.loginButton.enabled == YES) {
        [self.loginButton setBackgroundColor:NEON_GREEN];
    }
    else {
        [self.loginButton setBackgroundColor:[UIColor grayColor]];
    }
}
-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [super touchesBegan:touches withEvent:event];
    
    [self.view endEditing:YES]; //YES ignores any textfield refusal to resign
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

- (void)forgotpasswordButtonTapped:(UIButton *)sender
{
    ForgotPasswordViewController *vc = [ForgotPasswordViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}
@end
