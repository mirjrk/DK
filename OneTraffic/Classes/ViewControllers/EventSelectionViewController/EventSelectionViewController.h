//
//  EventSelectionViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 2/8/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"
#import "BasicViewController.h"

@protocol EventAdded <NSObject>

-(void)eventAdded;

@end

@interface EventSelectionViewController : BasicViewController <UICollectionViewDataSource, UICollectionViewDelegate, UIAlertViewDelegate>

@property (nonatomic, strong) UICollectionView *eventCollection;
@property (nonatomic, strong) NSArray *imagesArray;
@property (nonatomic, strong) NSArray *eventNamesArray;
@property (nonatomic, strong) UIButton *dismissButton;
@property (nonatomic) float lat;
@property (nonatomic) float lon;
@property (nonatomic, weak) id <EventAdded>delegate;
@property NSInteger selectedIndexPath;


@end
