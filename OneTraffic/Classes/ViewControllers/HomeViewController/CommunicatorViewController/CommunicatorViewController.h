//
//  CommunicatorViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 2/28/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BasicViewController.h"
#import "CommunicatorModel.h"
#import "SWTableViewCell.h"
#import "QuickMessageViewController.h"
#import "HorizontalRouteSelectorViewController.h"

/*
 * @discussion Delegating for close button to remove Communicator from memory because we have custom structure, where Communicator is child view of Home, so manual managing of Communicator is neccessary.
 - Settings delegate is to notify Home to push to it's navigation stack.
 */
@protocol CommunicatorDelegate <NSObject>
- (void)closeButtonPressedCommunicator:(UIButton *)sender;
- (void)settingsButtonPressedCommunicator;
- (void)addFriendsButtonPressedCommunicator;
- (void)selectFriendPressed:(CommunicatorModel *)communicatorModel;
@end

@interface CommunicatorViewController : UIViewController <UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, SWTableViewCellDelegate, UIGestureRecognizerDelegate, QuickMessageDelegate>

@property (nonatomic, strong) UITableView *communicatorTableView;

/*
 * @discussion We need this delegate because main push navigation is from container(Home). Structure of this part is sensitive, so delegating commands to HomeViewController is maybe best way to prevent some misleadings, etc.
 */
@property (nonatomic, weak) id <CommunicatorDelegate> delegate;
@property (nonatomic, strong) UIButton *settingsButton;

@property (nonatomic, strong) UITextField *searchTextField;
@property (nonatomic, strong) NSArray *allFriendsHolder;
- (void)getCommunicatorList;

@end
