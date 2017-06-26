//
//  SelectRouteViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 3/14/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainRouteModel.h"

@protocol SelectedRouteDelegate <NSObject>
-(void)routeSelected:(MainRouteModel *)route;
-(void)cancelSelectRouteController;
-(void)removeDestination;
@end

@interface SelectRouteViewController : UIViewController <UITableViewDataSource, UITableViewDelegate,UIGestureRecognizerDelegate>

@property (nonatomic, strong) UITableView *routesTableView;
@property (nonatomic, strong) NSArray *routes;
@property (nonatomic, strong) NSDictionary *allRoutesData;
@property (nonatomic, weak) id <SelectedRouteDelegate> delegate;
@property (nonatomic, strong) MainRouteModel *mainRoute;
@property (nonatomic, strong) UIView *clearView;

@end
