//
//  NotificationView.h
//  OneTraffic
//
//  Created by Stefan Andric on 3/28/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioServices.h>
#import "ReplyView.h"
@import AVFoundation;


@interface NotificationView : UIView <AVAudioPlayerDelegate, ReplyDelegate, UIGestureRecognizerDelegate>
@property (nonatomic, strong) UIView *notificationView;
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *subtitleLabel;
@property (nonatomic, strong) UIImageView *imageView;
@property (nonatomic, strong) UIButton *acceptButton;
@property (nonatomic, strong) UIButton *cancelButton;
@property (nonatomic, strong) NSDictionary *response;
@property (nonatomic, strong) UIView *tapCancelView;
@property (nonatomic, assign) NSInteger notificationType;

- (id)initWithFrame:(CGRect)frame data:(NSDictionary*)dict andType:(NSInteger)type;

@end
