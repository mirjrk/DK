//
//  EventDetailsViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 4/1/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Mapbox/Mapbox.h>
#import "LocationModel.h"
#import "AddCommentViewController.h"
#import "BasicViewController.h"
#import "EventModel.h"
#import "CommentsViewController.h"
@import AVFoundation;

@interface EventDetailsViewController : BasicViewController <MGLMapViewDelegate, UIGestureRecognizerDelegate>

@property (nonatomic, strong) EventModel *event;
@property (nonatomic) MGLMapView *mapView;
@property (nonatomic, strong) UITableView *eventCommentsTableView;

@property (nonatomic, strong) UIButton *commentButton;
@property (nonatomic, strong) UIButton *reportResolvedButton;
@property (nonatomic, strong) UIView *upperView;
@property (nonatomic, strong) UILabel *eventStartsIn;
@property (nonatomic, strong) UILabel *passEventIn;
@property (nonatomic, strong) UILabel *eventStartsInNumber;
@property (nonatomic, strong) UILabel *passEventInNumber;
@end
