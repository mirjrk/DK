
//
//  DestinationListViewController.m
//  OneTraffic
//
//  Created by Zesium on 2/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "DestinationListViewController.h"
#import "Constant.h"
#import "Communication.h"
#import "DestinationModel.h"
#import "DestinationListCell.h"
#import "AddDestinationViewController.h"
#import "LogicHelper.h"

@interface DestinationListViewController ()

@end

@implementation DestinationListViewController


-(void)loadView{
    
    UIView *view=[[UIView alloc] initWithFrame:CGRectMake(0, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view=view;
    self.view.backgroundColor=DARK_BLUE_TRANSPARENT;
    
    [self createInterface];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.isCalledWebservice = NO; //Will be called further
    self.headerLabel.text = NSLocalizedString(@"Places", nil);
    UIButton *plusButton = [UIHelper createSystemButtonWithFrame:CGRectMake(SCREEN_WIDTH-60, self.headerView.frame.size.height-35, 30, 30) andTitle:nil];
    [plusButton setBackgroundImage:[UIImage imageNamed:@"plus"] forState:UIControlStateNormal];
    [self.headerView addSubview:plusButton];
    [plusButton addTarget:self action:@selector(addButtonTapped) forControlEvents:UIControlEventTouchUpInside];
    // Do any additional setup after loading the view.
}

-(void)viewWillAppear:(BOOL)animated{
    
    [super viewWillAppear:animated];
    self.navigationController.navigationBar.hidden = YES;
    [self getListOfDestinations];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void) createInterface
{
    self.dataArray= [[NSMutableArray alloc] init];
    self.listTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 64, SCREEN_WIDTH,  SCREEN_HEIGHT-64)];
    self.listTableView.delegate = self;
    self.listTableView.dataSource = self;
    self.listTableView.backgroundColor = [UIColor clearColor];
    self.listTableView.bounces=YES;
    self.listTableView.separatorStyle= UITableViewCellSeparatorStyleNone;
    [self.view addSubview:self.listTableView];
    
    [UIHelper showTutorialWithType:@"PlacesTutorial" onView:self.view];
}


-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    DestinationListCell *cell = (DestinationListCell *)[tableView dequeueReusableCellWithIdentifier:@"destinationCell"];
    
    if (cell == nil) {
        cell = [[DestinationListCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"destinationCell"];
        cell.backgroundColor=[UIColor clearColor];
        cell.selectionStyle= UITableViewCellSelectionStyleNone;
        cell.delegate = self;
        
    }
    
    DestinationModel *dest=[self.dataArray objectAtIndex:indexPath.row];
    int destType = [dest.destinationType intValue];
    int destColor = [dest.colorType intValue];
    [cell.destImageView setImage:[UIImage imageNamed:[LogicHelper setLineUpImageDependingOnTypeOfIcon:destType andColorType:destColor]]];
    cell.nameLabel.text = [NSString stringWithFormat:@"%@",dest.destinationName];
    cell.leftUtilityButtons = [self leftButtons];
    
    //  cell.textLabel.textAlignment=NSTextAlignmentLeft;
    
    return cell;
    
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [[NSNotificationCenter defaultCenter] postNotificationName:@"destinationChosed" object:self.dataArray[indexPath.row] userInfo:nil];
    [self.navigationController popToRootViewControllerAnimated:YES];
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 80;
}


#pragma mark - SWTableViewDelegate

- (void)swipeableTableViewCell:(SWTableViewCell *)cell didTriggerLeftUtilityButtonWithIndex:(NSInteger)index {
    
    NSIndexPath *indexPath = [self.listTableView indexPathForCell:cell];
    
    
    switch (index) {
        case 0:{
            
            AddDestinationViewController *advc = [[AddDestinationViewController alloc]init];
            
            if ( self.currentLocation.longitude !=0 && self.currentLocation.latitude !=0) { //There is some location
                
                advc.myLocation=self.currentLocation;
                advc.isEdit=YES;
                advc.destination=self.dataArray[indexPath.row];
                [self.navigationController pushViewController:advc animated:YES];
                
            }
            
            [self.listTableView reloadData];
            
            break;
        }
        case 1:
            
        {
            self.selectedIndexPathForDeleting = indexPath.row;
            UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"Done", nil) message:NSLocalizedString(@"DeletePlace", nil) preferredStyle:UIAlertControllerStyleAlert];
            UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                NSIndexPath *path = [NSIndexPath indexPathForRow:self.selectedIndexPathForDeleting inSection:0];
                [self deleteDestinationAtIndexPath:path];
                
                
            }];
            [alert addAction:okAction];
            
            UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Cancel", nil) style:UIAlertActionStyleDefault handler:nil];
            [alert addAction:cancelAction];
            [self presentViewController:alert animated:YES completion:nil];
            
            break;
        }
        default:
            break;
    }
}

- (BOOL)swipeableTableViewCellShouldHideUtilityButtonsOnSwipe:(SWTableViewCell *)cell{
    
    return YES;
}

- (NSArray *)leftButtons
{
    NSMutableArray *leftUtilityButtons = [NSMutableArray new];
    
    [leftUtilityButtons sw_addUtilityButtonWithColor:
     NEON_GREEN
                                               title:@"Edit"];
    [leftUtilityButtons sw_addUtilityButtonWithColor:
     [UIColor redColor]
                                               title:@"Delete"];
    
    
    return leftUtilityButtons;
}

-(void)getListOfDestinations{
    
    NSString *userIdFrom = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    NSMutableDictionary *parameters = [NSMutableDictionary new];
    [parameters setValue:userIdFrom forKey:@"userId"];
    
    
    [Communication getDestinationListWithParameters:parameters successfulBlock:^(NSDictionary *response) {
        
        
        NSLog(@"response: %@",response);
        
        
        self.dataArray= [[NSMutableArray alloc] init];
        
        if ( [response objectForKey:@"destinations"]!= (id)[NSNull null] && [response objectForKey:@"destinations"]) {
            
            NSArray *destination = [response objectForKey:@"destinations"];
            
            if (destination.count < 1) {
                [self.listTableView reloadData];
                
                self.listTableView.backgroundView = [UIHelper createTableBackgroundView:self.listTableView andText:@"No places! Add them by clicking plus."];
            }
            else {
                for (NSDictionary *location in destination) {
                    
                    DestinationModel *destination=[[DestinationModel alloc] initWithDictionary:location];
                    [self.dataArray addObject:destination];
                    
                }
                self.listTableView.backgroundView = nil;
                [self.listTableView reloadData];
                self.isCalledWebservice = YES;
            }
            
        }
        
    } errorBlock:^(NSDictionary *error) {
        
    }];
    
}



-(void)deleteDestinationAtIndexPath:(NSIndexPath*)indexPath{
    
    DestinationModel*destination= self.dataArray[indexPath.row];
    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
    
    //    [parameters setObject:destination.destinationName forKey:@"name"];
    //    [parameters setObject:destination.destinationType forKey:@"type"];
    [parameters setObject:destination.destinationId forKey:@"destId"];
    NSString *userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    [parameters setObject:userId forKey:@"userId"];
    //[parameters setObject:destination.zoneIndex forKey:@"zoneIndex"];
    
    //    NSMutableDictionary *locationDict=[NSMutableDictionary dictionaryWithObjects:@[[NSNumber numberWithFloat:destination.location.lat],[NSNumber numberWithFloat:destination.location.lon]] forKeys:@[@"lat",@"lon"]];
    
    //    [parameters setObject:locationDict forKey:@"location"];
    
    
    [Communication removeDestinationWithParameters:parameters  successfulBlock:^(NSDictionary *response) {
        
        
        if([[response objectForKey:ERROR_MESSAGE]integerValue]==0 ){
            
            [self.dataArray removeObjectAtIndex:indexPath.row];
            [[NSNotificationCenter defaultCenter] postNotificationName:@"placeAdded" object:self];
        }
        
        [self.listTableView reloadData];
        
    } errorBlock:^(NSDictionary *error) {
        
    }];
    
}

-(void)addButtonTapped
{
    AddDestinationViewController *advc = [[AddDestinationViewController alloc]init];
    if ( self.currentLocation.longitude !=0 && self.currentLocation.latitude !=0) { //There is some location
        advc.myLocation=self.currentLocation;
        [self.navigationController pushViewController:advc animated:YES];
    }
    [self.listTableView reloadData];
}

@end
