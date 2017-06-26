//
//  AddressesViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 6/27/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AppDelegate.h"
#import "BasicViewController.h"


@protocol SelectedDestination <NSObject>
-(void)selectedAddressFromServer:(NSDictionary *)address;
@end

@interface AddressesViewController : BasicViewController <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) UITextField *addressesFilterTextField;
@property (nonatomic, strong) UITableView *addressesTableView;
@property (nonatomic, strong) NSArray *addressesArray;

@property (nonatomic, weak) id <SelectedDestination> delegate;
@end
