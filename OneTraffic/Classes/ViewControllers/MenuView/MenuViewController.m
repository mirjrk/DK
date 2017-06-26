//
//  MenuViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 2/29/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "MenuViewController.h"
#import "Constant.h"
#import "UIHelper.h"
#import "UIImageView+AFNetworking.h"
#import "MenuTableViewCell.h"

#define SLIDE_TIMING .25
@interface MenuViewController ()

@end

@implementation MenuViewController


- (void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view= view;
    self.view.backgroundColor= [UIColor clearColor] ;
    
    [self createInterface];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.menuTableView.separatorStyle = UITableViewCellSeparatorStyleNone; //Will set manually
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) createInterface
{
    self.clearView= [[UIView alloc] initWithFrame:CGRectMake(0, 0, 50, SCREEN_HEIGHT)];
    self.clearView.backgroundColor= [UIColor clearColor];
    [self.view addSubview:self.clearView];
    
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(close)];
    [self.clearView addGestureRecognizer:tapGestureRecognizer];
    
    self.coloredView= [[UIView alloc] initWithFrame:CGRectMake(50, 0, SCREEN_WIDTH-50, SCREEN_HEIGHT)];
    self.coloredView.backgroundColor= DARK_BLUE_TRANSPARENT;
    [self.view addSubview:self.coloredView];
    
    self.userImageView = [[UIImageView alloc] initWithFrame:CGRectMake (15,  50 , 50, 50)];
    self.userImageView.layer.cornerRadius = self.userImageView.frame.size.width / 2;
    self.userImageView.clipsToBounds = YES;
    [self.userImageView setUserInteractionEnabled:YES];
    UITapGestureRecognizer *userClickedImage =  [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userClickedImageOrLabel)];
    [userClickedImage setNumberOfTapsRequired:1];
    [self.userImageView addGestureRecognizer:userClickedImage];
    
    //self.userImageView.userInteractionEnabled=YES;
    [ self.coloredView addSubview:self.userImageView ];
    
  
    
    self.nameLabel= [[UILabel alloc] initWithFrame:CGRectMake(self.userImageView.frame.origin.x+self.userImageView.frame.size.width+15, self.userImageView.frame.origin.y+self.userImageView.frame.size.width/2-10, 200, 20)];
    
    if(!IS_EMPTY_STRING([[NSUserDefaults standardUserDefaults] objectForKey:USER_NAME])){
        
    [self.nameLabel setText:[NSString stringWithFormat: @"%@",[[NSUserDefaults standardUserDefaults] objectForKey:USER_NAME]]];
    }
        
    [self.nameLabel setTextColor:[UIColor whiteColor]];
    [self.nameLabel setUserInteractionEnabled:YES];
    UITapGestureRecognizer *labelTapped = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(userClickedImageOrLabel)];
    [labelTapped setNumberOfTapsRequired:1];
    [self.nameLabel addGestureRecognizer:labelTapped];
    [ self.coloredView addSubview:self.nameLabel];
    
    
    NSDictionary *parameters = @{TOKEN:[[NSUserDefaults standardUserDefaults]objectForKey:TOKEN]};
    [Communication getProfileWithParameters:parameters successfulBlock:^(NSDictionary *response) {
        NSDictionary *profileDict = [response objectForKey:@"profile"];
        
        
        if ([[NSUserDefaults standardUserDefaults] objectForKey:USER_NAME] == nil || [[NSUserDefaults standardUserDefaults] objectForKey:USER_NAME] == (id)[NSNull null]) {
            if ([profileDict objectForKey:@"name"] != (id)[NSNull null] && [profileDict objectForKey:@"surname"] != (id)[NSNull null]) {
                self.nameLabel.text = [NSString stringWithFormat:@"%@ %@",[profileDict objectForKey:@"name"], [profileDict objectForKey:@"surname"] ];
                NSString *nameString = [NSString stringWithFormat:@"%@ %@", [profileDict objectForKey:@"name"], [profileDict objectForKey:@"surname"]];
                [[NSUserDefaults standardUserDefaults] setObject:nameString forKey:USER_NAME];
            }
        }
        
        
        if ([profileDict objectForKey:@"pictureUrl"] != nil && [profileDict objectForKey:@"pictureUrl"] != (id)[NSNull null]) {
            
            [self.userImageView setImageWithURL:[NSURL URLWithString:[profileDict objectForKey:@"pictureUrl"]] placeholderImage:[UIImage imageNamed:@"photo_icon"]];
        }
    } errorBlock:^(NSDictionary *error) {
        
    }];
    
    self.dataArray = @[@"", @"Traffic", @"Location sharing"];
    self.menuTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, self.userImageView.frame.origin.y+self.userImageView.frame.size.height+5, self.view.frame.size.width-50,  self.view.frame.size.height-105)];
    self.menuTableView.delegate = self;
    self.menuTableView.dataSource = self;
    self.menuTableView.backgroundColor=[UIColor clearColor];
    self.menuTableView.bounces=NO;
    [self.coloredView addSubview:self.menuTableView];
    
    
    UIPanGestureRecognizer *panGestureRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(move:)];
    [self.view addGestureRecognizer:panGestureRecognizer];
    
    
    
}

-(void)userClickedImageOrLabel
{
    [self.delegate userTappedImage];
}

-(void)move:(id)sender {
    [self.view.superview bringSubviewToFront:[(UIPanGestureRecognizer*)sender view]];
    CGPoint translatedPoint = [(UIPanGestureRecognizer*)sender translationInView:self.view.superview];
    
    if ([(UIPanGestureRecognizer*)sender state] == UIGestureRecognizerStateBegan) {
        
        firstX = [[sender view] center].x;
        firstY = [[sender view] center].y;
        NSLog(@"firstX  %f",firstX);
        
    }
    
    translatedPoint = CGPointMake(firstX+translatedPoint.x, firstY);
    NSLog(@"translatedPoint  %f",translatedPoint.x);
    
    if(translatedPoint.x > [[sender view] frame].size.width/2 ){
        
        [[sender view] setCenter:translatedPoint];
        
    }
    if ([(UIPanGestureRecognizer*)sender state] == UIGestureRecognizerStateEnded) {
        CGFloat velocityX = ([(UIPanGestureRecognizer*)sender velocityInView:self.view.superview].x);
        NSLog(@"ENDED");
        CGFloat finalX = translatedPoint.x + velocityX;
        CGFloat finalY = firstY;// translatedPoint.y + (.35*[(UIPanGestureRecognizer*)sender velocityInView:self.view].y);
        
        NSLog(@"finalX  %f",finalX);
        
        NSLog(@"self.superview.frame.size.width  %f",self.view.superview.frame.size.width);
        
        if (finalX <= self.view.superview.frame.size.width/2+ self.view.frame.size.width/2 ){
            finalX =  self.view.frame.size.width/2;
            NSLog(@"maenj");
            
        } else if (finalX >  self.view.superview.frame.size.width/2 + self.view.frame.size.width/2) {
            finalX =  self.view.superview.frame.size.width + self.view.frame.size.width/2;
            NSLog(@"vece");
            
        }
        
        
        
        CGFloat animationDuration = 0.3;
        
        NSLog(@"the duration is: %f", animationDuration);
        
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:animationDuration];
        [UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
        [UIView setAnimationDelegate:self];
        
        
        if (finalX >  self.view.superview.frame.size.width/2+ self.view.frame.size.width/2 ) {
            
            [UIView setAnimationDidStopSelector:@selector(close)];
            
        }
        
        [[sender view] setCenter:CGPointMake(finalX, finalY)];
        [UIView commitAnimations];
    }
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    if (indexPath.row == 0) {
        
        MenuTableViewCell *menuCell = (MenuTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"menuCellE"];
        if (menuCell == nil) {
            menuCell = [[MenuTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"menuCellE"];
            menuCell.backgroundColor = [UIColor clearColor];
            menuCell.selectionStyle = UITableViewCellSelectionStyleNone;
            [menuCell.firstButton addTarget:self action:@selector(settingsButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
            [menuCell.secondButton addTarget:self action:@selector(faqButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
            [menuCell.thirdButton addTarget:self action:@selector(agreementButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
            [menuCell.forthButton addTarget:self action:@selector(logoutButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
            
        }
        return menuCell;
    }
    
    else if (indexPath.row == 4) {
        SocialNetworkTableViewCell *socialCell = (SocialNetworkTableViewCell*)[tableView dequeueReusableCellWithIdentifier:@"socialCell"];
        if (socialCell == nil) {
            socialCell = [[SocialNetworkTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"socialCell"];
            socialCell.backgroundColor = [UIColor clearColor];
            socialCell.delegate = self;
            socialCell.selectionStyle = UITableViewCellSelectionStyleNone;
            [socialCell.textLabel setTextColor:NEON_GREEN];
            UIImageView *line = [[UIImageView alloc] initWithFrame:CGRectMake(0, 58, 320, 2)];
            line.backgroundColor = NEON_GREEN;
            [socialCell addSubview:line];
            
        }
        socialCell.textLabel.text = NSLocalizedString(@"Recommend", nil);
        return socialCell;
    }
    
    
    else {
        UITableViewCell *switchCell = (UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"switchCell"];
        if (switchCell == nil) {
            switchCell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"switchCell"];
            switchCell.backgroundColor = [UIColor clearColor];
            switchCell.selectionStyle = UITableViewCellSelectionStyleNone;
            UISwitch *aSwitch = [[UISwitch alloc] initWithFrame:CGRectZero];
            switchCell.accessoryView = aSwitch;
            aSwitch.tag = indexPath.row;
            [aSwitch addTarget:self action:@selector(switchChangedMenu:) forControlEvents:UIControlEventValueChanged];UIImageView *line = [[UIImageView alloc] initWithFrame:CGRectMake(0, 58, 320, 2)];
            line.backgroundColor = NEON_GREEN;
            [switchCell addSubview:line];
            if (indexPath.row == 2) {
                [aSwitch setOn:[[NSUserDefaults standardUserDefaults] boolForKey:@"locationSharing"]];
            }
            if (indexPath.row == 1) {
                [aSwitch setOn:[[NSUserDefaults standardUserDefaults] boolForKey:@"traffic"]];
            }
        }
        switchCell.textLabel.text = [NSString stringWithFormat:@"%@",[self.dataArray objectAtIndex:indexPath.row]];
        switchCell.textLabel.textColor = NEON_GREEN;
        switchCell.textLabel.textAlignment = NSTextAlignmentLeft;
        return switchCell;
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == 0) {
        return 80;
    }
    else {
        return 60;
    }
    
}


-(void)close {
    
    
    NSLog(@"close");
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeMenu)]) {
        
        [self.delegate closeMenu];
        
    }
}

-(void)facebookSelected
{
    NSLog(@"Selected fb");
    if ([SLComposeViewController isAvailableForServiceType:SLServiceTypeFacebook]) {
        SLComposeViewController *facebookController = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeFacebook];
        
        // add initial text
        [facebookController setInitialText:@"I'm using OneTraffic, this application is best for social traffic sharing!"];
        
        // present controller
        [self presentViewController:facebookController animated:YES completion:nil];
    }
}

-(void)twitterSelected
{
    if ([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter])
    {
        SLComposeViewController *tweetSheet = [SLComposeViewController
                                               composeViewControllerForServiceType:SLServiceTypeTwitter];
        [tweetSheet setInitialText:@"I'm using OneTraffic, this application is best for social traffic sharing!"];
        [self presentViewController:tweetSheet animated:YES completion:nil];
    }
}

-(void)emailSelected
{
    if ([MFMailComposeViewController canSendMail]) {
        // device is configured to send mail
        
        // Email Subject
        NSString *emailTitle = @"One Traffic - invite";
        // Email Content
        NSString *messageBody = @"I'm using OneTraffic, this application is best for social traffic sharing!";
        // To address
        NSArray *toRecipents = [NSArray arrayWithObject:@"post@onetraffic.com"];
        
        
        
        MFMailComposeViewController *mc = [[MFMailComposeViewController alloc] init];
        mc.mailComposeDelegate = self;
        [mc setSubject:emailTitle];
        [mc setMessageBody:messageBody isHTML:NO];
        [mc setToRecipients:toRecipents];
        
        // Present mail view controller on screen
        [self presentViewController:mc animated:YES completion:NULL];
    }
}
- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
    // Notifies users about errors associated with the interface
    switch (result)
    {
        case MFMailComposeResultCancelled:
            break;
        case MFMailComposeResultSaved:
            break;
        case MFMailComposeResultSent:
            break;
        case MFMailComposeResultFailed:
            break;
            
        default:
            
            break;
    }
    [self dismissViewControllerAnimated:NO completion:nil];
    
    
}
-(void)switchChangedMenu:(UISwitch *)sender
{
    [self.delegate switchChanged:sender];
}

-(void)settingsButtonTapped:(UIButton *)sender
{
    [self.delegate selectedIndex:2];
}

-(void)faqButtonTapped:(UIButton *)sender
{
    [self.delegate selectedIndex:3];
}

-(void)agreementButtonTapped:(UIButton *)sender
{
    [self.delegate selectedIndex:4];
}

-(void)logoutButtonTapped:(UIButton *)sender
{
    [self.delegate selectedIndex:5];
}

@end
