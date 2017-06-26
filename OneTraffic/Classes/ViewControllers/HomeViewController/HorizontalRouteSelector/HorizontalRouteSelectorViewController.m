//
//  HorizontalRouteSelectorViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 12/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "HorizontalRouteSelectorViewController.h"
#import "Constant.h"
#import "HorizontalSelectorCollectionViewCell.h"
#import "EventModel.h"
#import "LogicHelper.h"
#import "UIHelper.h"

@interface HorizontalRouteSelectorViewController ()
@property BOOL isStart;
@property NSInteger timerInteger;
@property NSTimer *timer;
@end

@implementation HorizontalRouteSelectorViewController


-(void)loadView
{
    self.view = [[UIView alloc] initWithFrame:CGRectMake(0, -SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view.backgroundColor = DARK_BLUE;
    
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.routes = [self.allRoutesData objectForKey:@"routes"];
    [self setupCollectionView];
    self.isStart = YES;
    self.timerInteger = 4;
    [self addTimer];
}


- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:YES];
    [self.timer invalidate];
    self.timer = nil;
}

- (void)setupCollectionView {
    CGRect collectionFrame = CGRectZero;
    if (self.routes.count == 1) {
        collectionFrame =  CGRectMake((SCREEN_WIDTH/2-20-60), 15, (SCREEN_WIDTH-20-60)/2-10, 60);
    }
    else {
        collectionFrame =  CGRectMake(10, 15, SCREEN_WIDTH-20-60, 60);
    }
    self.collectionView = [[UICollectionView alloc] initWithFrame:collectionFrame collectionViewLayout:[self setUpCollectionViewFlow]];
    self.collectionView.showsHorizontalScrollIndicator = NO;
    NSString *identifier = @"horisontallRouteCell";
    UINib *nib = [UINib nibWithNibName:@"HorisontalSelectorCollectionViewCell" bundle: nil];
    [self.collectionView registerNib:nib forCellWithReuseIdentifier:identifier];
    [self.collectionView setBackgroundColor:DARK_BLUE];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    
    [self.view addSubview:self.collectionView];
    
    UIButton *cancelButton = [UIHelper createCustomButtonWithFrame:CGRectMake(SCREEN_WIDTH-60, 15, 60, 60) andTitle:@""];
    [cancelButton setImage:[UIImage imageNamed:@"stopRoute"] forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(stopButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    cancelButton.backgroundColor = [UIColor clearColor];
    [self.view addSubview:cancelButton];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake((SCREEN_WIDTH-20-60)/2-10, 60);
}

-(UICollectionViewFlowLayout*) setUpCollectionViewFlow
{
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionHorizontal];
    [flowLayout setSectionInset:UIEdgeInsetsMake(0, 0, 0, 0)];
    flowLayout.minimumInteritemSpacing = 0;
    
    return flowLayout;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    if (self.showRoutes == YES) {
        return self.routes.count;
    }
    else {
        return 0;
    }
    
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HorizontalSelectorCollectionViewCell *cell = (HorizontalSelectorCollectionViewCell *)[self.collectionView dequeueReusableCellWithReuseIdentifier:@"horisontallRouteCell" forIndexPath:indexPath];
    
    
    
    
    MainRouteModel *route =[[MainRouteModel alloc]initWithDictionary:self.routes[indexPath.row]];
    NSArray *array = [NSMutableArray arrayWithArray:route.reportListOfRoute];
    
    for ( NSMutableDictionary *dict in array ) {
        
        if([dict objectForKey:@"type"]!=nil &&[[dict objectForKey:@"type"] integerValue]==4 ){
            
            EventModel *event = [[EventModel alloc] initWithDictionary:dict];

            cell.routeLabel.text = [NSString stringWithFormat:@"%@", [LogicHelper createTopMenuStringFromDestinationWithoutDest:event]];
            
            
            
        }
    }
    
    if (indexPath.row == self.selectedIndex) {
        cell.selectedIndicatorView.backgroundColor = [UIColor grayColor];
        
    }
    else {
        cell.selectedIndicatorView.backgroundColor = [UIColor clearColor];
    }
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    
    if (self.selectedIndex == indexPath.row) {
        [self.delegate deselectRoute];
        self.selectedIndex = -1;
    }
    
    else {
        self.selectedIndex = indexPath.row;
        self.selectedRoute = [[MainRouteModel alloc] initWithDictionary:self.routes[indexPath.row]];
        [self.delegate routeHorizontalSelected:self.selectedRoute andIndex:indexPath.row];
    }
    
    [self.collectionView reloadData];
}

- (void)selectedPerform {
    
}

- (void)stopButtonTapped:(UIButton *)sender {
    [self.delegate removeHorizontalDestination];
}

- (void)addTimer {
    self.timer = [NSTimer scheduledTimerWithTimeInterval:1.0
                                                  target:self
                                                selector:@selector(decreaseTimer:)
                                                userInfo:nil
                                                 repeats:YES];
}

- (void)decreaseTimer:(NSTimer *)timer {
    if (self.timerInteger > 0) {
        self.timerInteger--;
        NSLog(@"Timer: %ld", (long)self.timerInteger);
    }
    else  {
        [self.timer invalidate];
        self.timer = nil;
        [self.delegate hideHorizontalSelector];
        NSLog(@"Should hide");
    }
}

@end
