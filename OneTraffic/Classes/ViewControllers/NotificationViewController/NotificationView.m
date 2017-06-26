//
//  NotificationView.m
//  OneTraffic
//
//  Created by Stefan Andric on 3/28/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "NotificationView.h"
#import "Constant.h"
#import "Communication.h"
@import AVFoundation;
#import "LogicHelper.h"
#import "UIImageView+AFNetworking.h"
#import "UIHelper.h"

#define kDropdownImageSize 40
#define kDropdownPadding 10
#define kDropdownTitleFontSize 19
#define kDropdownSubtitleFontSize 14
#define kDropdownButtonWidth 75
#define kDropdownButtonHeight 30

@implementation NotificationView


- (id)initWithFrame:(CGRect)frame data:(NSDictionary*)dict andType:(NSInteger)type
{
    self = [super initWithFrame:frame];
    if (self) {
        self.notificationType=type;
        self.response = dict;
        self.backgroundColor = DARK_BLUE;
        [self createInterface];
    }
    return self;
}

-(void)createInterface
{
    //    self.frame = CGRectMake(0, -SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
    self.frame =  CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    //    [UIView animateWithDuration:1 delay:0 usingSpringWithDamping:1 initialSpringVelocity:5 options:UIViewAnimationOptionCurveEaseInOut animations:^{
    //        self.frame =  CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    //    } completion:^(BOOL finished) {
    //    }];
    
    self.imageView = [[UIImageView alloc] initWithFrame:CGRectMake(SCREEN_WIDTH/2-50, SCREEN_HEIGHT/2-180, 100, 100)];
    self.imageView.image = [UIImage imageNamed:@"Logo"];
    self.imageView.layer.cornerRadius = self.imageView.frame.size.height/2;
    self.imageView.clipsToBounds = YES;
    self.imageView.layer.borderColor = NEON_GREEN.CGColor;
    self.imageView.layer.borderWidth = 1.5f;
    
    NSDictionary *from = [self.response objectForKey:@"from"];
    if (self.notificationType < 200) {
        NSString *pictureUrl = [from objectForKey:@"pictureUrl"];
        if (pictureUrl != (id)[NSNull null] && pictureUrl.length > 0 && ![pictureUrl isEqualToString:@""]) {
            [self.imageView setImageWithURL:[NSURL URLWithString:[from objectForKey:@"pictureUrl"]]];
        }
        else if ([from objectForKey:@"name"] != (id)[NSNull null] && ![[from objectForKey:@"name"]  isEqual: @""] && from != nil) {
            self.imageView.image =  [UIHelper drawImageWithSnapshotWithFirstName:[from objectForKey:@"name"] andLastName:[from objectForKey:@"surname"]];
        }
    }
    else {
        
    }
    
    [self addSubview:self.imageView];
    
    self.titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, SCREEN_HEIGHT/2+40, SCREEN_WIDTH, 40)];
    self.titleLabel.textColor = [UIColor whiteColor];
    self.titleLabel.textAlignment = NSTextAlignmentCenter;
    self.titleLabel.font = [UIFont systemFontOfSize:28];
    [self addSubview:self.titleLabel];
    
    NSDictionary *user=[self.response objectForKey:@"from"];
    NSMutableString *username=[NSMutableString stringWithFormat:@"%@", [user objectForKey:@"name"]];
    
    if([user objectForKey:@"surname"]!= (id)[NSNull null]){
        
        [username appendFormat:@" %@",[user objectForKey:@"surname"]];
        
    }
    
    if( self.notificationType == 1){
        
        self.titleLabel.text = @"Location Sharing Request";
        
    }else if (self.notificationType == 2){
        
        self.titleLabel.text = @"You now share location";
        
    }else if (self.notificationType == 3){
        
        self.titleLabel.text =[ NSString stringWithFormat:@"User %@",username ];
        
    }else if (self.notificationType == 10){
        
        self.titleLabel.text = NSLocalizedString(@"FriendRequest", nil);
        
    }else if (self.notificationType == 11){
        
        self.titleLabel.text = @"You are now friend";
        
    }else if (self.notificationType == 20){
        
        self.titleLabel.text = [NSString stringWithFormat:@"%@ posted to %@",username ,[self.response objectForKey:@"channelName"]];
        self.titleLabel.font = [UIFont systemFontOfSize:15];
    }
    else if (self.notificationType == 30){
        self.imageView.frame = CGRectMake(self.imageView.frame.origin.x, self.imageView.frame.origin.y-100, self.imageView.frame.size.width, self.imageView.frame.size.height);
        
        if ([self.response objectForKey:@"message"] != (id)[NSNull null]) {
            NSData *dataa = [[self.response objectForKey:@"message"] dataUsingEncoding:NSUTF8StringEncoding];
            
            self.titleLabel.text = [[NSString alloc] initWithData:dataa encoding:NSNonLossyASCIIStringEncoding];
        }
    }
    else if (self.notificationType == 101){
        self.titleLabel.text = NSLocalizedString(@"NewNotifications", nil);
    }
    
    
    self.subtitleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, self.titleLabel.frame.origin.y+self.titleLabel.frame.size.height+5,  SCREEN_WIDTH, 40)];
    self.subtitleLabel.textColor = NEON_GREEN;
    self.subtitleLabel.textAlignment = NSTextAlignmentCenter;
    self.subtitleLabel.font = [UIFont systemFontOfSize:18];
    
    if(self.notificationType == 1 || self.notificationType == 10){
        
        self.subtitleLabel.text = [NSString stringWithFormat:@"from %@",username] ;
        
    }else if (self.notificationType == 2 || self.notificationType == 11){
        
        self.subtitleLabel.text = [NSString stringWithFormat:@"with %@",username] ;
        
    }else if(self.notificationType == 20 ){
        
        self.subtitleLabel.text = [NSString stringWithFormat:@"%@", [self.response objectForKey:@"message"]] ;
        
        
    }else if(self.notificationType == 3 ){
        
        self.subtitleLabel.text = [NSString stringWithFormat:@"has left location sharing group"] ;
        
    }
    else if (self.notificationType == 101){
        self.imageView.contentMode = UIViewContentModeScaleAspectFit;
        self.subtitleLabel.text = @"View when safe.";
    }
    else if (self.notificationType >= 200) { //Event handling
        int typeOfNotification = (int)self.notificationType - 200;
        
        self.titleLabel.frame = CGRectMake(0, CGRectGetMaxY(self.imageView.frame)+20, SCREEN_WIDTH, SCREEN_HEIGHT-CGRectGetMaxY(self.imageView.frame)-220);
        self.titleLabel.textAlignment = NSTextAlignmentCenter;
        self.imageView.image = [UIImage imageNamed:[LogicHelper setImageDependingOnId:typeOfNotification]];
        self.titleLabel.numberOfLines = 0;
        
//        self.titleLabel.text = [NSString stringWithFormat:@"Event detected: %@", [LogicHelper setTitleDependingOnId:typeOfNotification]];
        NSMutableAttributedString *final = [NSMutableAttributedString new];
        
        NSDictionary *attrs = @{NSForegroundColorAttributeName:[UIColor whiteColor]};
        NSAttributedString *eventDetected = [[NSAttributedString alloc] initWithString:@"Event detected:\n" attributes:attrs];
        [final appendAttributedString:eventDetected];
        
        NSDictionary *attrsTwo = @{NSForegroundColorAttributeName:NEON_GREEN};
        NSAttributedString *eventType = [[NSAttributedString alloc] initWithString:[LogicHelper setTitleDependingOnId:typeOfNotification] attributes:attrsTwo];
        [final appendAttributedString:eventType];
        
        
        self.titleLabel.attributedText = final;
        
        self.imageView.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleTapOfEvent:)];
        tap.numberOfTapsRequired = 1;
        tap.delegate = self;
        [self.imageView addGestureRecognizer:tap];
    }
    
    else if (self.notificationType == 30) {
        self.subtitleLabel.frame = CGRectMake(self.subtitleLabel.frame.origin.x, CGRectGetMaxY(self.imageView.frame) + 20, self.subtitleLabel.frame.size.width, self.subtitleLabel.frame.size.height);
        self.subtitleLabel.textColor = [UIColor whiteColor];
        self.subtitleLabel.font = [UIFont systemFontOfSize:25];
        
        
        self.subtitleLabel.text = [NSString stringWithFormat:@"%@ %@", [from objectForKey:@"name"], [from objectForKey:@"surname"]];
        
        
        self.titleLabel.frame = CGRectMake(self.titleLabel.frame.origin.x, CGRectGetMaxY(self.subtitleLabel.frame)+20, self.titleLabel.frame.size.width, SCREEN_HEIGHT-CGRectGetMaxY(self.subtitleLabel.frame)-170);
        self.titleLabel.numberOfLines = 0;
    }
    
    
    
    [self addSubview:self.subtitleLabel];
    
    self.acceptButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    [self.acceptButton setFrame:CGRectMake(SCREEN_WIDTH-150, SCREEN_HEIGHT-140, 80, 80)];
    [self.acceptButton setImage:[UIImage imageNamed:@"acceptIconNotification"] forState:UIControlStateNormal];
    [self.acceptButton addTarget:self action:@selector(acceptButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    
    self.cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    self.cancelButton.titleLabel.font = [UIFont fontWithName:@"HelveticaNeue-Medium" size:13];
    [self.cancelButton setTitleColor:DARK_BLUE forState:UIControlStateNormal];
    self.cancelButton.adjustsImageWhenHighlighted = YES;
    
    [self.cancelButton setFrame:CGRectMake(70, SCREEN_HEIGHT-140, 80, 80)];
    [self.cancelButton setImage:[UIImage imageNamed:@"declineIconNotification"] forState:UIControlStateNormal];
    [self.cancelButton addTarget:self action:@selector(cancelButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:self.acceptButton];
    [self addSubview:self.cancelButton];
    
    if (self.notificationType == 1 || self.notificationType == 10){
        
        
    }else if (self.notificationType == 30) {
        //        self.cancelButton.frame = CGRectMake(SCREEN_WIDTH/2-40, self.cancelButton.frame.origin.y, 80, 80);
        [self.acceptButton setImage:[UIImage imageNamed:@"reply_button"] forState:UIControlStateNormal];
    }
    else if (self.notificationType == 101 || self.notificationType >= 200) {
        [self.acceptButton removeFromSuperview];
        self.cancelButton.frame = CGRectMake(SCREEN_WIDTH/2-40, self.cancelButton.frame.origin.y, 80, 80);
    }
    
    AudioServicesPlayAlertSound(kSystemSoundID_Vibrate);
    if ([[NSUserDefaults standardUserDefaults] boolForKey:@"alertSound"] == YES ) {
        [LogicHelper playSoundNamed:@"OTNotificationSound"];
    }
    
    //    [self performSelector:@selector(speak) withObject:nil afterDelay:1];
    
}

- (void)speak {
    
    AVSpeechSynthesizer *synth = [[AVSpeechSynthesizer alloc] init];
    NSString *toSpeech = @"";
    toSpeech = [NSString stringWithFormat:@"%@ %@", self.titleLabel.text, self.subtitleLabel.text];
    
    AVSpeechUtterance *speech = [[AVSpeechUtterance alloc] initWithString:toSpeech];
    speech.voice = [AVSpeechSynthesisVoice voiceWithLanguage:@"en-US"];
    speech.rate = AVSpeechUtteranceDefaultSpeechRate;
    [synth speakUtterance:speech];
    
    UISwipeGestureRecognizer *swipeUp = [[UISwipeGestureRecognizer alloc]initWithTarget:self action:@selector(swipeUp:)];
    swipeUp.direction=UISwipeGestureRecognizerDirectionUp;
    [self addGestureRecognizer:swipeUp];
    
}



-(void)acceptButtonTapped:(UIButton *)sender
{
    //    [self decrementOneBadge];
    if(self.notificationType == 1){
        
        [self acceptLocationSharing:YES];
        
    }else if (self.notificationType == 10){
        
        [self acceptFriendRequest:YES];
        
    }
    //Message
    else if (self.notificationType == 30) {
        ReplyView *reply = [[ReplyView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
        reply.delegate = self;
        [self addSubview:reply];
    }
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"notificationAccepted" object:self];
    
}

-(void)acceptLocationSharing:(BOOL)accept
{
    
    NSNumber *acceptInt = accept ? @1 : @0;
    
    NSString *groupId = [self.response objectForKey:@"groupId"] ;
    
    NSMutableArray *usersIds = [NSMutableArray new];
    for (NSDictionary *dict in [self.response objectForKey:@"usersInGroup"]) {
        [usersIds addObject:[dict objectForKey:@"id"]];
    }
    
    NSDictionary *group = @{@"groupId":groupId,
                            @"allowedFollower":acceptInt,
                            @"wantToFollow":acceptInt,
                            @"usersInGroup":usersIds,
                            @"messageId":[self.response objectForKey:@"messageId"]};
    
    NSArray *groupsArray = @[group];
    NSDictionary *parameters = @{@"userId":[[NSUserDefaults standardUserDefaults] objectForKey:USER_ID],
                                 @"locationSharingGroups":groupsArray};
    
    [Communication requestForLocationSharing:parameters successfulBlock:^(NSDictionary *response) {
        
        NSLog(@"SUCCESS ACCEPT: %@", parameters);
        [self animateClose];
    } errorBlock:^(NSDictionary *error) {
        NSLog(@"FAILURE ACCEPT: %@", parameters);
        //TODO
        [self animateClose];
    }];
    
    
}

-(void)acceptFriendRequest:(BOOL)accept
{
    NSNumber *acceptInt =(accept) ? [NSNumber numberWithInt:1] :[NSNumber numberWithInt:-1];
    
    NSDictionary *parameters = @{@"groupId":[self.response objectForKey:@"groupId"],
                                 @"acceptFriendship":acceptInt,
                                 @"messageId":[self.response objectForKey:@"messageId"]};
    
    [Communication friendRequestResponse:parameters successBlock:^(NSDictionary *response) {
        NSLog(@"SUCCESS ACCEPT: %@", parameters);
        [[NSNotificationCenter defaultCenter] postNotificationName:@"reloadFriends" object:nil];
        
        [self animateClose];
        
    } errorBlock:^(NSDictionary *error) {
        NSLog(@"FAILURE ACCEPT: %@", parameters);
        //TODO
        
        [self animateClose];
    }];
    
}

-(void)cancelButtonTapped:(UIButton *)sender
{
    if(self.notificationType == 1){
        [self acceptLocationSharing:NO];
    }
    
    else if (self.notificationType == 10){
        
        [self acceptFriendRequest:NO];
        
    }
    
    else{
        
        if (self.notificationType == 11){
            
            [[NSNotificationCenter defaultCenter] postNotificationName:@"reloadFriends" object:nil];
            
        }
        
        [self animateClose];
    }
    
    [[NSNotificationCenter defaultCenter] postNotificationName:@"notificationDeclined" object:self];
}

-(void)animateClose
{
    
    [UIView animateWithDuration:1 delay:0 usingSpringWithDamping:1 initialSpringVelocity:5 options:UIViewAnimationOptionCurveEaseInOut animations:^{
        self.notificationView.frame = CGRectMake(0, -150, SCREEN_WIDTH, 100);
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}

-(void)playSound
{
    SystemSoundID audioEffect;
    NSString *path = [[NSBundle mainBundle] pathForResource:@"OTNotificationSound" ofType:@"wav"];
    
    if ([[NSFileManager defaultManager] fileExistsAtPath : path]) {
        NSURL *pathURL = [NSURL fileURLWithPath: path];
        AudioServicesCreateSystemSoundID((__bridge CFURLRef) pathURL, &audioEffect);
        AudioServicesPlaySystemSound(audioEffect);
    }
    else {
        NSLog(@"error, file not found: %@", path);
    }
}

-(void)swipeUp:(UISwipeGestureRecognizer*)gestureRecognizer
{
    [UIView animateWithDuration:2 delay:0 usingSpringWithDamping:1 initialSpringVelocity:5 options:UIViewAnimationOptionCurveEaseInOut animations:^{
        self.notificationView.frame = CGRectMake(0, -100, SCREEN_WIDTH, 100);
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}


- (void)stringToSend:(NSString *)string {
    
    NSString *groupId = [self.response objectForKey:@"groupId"];
    NSDictionary *params = @{@"groupId":groupId,
                             @"message": string,
                             @"usersInGroup":@[]};
    
    
    [Communication sendMessage:params withSuccessBlock:^(NSDictionary *response) {
        if ([[response objectForKey:@"error"] integerValue] == 0) {
            [self removeFromSuperview];
        }
    } errorBlock:^(NSDictionary *error) {
        NSLog(@"Error sending message");
    }];
}

- (void)handleTapOfEvent:(UIPinchGestureRecognizer *)recognizer {
    [[NSNotificationCenter defaultCenter] postNotificationName:@"eventDetailTapped"
                                                        object:nil
                                                      userInfo:self.response];
    [self removeFromSuperview];
}


@end
