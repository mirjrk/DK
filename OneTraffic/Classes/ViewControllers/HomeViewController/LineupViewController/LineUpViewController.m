//
//  LineUpViewController.m
//  OneTraffic
//
//  Created by Zesium on 3/11/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "LineUpViewController.h"
#import "EventDetailsViewController.h"
#import "UIHelper.h"

#define CELL_HEIGHT 120

@interface LineUpViewController ()

@end

@implementation LineUpViewController


-(void)loadView
{
    self.view= [[UIView alloc] initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    [self createInterface];
    
}


-(void)createInterface{
    
    self.lineUpArray = [[NSMutableArray alloc]init];
    
    NSDictionary *myLocation = @{@"type":@-101}; //Type -101 is for myLocation
    [self.lineUpArray addObject:myLocation];
    
    
    self.lineUpTableView = [[UITableView alloc] initWithFrame:CGRectMake(0,  SCREEN_HEIGHT-80-120, SCREEN_WIDTH, 120)];
    self.lineUpTableView.delegate = self;
    self.lineUpTableView.dataSource = self;
    [self.lineUpTableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];
    self.lineUpTableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.lineUpTableView];
    
    [self.lineUpTableView setContentOffset:CGPointMake(0, CGFLOAT_MAX)];
    
    
    self.myPlacesButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.myPlacesButton setFrame:CGRectMake(SCREEN_WIDTH-60, self.view.frame.size.height-140 , 50, 50)];
    self.myPlacesButton.layer.cornerRadius = 20;
    self.myPlacesButton.clipsToBounds = YES;
    [self.myPlacesButton setBackgroundColor:DARK_BLUE];
    [self.myPlacesButton setImage:[UIImage imageNamed:@"myPlacesButton"] forState:UIControlStateNormal];
    [self.myPlacesButton addTarget:self action:@selector(myPlacesButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
//    [self.view addSubview:self.myPlacesButton];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
#pragma mark - TableViewMethods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.lineUpArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    EventModel *event = [[EventModel alloc] initWithDictionary:self.lineUpArray[indexPath.row]];
    
    if(event.type == -101){
        MyLocationCell *cell = (MyLocationCell *)[tableView dequeueReusableCellWithIdentifier:@"MyLocationCell"];
        if (cell == nil) {
            cell = [[MyLocationCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"MyLocationCell"];
            cell.backgroundColor = [UIColor clearColor];
            //        cell.accessoryView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"disclosureIndicator"]];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        return cell;
        
    }else{
        
        AlertCenterTableViewCell *cell = (AlertCenterTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"alertCenterCell"];
        
        if (cell == nil) {
            
            cell = [[AlertCenterTableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"alertCenterCell"];
            cell.backgroundColor = [UIColor clearColor];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        
        // in case that there
        if (indexPath.row == 0 || event.cause==100) {
            cell.topImageView.hidden = YES;
        }else{
            cell.topImageView.hidden = NO;
        }
        cell.bottomImageView.hidden = NO;
        
        if (event.type == 2) {
            
            cell.eventLabel.text = event.commercialUser;
        }
        else if (event.type == 4) {
            [cell.activityImageView setImage:[UIImage imageNamed:[LogicHelper setLineUpImageDependingOnTypeOfIcon:event.typeOfIcon andColorType:[event.colorType intValue]]]];
            cell.eventLabel.text = event.destinationName;
        }
        
        
        else { // Type is 1, so just set image depending on type of event
            if (event.magnitude == 0) {
                
                [cell.activityImageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%@", [LogicHelper setImageDependingOnId:event.cause]]]];
                
                cell.eventLabel.text = [LogicHelper setTitleDependingOnId:event.cause];
                
            }
            else {
                if (event.cause == 4 || event.cause == 8) {
                    [cell.activityImageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%@", [LogicHelper setImageDependingOnId:event.cause]]]];
                    
                    cell.eventLabel.text = [LogicHelper setTitleDependingOnId:event.cause];
                    
                }
                else {
                    [cell.activityImageView setImage:[UIImage imageNamed:[NSString stringWithFormat:@"%@Orange", [LogicHelper setImageDependingOnId:event.cause]]]];
                    
                    cell.eventLabel.text = [LogicHelper setTitleDependingOnId:event.cause];
                }
            }
            
        }
        
        [cell.timeLabel setAttributedText:[LogicHelper setAttributedStringDependingOnValue:event.temporalDistance andType:@"duration"]];
        [cell.distanceLabel setAttributedText:[LogicHelper setAttributedStringDependingOnValue:event.distance andType:@"length"]];
        
        if (event.type != 1) {
            cell.delayLabel.hidden = YES;
        }
        else {
            cell.delayLabel.hidden = NO;
            
            if (event.duration/60<1) {
                [cell.delayLabel setText:[[UIHelper setAttributedString:[NSString stringWithFormat:@"+ %0.f", event.duration] andType:@"sec"] string ]];
            }
            else {
                [cell.delayLabel setText:[[UIHelper setAttributedString:[NSString stringWithFormat:@"+ %0.f", event.duration/60] andType:@"min"]string ]];
            }
        }
        
        
        if (event.type == -100) {
            cell.hidden = YES;
        }
        else {
            cell.hidden = NO;
        }
        if (event.isDrivenThrough == YES) {
            cell.activityImageView.clipsToBounds = YES;
            cell.activityImageView.layer.cornerRadius = cell.activityImageView.frame.size.height/2;
            cell.activityImageView.layer.borderWidth = 5.0;
            cell.activityImageView.layer.borderColor = NEON_GREEN.CGColor;
            
            //There could be more of driven through events, so it needs to be checked and hide down arrows only on last event
            if (indexPath.row == self.lineUpArray.count-1) {
                cell.bottomImageView.hidden = YES;
            }
            
        }else{
            cell.activityImageView.layer.borderColor = [UIColor clearColor].CGColor;
        }
        
        
        [cell.eventLabel sizeToFit];
        
        [cell.delayLabel setFrame:CGRectMake(cell.eventLabel.frame.origin.x+cell.eventLabel.frame.size.width+2, cell.eventLabel.frame.origin.y, SCREEN_WIDTH-(CGRectGetMaxX(cell.eventLabel.frame)+CGRectGetMaxX(cell.activityImageView.frame)), cell.eventLabel.frame.size.height)];
        [cell.delayLabel sizeToFit];
        return cell;
        
    }
    
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    EventModel *model = [[EventModel alloc] initWithDictionary:self.lineUpArray[indexPath.row]];
    if (model.type == 1) {
        EventDetailsViewController *vc = [[EventDetailsViewController alloc] init];
        //        vc.eventId = model.eventId;
        //        vc.location = model.location;
        //        vc.type = model.type;
        //        vc.typeOfIcon = model.typeOfIcon;
        //        vc.cause = model.cause;
        //        vc.isDrivenThrough = model.isDrivenThrough;
        vc.event = model;
        [self.navigationController pushViewController:vc animated:YES];
    }
    
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return CELL_HEIGHT;
}


-(void)setUpLineUpArray:(NSArray*)array{
    
    self.lineUpArray= [NSMutableArray arrayWithArray:array];
    [self.lineUpTableView reloadData];
    [self setUpLineUpTableHeight];
    
    
    
}

-(void) setUpLineUpTableHeight{
    
    
    if(self.lineUpArray.count*CELL_HEIGHT >= SCREEN_HEIGHT-80-53){
        
        [self.lineUpTableView setFrame:CGRectMake(0, 53, SCREEN_WIDTH, SCREEN_HEIGHT-80-53)];
        
        
    }else{
        
        [self.lineUpTableView setFrame:CGRectMake(0, SCREEN_HEIGHT-80-self.lineUpArray.count*CELL_HEIGHT, SCREEN_WIDTH, self.lineUpArray.count*CELL_HEIGHT)];
        
    }
    
    
}


-(void)myPlacesButtonClicked:(UIButton *)sender
{
    [self.delegate myPlacesTapped];
}
@end
