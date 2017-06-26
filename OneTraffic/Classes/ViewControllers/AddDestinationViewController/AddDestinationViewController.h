//
//  AddDestinationViewController.h
//  OneTraffic
//
//  Created by Zesium on 2/10/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Mapbox/Mapbox.h>
#import "TPKeyboardAvoidingScrollView.h"
#import "AFNetworkReachabilityManager.h"
#import "DestinationModel.h"
#import "HomeViewController.h"
#import "BasicViewController.h"
#import "AddressesViewController.h"

@interface AddDestinationViewController : BasicViewController <MGLMapViewDelegate, UITextFieldDelegate,UIPickerViewDataSource, UIPickerViewDelegate, UIScrollViewDelegate,SelectedDestination, UITableViewDelegate, UITableViewDataSource>

@property(nonatomic) MGLMapView *mapView;

@property(nonatomic, strong) UITextField *destinationTextField;


@property(nonatomic, assign) CLLocationCoordinate2D myLocation;
@property(nonatomic, assign) CLLocationCoordinate2D myDestination;

@property(nonatomic, assign) NSInteger typeIndex;
@property(nonatomic, assign) NSInteger colorIndex;
@property(nonatomic, assign) BOOL isEdit;

@property(nonatomic,retain) DestinationModel *destination;

@property(nonatomic,retain) UITextField *searchTextField;
//@property(nonatomic,retain) UIButton *backButton;
@property(nonatomic,retain) UIButton *searchButton;
@property(nonatomic,retain) TPKeyboardAvoidingScrollView *scrollView;
@property(nonatomic,retain) UIImageView *typeImageView;
@property(nonatomic,retain) UITextField *nameTextField;
@property(nonatomic,retain) UIButton *saveButton;
@property(nonatomic,retain) UIView *containerView;

@property(nonatomic,retain) UIPickerView *pickerView;
//@property(nonatomic,retain) UIPickerView *colorPickerView;
@property(nonatomic,retain) NSArray *pickerValuesArray;
@property(nonatomic,retain) NSArray *colorPickerArray;
@property(nonatomic, strong) NSArray *englishPickerArray;
@property(nonatomic, strong) NSArray *englishColorArray;

@property(nonatomic, retain) UITextField *streetTextField;
@property(nonatomic, retain) UITextField *numberTextField;
@property(nonatomic, retain) UITextField *cityTextField;
@property(nonatomic, retain) UITextField *countryTextField;
@property(nonatomic, retain) UIButton *searchStreetButton;

//This is for showing addressess on one screen
@property(nonatomic, strong) UITableView *searchAddressTableView;
@property (nonatomic, strong) NSArray *addressesArray;
@end
