//
//  HorizontalRouteSelectorViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 12/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainRouteModel.h"


@protocol HorizontalRouteDelegate <NSObject>
-(void)removeHorizontalDestination;
- (void)routeHorizontalSelected:(MainRouteModel *)route andIndex:(NSInteger)index;
- (void)hideHorizontalSelector;

-(void)deselectRoute;
@end


@interface HorizontalRouteSelectorViewController : UIViewController <UICollectionViewDelegate, UICollectionViewDataSource>

@property (nonatomic, strong) UICollectionView *collectionView;
@property (nonatomic, strong) NSArray *routes;
@property (nonatomic, strong) NSDictionary *allRoutesData;
@property (nonatomic, strong) MainRouteModel *mainRoute;
@property (nonatomic, weak) id <HorizontalRouteDelegate> delegate;
@property (nonatomic, strong) UIButton *startStopButton;
@property (nonatomic, strong) MainRouteModel *selectedRoute;
@property (nonatomic, assign) BOOL showRoutes;

@property NSInteger selectedIndex;
@property BOOL isRouteSelected;
@end
