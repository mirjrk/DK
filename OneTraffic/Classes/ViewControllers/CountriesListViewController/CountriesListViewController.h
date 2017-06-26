//
//  CountriesListViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 4/20/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BasicViewController.h"

@protocol SelectedCountry<NSObject>
-(void)countryCode:(NSString *)code;
@end

@interface CountriesListViewController : BasicViewController <UITableViewDelegate, UITableViewDataSource>
@property (nonatomic, strong) UITableView *countriesTableView;
@property (nonatomic, strong) NSArray *countries;
@property (nonatomic, strong) NSDictionary *allCountries;
@property NSInteger selectedIndex;
@property (nonatomic, weak) id <SelectedCountry> delegate;
@end
