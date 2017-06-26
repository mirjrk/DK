//
//  DestinationListViewController.h
//  OneTraffic
//
//  Created by Zesium on 2/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DestinationModel.h"
#import "SWTableViewCell.h"
#import <CoreLocation/CoreLocation.h>
#import "UIHelper.h"
#import "BasicViewController.h"


@interface DestinationListViewController : BasicViewController <UITableViewDataSource, UITableViewDelegate, UIGestureRecognizerDelegate, SWTableViewCellDelegate, UIActionSheetDelegate>

@property (nonatomic,retain)UIView *clearView;
@property (nonatomic,retain)UIButton *closeButton;
@property (nonatomic,retain)UIView *coloredView;

@property(nonatomic, assign) CLLocationCoordinate2D currentLocation;

@property (nonatomic,retain)NSMutableArray *dataArray;
@property (nonatomic,retain)UITableView *listTableView;
@property (nonatomic,retain) UIPanGestureRecognizer *panGestureRecognizer;
@property BOOL isCalledWebservice;
@property NSInteger selectedIndexPathForDeleting; //Globally defined because we need confirmation of deleting before we really delete destination


@end
