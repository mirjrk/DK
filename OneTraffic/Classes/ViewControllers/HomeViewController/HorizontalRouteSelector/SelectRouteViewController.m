//
//  SelectRouteViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 3/14/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "SelectRouteViewController.h"
#import "Constant.h"
#import "HomeViewController.h"
#import "MainRouteModel.h"
#import "SelectRouteCell.h"
#import "LogicHelper.h"

@interface SelectRouteViewController ()

@end

@implementation SelectRouteViewController

-(void)loadView
{
    self.view = [[UIView alloc] initWithFrame:CGRectMake(0, -SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view.backgroundColor=[UIColor clearColor];

    [self createInterface];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)createInterface
{
    self.routes = [self.allRoutesData objectForKey:@"routes"];

    self.routesTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 53, SCREEN_WIDTH, (self.routes.count+1)*53)];
    self.routesTableView.delegate = self;
    self.routesTableView.dataSource = self;
    self.routesTableView.backgroundColor = DARK_BLUE_TRANSPARENT;
    self.routesTableView.separatorStyle = UITableViewCellSeparatorStyleNone;


    [self.view addSubview:self.routesTableView];
    
    [self.routesTableView reloadData];

    self.clearView= [[UIView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(self.routesTableView.frame), SCREEN_WIDTH, SCREEN_HEIGHT-CGRectGetMaxY(self.routesTableView.frame))];
    self.clearView.backgroundColor= [UIColor clearColor];
    
    UITapGestureRecognizer *tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(cancel)];
    [tapRecognizer setNumberOfTapsRequired:1];
    [tapRecognizer setDelegate:self];
    
    [self.clearView addGestureRecognizer:tapRecognizer];
    
    [self.view addSubview:self.clearView];

}


#pragma mark - TableView Delegate and DataSource methods
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.routes.count+1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    
    SelectRouteCell *cell = (SelectRouteCell *)[tableView dequeueReusableCellWithIdentifier:@"SelectRouteCell"];
    if (cell == nil) {
        cell = [[SelectRouteCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"SelectRouteCell"];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        UIView *line = [[UIView alloc]initWithFrame:CGRectMake(0, 52.5, SCREEN_WIDTH, 0.5)];
       line.backgroundColor = [UIColor lightGrayColor];
        [cell addSubview:line];
    }
    
    if(indexPath.row == self.routes.count){
      
        [cell.destinationImageView setImage:[UIImage imageNamed: @"inappropriateButton"]];
        cell.destinationLabel.text = NSLocalizedString(@"Stop", nil);
  
    }else{
        MainRouteModel *route =[[MainRouteModel alloc]initWithDictionary:self.routes[indexPath.row]];
        NSArray *array = [NSMutableArray arrayWithArray:route.reportListOfRoute];
        
        for ( NSMutableDictionary *dict in array ) {
            
            if([dict objectForKey:@"type"]!=nil &&[[dict objectForKey:@"type"] integerValue]==4 ){
                
                EventModel *event = [[EventModel alloc] initWithDictionary:dict];
                [cell.destinationImageView setImage:[UIImage imageNamed:[LogicHelper setLineUpImageDependingOnTypeOfIcon:event.typeOfIcon andColorType:[event.colorType intValue]]]];
                cell.destinationLabel.text = [NSString stringWithFormat:@"%@",[LogicHelper createTopMenuStringFromDestination:event]];
                
            }
            
        }

    }

    return cell;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return 53;
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    
    if(indexPath.row == self.routes.count){
        
        [self remove];
        
    }else{
        MainRouteModel *mainRoute = [[MainRouteModel alloc] initWithDictionary:self.routes[indexPath.row]];
        if ([self.delegate respondsToSelector:@selector(routeSelected:)]) {
            [self.delegate routeSelected:mainRoute];
        }
    }

}

-(void)cancel{
    
    if ([self.delegate respondsToSelector:@selector(cancelSelectRouteController)]) {
        [self.delegate cancelSelectRouteController];
    }

    
}
-(void)remove{
    
    if ([self.delegate respondsToSelector:@selector(removeDestination)]) {
        [self.delegate removeDestination];
    }
    
    
}
@end
