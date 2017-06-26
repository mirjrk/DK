//
//  SocialNetworkTableViewCell.h
//  OneTraffic
//
//  Created by Stefan Andric on 3/1/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol SocialDelegate <NSObject>
-(void)facebookSelected;
-(void)twitterSelected;
-(void)emailSelected;
@end

@interface SocialNetworkTableViewCell : UITableViewCell
@property(nonatomic, retain) UIImageView *emailImage;
@property(nonatomic, retain) UIImageView *facebookImage;
@property(nonatomic, retain) UIImageView *twitterImage;
@property(nonatomic, retain) UILabel *recommendLabel;
@property(nonatomic, weak) id <SocialDelegate> delegate;
@end
