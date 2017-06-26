//
//  MenuViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 2/29/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <UIKit/UIGestureRecognizerSubclass.h>
#import "Communication.h"
#import "SocialNetworkTableViewCell.h"

@import MessageUI;
@import Social;

@protocol MenuDelegate <NSObject>;
-(void)selectedIndex:(NSInteger)index ;
-(void)closeMenu ;
-(void)userTappedImage;
-(void)switchChanged:(UISwitch *)sender;
@end
@interface MenuViewController : UIViewController <UITableViewDataSource, UITableViewDelegate,UIGestureRecognizerDelegate, SocialDelegate, MFMailComposeViewControllerDelegate>
{
    CGFloat _centerX;
    CGFloat firstX;
    CGFloat  firstY ;
}
@property (nonatomic,retain)UIView *clearView;
@property (nonatomic,retain)UIButton *closeButton;
@property (nonatomic,retain)UIView *coloredView;
@property (nonatomic,retain)UIImageView *userImageView;
@property (nonatomic,retain)UILabel *nameLabel;
@property (nonatomic,retain)NSArray *dataArray;
@property (nonatomic,retain)UITableView *menuTableView;
@property (nonatomic,retain) UIPanGestureRecognizer *panGestureRecognizer;
@property (nonatomic, weak) id <MenuDelegate> delegate;
@end
