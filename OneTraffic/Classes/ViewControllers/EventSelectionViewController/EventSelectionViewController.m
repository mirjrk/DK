//
//  EventSelectionViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 2/8/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "EventSelectionViewController.h"
#import "Constant.h"
#import "EventCollectionViewCell.h"
#import "Communication.h"
#import "UIHelper.h"
#import "LogicHelper.h"

@interface EventSelectionViewController ()
@end

@implementation EventSelectionViewController

-(void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT-80)];
    self.view= view;
    self.view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"backgroundImage"]];
}

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    self.headerLabel.text = NSLocalizedString(@"ReportEvent", nil);
    
    self.eventNamesArray = [LogicHelper makeLocalizableArrayOfKeys:@[@"Undefined", @"Accident", @"Hazard", @"ClosedRoad", @"Police", @"RoadEvent", @"RoadWork", @"Traffic", @"Weather"]];
    
    self.imagesArray = @[@"UndefinedWhite", @"AccidentWhite", @"AttentionWhite", @"ClosedRoadWhite", @"PoliceWhite", @"RoadEventWhite", @"RoadWorkWhite", @"TrafficWhite", @"WeatherWhite"];
    self.backButton.hidden = YES;
    CGRect collectionFrame = CGRectMake(0, CGRectGetMaxY(self.headerView.frame), SCREEN_WIDTH, SCREEN_HEIGHT-60);
    self.eventCollection = [[UICollectionView alloc] initWithFrame:collectionFrame collectionViewLayout:[self setUpCollectionViewFlow]];
    
    [self.eventCollection registerClass:[EventCollectionViewCell class] forCellWithReuseIdentifier:@"cellIdentifier"];
    [self.eventCollection setBackgroundColor:[UIColor clearColor]];
    self.eventCollection.delegate = self;
    self.eventCollection.dataSource = self;
    
    [self.view addSubview:self.eventCollection];
    
    [UIHelper showTutorialWithTypeOfPlus:@"ReportEventTutorial" onView:[UIApplication sharedApplication].keyWindow];
}

-(void)dismissButtonPressed
{
    [self dismissViewControllerAnimated:YES completion:nil];
}


-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:YES];
}

#pragma mark-UICollectionView Methods
-(NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.imagesArray.count;
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    EventCollectionViewCell *cell= (EventCollectionViewCell *)[self.eventCollection dequeueReusableCellWithReuseIdentifier:@"cellIdentifier" forIndexPath:indexPath];
    [cell.eventImage setImage:[UIImage imageNamed:self.imagesArray[indexPath.row]]];
    cell.eventName.text = self.eventNamesArray[indexPath.row];
    cell.backgroundColor=[UIColor clearColor];
    cell.highlight.hidden = YES;
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake(SCREEN_WIDTH/3-5, SCREEN_WIDTH/3-5);
}

-(UICollectionViewFlowLayout*) setUpCollectionViewFlow
{
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [flowLayout setSectionInset:UIEdgeInsetsMake(0, 0, 0, 0)];
    flowLayout.minimumInteritemSpacing = 0;
    flowLayout.minimumLineSpacing = 10;
    
    return flowLayout;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if (indexPath.row == 9 || indexPath.row == 11) {
        return;
    }
    self.selectedIndexPath = indexPath.row; //Getting current index
    
    /*
     * Asking for confirmation, because user can accidentaly press some button while driving.
     */
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    hud.removeFromSuperViewOnHide = YES;
    
    NSDictionary *parameters = @{@"magnitude":@2,
                                 @"cause":@(self.selectedIndexPath),
                                 @"location":@{@"lat":@(self.lat),
                                               @"lon":@(self.lon)}};
    [Communication addEventWithParameters:parameters successfulBlock:^(NSDictionary *response) {
        
        
        
        hud.mode = MBProgressHUDModeText;
        [hud setCenter:self.view.center];
        hud.labelText = @"Thanks for participating!";
        
        
        
        [hud hide:YES afterDelay:1];
        [self performSelector:@selector(removeScreen) withObject:nil afterDelay:1];
        
        
    } errorBlock:^(NSDictionary *error) {
        
        
        dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [hud hide:YES];
                
                
                UIAlertController *alert = [UIHelper showAlertControllerWithTitle:@"Error" andText:@"Error happened. Please try again."];
                [self presentViewController:alert animated:YES completion:nil];
                
            });
        });
        
        
    }];
    
}


-(void)backAction
{
    [self.navigationController popToRootViewControllerAnimated:YES];
}

-(void)removeScreen
{
    [self.delegate eventAdded];
}
@end
