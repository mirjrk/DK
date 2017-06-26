//
//  CountriesListViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 4/20/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "CountriesListViewController.h"
#import "LogicHelper.h"
#import "Constant.h"
#import "UIHelper.h"
#import "CountriesTableViewCell.h"

@interface CountriesListViewController ()

@end

@implementation CountriesListViewController


-(void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view= view;
    self.view.backgroundColor = DARK_BLUE;
    [self setupView];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.countries = [[NSArray alloc] initWithArray:[LogicHelper getAllCountries]]; //Getting all countries from class method that is returning array of JSON's.
    self.headerLabel.text = NSLocalizedString(@"SelectCountry", nil);
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.countries.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CountriesTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    NSDictionary *country = self.countries[indexPath.row];
    
    if (cell == nil) {
        cell = [[CountriesTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
        cell.backgroundColor = [UIColor clearColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    cell.countryNameLabel.text = [country objectForKey:kCountryName];
    cell.countryCodeLabel.text = [country objectForKey:kCountryCallingCode];
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *country = self.countries[indexPath.row];
    NSString *countryCode = [country objectForKey:kCountryCallingCode];
    [self.delegate countryCode:countryCode];
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)setupView
{
    self.countriesTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 64+SPACE_MARGIN, SCREEN_WIDTH, SCREEN_HEIGHT-64-SPACE_MARGIN)];
    self.countriesTableView.dataSource = self;
    self.countriesTableView.delegate = self;
    self.countriesTableView.allowsMultipleSelection = NO;
    self.countriesTableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.countriesTableView];
}

@end
