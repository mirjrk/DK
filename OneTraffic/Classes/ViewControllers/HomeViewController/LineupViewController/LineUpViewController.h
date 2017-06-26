//
//  LineUpViewController.h
//  OneTraffic
//
//  Created by Zesium on 3/11/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Constant.h"
#import "EventModel.h"
#import "SectionModel.h"
#import "LogicHelper.h"
#import "MainRouteModel.h"
#import "MyLocationCell.h"
#import "AlertCenterTableViewCell.h"
#import "AFNetworkReachabilityManager.h"
#import "AFNetworkActivityIndicatorManager.h"
@protocol LineUpCustomDelegate <NSObject>
-(void)myPlacesTapped;
@end

@interface LineUpViewController : UIViewController<UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, weak) id <LineUpCustomDelegate> delegate;

/*!
 * @brief Alert center table view.
 */
@property(nonatomic, retain) UITableView *lineUpTableView;


/**
 * @brief Array of user locations in one timer cycle
 */
@property (nonatomic, retain) NSMutableArray *lineUpArray;

@property (nonatomic, strong) UIButton *myPlacesButton;

-(void)setUpLineUpArray:(NSMutableArray*)array;

@end
