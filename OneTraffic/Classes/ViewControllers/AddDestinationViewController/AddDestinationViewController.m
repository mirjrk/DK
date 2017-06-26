//
//  AddDestinationViewController.m
//  OneTraffic
//
//  Created by Zesium on 2/10/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "AddDestinationViewController.h"
#import "Constant.h"
#import "CustomMGLPointAnnotation.h"
#import "UIHelper.h"
#import "Communication.h"
#import "LogicHelper.h"
#import "MBProgressHUD.h"


@interface AddDestinationViewController ()
@property BOOL alreadySelected;
@property BOOL userIsGoingToSearch;
@property (nonatomic, assign) BOOL calledWebservice;
@property NSString *lastTypedString;
@property NSDictionary *selectedAddress;
@property BOOL selectedAddressWithoutNumber;
@end

@implementation AddDestinationViewController


-(void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view= view;
    self.view.backgroundColor= DARK_BLUE;
    self.automaticallyAdjustsScrollViewInsets = NO;
    [self createInterface];
    
}
-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    self.userIsGoingToSearch = NO;
    self.navigationController.navigationBar.hidden = YES;
    
}
-(void)viewWillDisappear:(BOOL)animated
{
    self.navigationController.navigationBar.hidden = NO;
    [self.view endEditing:YES];
    
    
    [super viewWillDisappear:YES];
}

- (void)viewDidDisappear:(BOOL)animated {
    if (self.userIsGoingToSearch == NO) {
        [self.mapView removeFromSuperview];
        self.mapView = nil;
    }
    [super viewDidDisappear:YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setupKeyboardNotifications];
    [self predefineAddressByCoordinates];
    self.headerLabel.text = NSLocalizedString(@"AddPlace", nil);
    self.scrollView.frame = CGRectMake(0, CGRectGetMaxY(self.headerView.frame), SCREEN_WIDTH, SCREEN_HEIGHT);
    if (self.isEdit) {
        self.typeIndex = [self.destination.destinationType intValue];
        self.colorIndex = [self.destination.colorType intValue];
    }
}

-(void)createInterface
{
    
    NSString *styleUrl = MAP_FINAL_URL;
    NSURL *urlStyle = [NSURL URLWithString:styleUrl];
    
    self.scrollView= [[TPKeyboardAvoidingScrollView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(self.headerView.frame), SCREEN_WIDTH, SCREEN_HEIGHT)];
    [self.view addSubview:self.scrollView];
    
    self.destinationTextField = [UIHelper createTextFieldWithFrame:CGRectMake(SCREEN_WIDTH/2-(SCREEN_WIDTH-80)/2, 10, SCREEN_WIDTH-80, 40) placeHolder:@"" delegate:self];
    
    [UIHelper customizeTextField:self.destinationTextField withPlaceholder:NSLocalizedString(@"Street", nil)];
    self.destinationTextField.delegate = self;
    self.destinationTextField.tag = 1001;
    [self.destinationTextField addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventAllEvents];
    self.destinationTextField.autocapitalizationType = UITextAutocapitalizationTypeSentences;
    [self.scrollView addSubview:self.destinationTextField];
    
    self.numberTextField = [[UITextField alloc] initWithFrame:CGRectMake(0, 0, 60, 40)];
    [self.destinationTextField setRightViewMode:UITextFieldViewModeAlways];
    self.numberTextField.tag = 121;
    [self.numberTextField addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventAllEvents];
    [UIHelper customizeTextField:self.numberTextField withPlaceholder:@"#"];
    self.numberTextField.textAlignment = NSTextAlignmentCenter;
    self.numberTextField.keyboardType = UIKeyboardTypePhonePad;
    self.numberTextField.enabled = NO;
    [self.destinationTextField setRightView:self.numberTextField];
    UIToolbar *numberToolbarForNumber = [UIHelper createToolbarWithDoneButtonAndView:self.view];
    UIBarButtonItem *doneButtonForNumber = [[UIBarButtonItem alloc]initWithTitle:NSLocalizedString(@"Done", nil) style:UIBarButtonItemStyleDone target:self action:@selector(doneClicked:)];
    [doneButtonForNumber setTintColor:DARK_BLUE];
    numberToolbarForNumber.items = [NSArray arrayWithObjects: [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil], doneButtonForNumber, [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],nil];
    [numberToolbarForNumber sizeToFit];
    self.numberTextField.inputAccessoryView = numberToolbarForNumber;
    
    self.searchButton = [UIButton buttonWithType:UIButtonTypeCustom];
    self.searchButton.frame = CGRectMake(CGRectGetMaxX(self.destinationTextField.frame)+10, 15, 20, 20);
    [self.searchButton setImage:[UIImage imageNamed:@"searchIcon"] forState:UIControlStateNormal];
    [self.searchButton addTarget:self action:@selector(searchButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    
    self.typeImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 40, 40)];
    if (self.isEdit == YES) {
        int destinationType = [self.destination.destinationType intValue];
        int colorType = [self.destination.colorType intValue];
        [self.typeImageView setImage:[UIImage imageNamed:[LogicHelper setLineUpImageDependingOnTypeOfIcon:destinationType andColorType:colorType]]];
    }
    else {
        [self.typeImageView setImage:[UIImage imageNamed:@"HomePlacesRed"]];
    }
    self.typeImageView.userInteractionEnabled=YES;
    
    UITapGestureRecognizer *typeTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(setUpPicker)];
    [self.typeImageView addGestureRecognizer:typeTap];
    
    self.nameTextField= [UIHelper createTextFieldWithFrame:CGRectMake(self.destinationTextField.frame.origin.x, CGRectGetMaxY(self.destinationTextField.frame)+10 , self.destinationTextField.frame.size.width, 40) placeHolder:NSLocalizedString(@"Name", nil) delegate:self];
    self.nameTextField.autocapitalizationType = UITextAutocapitalizationTypeWords;
    [UIHelper customizeTextField:self.nameTextField withPlaceholder:NSLocalizedString(@"NamePlace", nil)];
    UIView *paddingView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 40, 40)];
    [paddingView addSubview:self.typeImageView];
    [self.nameTextField setRightViewMode:UITextFieldViewModeAlways];
    [self.nameTextField setRightView:paddingView];
    [self.scrollView addSubview:self.nameTextField];
    
    self.saveButton = [UIButton buttonWithType:UIButtonTypeCustom];
    self.saveButton.frame=CGRectMake(SCREEN_WIDTH/2-(SCREEN_WIDTH-200)/2, CGRectGetMaxY(self.nameTextField.frame)+30, SCREEN_WIDTH-200, 50);
    self.saveButton.layer.cornerRadius = self.saveButton.frame.size.height/2;
    self.saveButton.layer.masksToBounds = YES;
    
    [self.saveButton setBackgroundColor:NEON_GREEN];
    self.saveButton.clipsToBounds = YES;
    
    [self.saveButton addTarget:self action:@selector(saveAction) forControlEvents:UIControlEventTouchUpInside];
    
    self.saveButton.titleLabel.font=[UIFont boldSystemFontOfSize:16];
    [self.saveButton setTitle:NSLocalizedString(@"Save",nil) forState:UIControlStateNormal];
    [self.saveButton setTitleColor:DARK_BLUE  forState:UIControlStateNormal];
    [self.scrollView addSubview:self.saveButton];
    
    
    self.mapView = [[MGLMapView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(self.saveButton.frame)+10, SCREEN_WIDTH, SCREEN_HEIGHT-(CGRectGetMaxY(self.saveButton.frame)+10)) styleURL:urlStyle];
    
    [self setUpMap];
    
    [self.scrollView setContentSize:CGSizeMake(SCREEN_WIDTH, CGRectGetMaxY(self.mapView.frame)+64)];
    
    self.pickerValuesArray = [LogicHelper makeLocalizableArrayOfKeys:@[@"Home", @"Work", @"Sports", @"Culture", @"School", @"Transportation", @"Commercial", @"Parking", @"Undefined", @"Family", @"Friends"]];
    
    
    self.englishPickerArray = @[@"Home", @"Work", @"Sports", @"Culture", @"School", @"Transportation", @"Commercial", @"Parking", @"Undefined", @"Family", @"Friends"];
    
    self.englishColorArray = @[@"Red", @"Dark Red", @"Green", @"Dark Green", @"Blue", @"Dark Blue", @"Orange", @"Dark Orange"];
    
    
    self.colorPickerArray = [LogicHelper makeLocalizableArrayOfKeys:@[@"Red", @"Dark Red", @"Green", @"Dark Green", @"Blue", @"Dark Blue", @"Orange", @"Dark Orange"]];
    
    
    
    if(self.isEdit && self.destination !=nil){
        
        self.nameTextField.text=self.destination.destinationName;
        
        self.myDestination= CLLocationCoordinate2DMake(self.destination.location.lat,self.destination.location.lon);
        
        CustomMGLPointAnnotation *hello = [[CustomMGLPointAnnotation alloc] init];
        hello.coordinate = CLLocationCoordinate2DMake(self.myDestination.latitude, self.myDestination.longitude);
        hello.title = self.destination.destinationName;
        // Add marker `hello` to the map
        [self.mapView addAnnotation:hello];
        
        self.searchTextField.text = [NSString stringWithFormat:@"%f,%f",self.myDestination.latitude, self.myDestination.longitude];
        
        CLLocationCoordinate2D coordinates[2];
        
        coordinates[0]= self.myLocation;
        coordinates[1]= self.myDestination;
        
        [self.mapView setVisibleCoordinates:coordinates count:2 edgePadding:UIEdgeInsetsMake(100, 100, 100, 100) animated:NO];
        [self.mapView selectAnnotation:hello animated:YES];
        
        
        [UIHelper showTutorialWithType:@"AddPlaceTutorial" onView:self.view];
        
    }
    
}


- (void)back {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)setUpPicker{
    
    if(self.nameTextField.isFirstResponder){
        
        [self.nameTextField resignFirstResponder];
    }
    
    if (self.containerView!=nil) {
        return;
    }
    
    self.containerView= [[UIView alloc] initWithFrame:CGRectMake(0, SCREEN_HEIGHT-245,SCREEN_WIDTH, 245)];
    self.containerView.backgroundColor=[UIColor redColor];
    
    UIToolbar* numberToolbar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0,SCREEN_WIDTH, 45)];
    numberToolbar.translucent = NO;
    numberToolbar.barTintColor = NEON_GREEN;
    UIBarButtonItem *doneButton = [[UIBarButtonItem alloc]initWithTitle:NSLocalizedString(@"Done", nil) style:UIBarButtonItemStyleDone target:self action:@selector(done)];
    [doneButton setTintColor:DARK_BLUE];
    
    numberToolbar.items = [NSArray arrayWithObjects: [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil], doneButton, [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil], nil];
    UITapGestureRecognizer *singleFingerTap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(done)];
    [numberToolbar addGestureRecognizer:singleFingerTap];
    
    
    if (self.pickerView == nil) {
        self.pickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 45, SCREEN_WIDTH, 200)];
        
        self.pickerView.showsSelectionIndicator=YES;
        self.pickerView.dataSource = self;
        self.pickerView.delegate = self;
        self.pickerView.tag = 101;
        [ self.pickerView setBackgroundColor:DARK_BLUE];
    }
    
    [self.containerView addSubview: numberToolbar];
    [self.containerView addSubview: self.pickerView];
    
    if (self.isEdit == YES && self.alreadySelected != YES) { //If editing location, select type
        int selectDestinationTypeRow = [self.destination.destinationType intValue];
        int selectColorType = [self.destination.colorType intValue];
        [self.pickerView selectRow:selectDestinationTypeRow inComponent:0 animated:YES];
        [self.pickerView selectRow:selectColorType inComponent:1 animated:YES];
        self.alreadySelected = YES;
    }
    [self.view addSubview: self.containerView];
    
}

-(void)done
{
    
    NSString *nameOfDestination = self.nameTextField.text;
    if (self.nameTextField.hasText) {
        if ([self.pickerValuesArray containsObject:nameOfDestination]) {
            self.nameTextField.text= self.pickerValuesArray[ [self.pickerView selectedRowInComponent:0]];
        }
    }
    else {
        self.nameTextField.text= self.pickerValuesArray[ [self.pickerView selectedRowInComponent:0]];
    }
    NSInteger selectedRowInComponentZero = [self.pickerView selectedRowInComponent:0];
    NSInteger selectedRowInComponentOne = [self.pickerView selectedRowInComponent:1];
    NSString *destinationName = [NSString stringWithFormat:@"%@Places%@", self.englishPickerArray[selectedRowInComponentZero], self.englishColorArray[selectedRowInComponentOne]];
    destinationName = [destinationName stringByReplacingOccurrencesOfString:@" " withString:@""];
    UIImage *placeType = [UIImage imageNamed:destinationName];
    [self.typeImageView setImage:placeType];
    
    self.typeIndex = [self.pickerView selectedRowInComponent:0];
    self.colorIndex = [self.pickerView selectedRowInComponent:1];
    [self.containerView removeFromSuperview];
    [self.pickerView removeFromSuperview];
    self.containerView=nil;
    
}


-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return 2;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    switch (component) {
        case 0:
            return self.pickerValuesArray.count;
            break;
            
        default:
            return self.colorPickerArray.count;
            break;
    }
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    NSString *title = @"";
    if (component == 0) {
        title = self.pickerValuesArray[row];
    }
    else {
        title = self.colorPickerArray[row];
    }
    
    return title;
    
}

- (NSAttributedString *)pickerView:(UIPickerView *)pickerView attributedTitleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    NSString *title;
    if (component == 0) {
        title = self.pickerValuesArray[row];
    }
    else {
        title = self.colorPickerArray[row];
    }
    NSAttributedString *attString = [[NSAttributedString alloc] initWithString:title attributes:@{NSForegroundColorAttributeName:NEON_GREEN}];
    
    return attString;
}

-(void)setUpMap
{
    self.mapView.showsUserLocation = YES;
    self.mapView.delegate = self;
    self.mapView.tintColor = NEON_GREEN;
    // set the map's center coordinate
    self.mapView.attributionButton.hidden=YES;
    self.mapView.logoView.hidden=YES;
    [self.scrollView addSubview:self.mapView];
    
    [self.mapView setCenterCoordinate:self.myLocation
                            zoomLevel:16
                             animated:NO];
    
    UITapGestureRecognizer *doubleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:nil];
    doubleTap.numberOfTapsRequired = 2;
    [self.mapView addGestureRecognizer:doubleTap];
    
    
    // delay single tap recognition until it is clearly not a double
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [singleTap requireGestureRecognizerToFail:doubleTap];
    [self.mapView addGestureRecognizer:singleTap];
    
}

-(void)handleSingleTap:(UITapGestureRecognizer *)tap
{
    
    [self.mapView removeAnnotations:self.mapView.annotations];
    if (self.isEdit != YES) {
        self.myDestination = [self.mapView convertPoint:[tap locationInView:self.mapView]
                                   toCoordinateFromView:self.mapView];
    }
    
    
    CustomMGLPointAnnotation *hello = [[CustomMGLPointAnnotation alloc] init];
    hello.coordinate = CLLocationCoordinate2DMake(self.myDestination.latitude, self.myDestination.longitude);
    hello.title = NSLocalizedString(@"MyPlace", );
    
    // Add marker `hello` to the map
    [self.mapView addAnnotation:hello];
    
    NSString *lat = [NSString stringWithFormat:@"%f", self.myDestination.latitude];
    NSString *lon = [NSString stringWithFormat:@"%f", self.myDestination.longitude];
    NSDictionary *location = @{@"lat":lat,
                               @"lon":lon};
    NSDictionary *dict = @{@"location":location};
    [self getAddress:dict];
    
    
    [self.mapView selectAnnotation:hello animated:YES];
    
    NSLog(@"You tapped at: %.5f, %.5f", self.myDestination.latitude, self.myDestination.longitude);
}



-(MGLAnnotationImage *)mapView:(MGLMapView *)mapView imageForAnnotation:(id<MGLAnnotation>)annotation
{
    MGLAnnotationImage *annotationImage;
    
    
    
    return annotationImage;
}


- (BOOL)mapView:(MGLMapView *)mapView annotationCanShowCallout:(id <MGLAnnotation>)annotation {
    return YES;
}

- (void)mapView:(MGLMapView *)mapView tapOnCalloutForAnnotation:(id <MGLAnnotation>)annotation{
    
}


- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField {
    
    if (textField.tag == 1001) {
        if (self.isEdit == YES) {
            return NO;
        }
        
        if (self.searchAddressTableView) {
            [self.searchAddressTableView removeFromSuperview];
        }
        self.addressesArray = [NSArray new];
        
        
        self.searchAddressTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(self.destinationTextField.frame), SCREEN_WIDTH, SCREEN_HEIGHT-CGRectGetMaxY(self.destinationTextField.frame))];
        self.searchAddressTableView.delegate = self;
        self.searchAddressTableView.dataSource = self;
        self.searchAddressTableView.alpha = 0.0f; //Will set to 1.0 with animations sliding in Table
        self.searchAddressTableView.backgroundColor = DARK_BLUE_TRANSPARENT;
        self.searchAddressTableView.tableFooterView = [UIView new];
        [self.scrollView addSubview:self.searchAddressTableView];
        self.destinationTextField.text = nil;
        self.numberTextField.text = nil;
        self.numberTextField.enabled = NO;
        
        [UIView animateWithDuration:0.2f animations:^{
            self.searchAddressTableView.alpha = 1.0f;
        } completion:^(BOOL finished) {
            
            
        }];
        
    }
    
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField{
    [textField layoutIfNeeded];
    if (textField.tag == 101) {
        
        
        NSString *string=self.searchTextField.text;
        NSCharacterSet *numSet = [NSCharacterSet decimalDigitCharacterSet];
        
        string =[string stringByTrimmingCharactersInSet:numSet] ;
        NSArray *array = [self.searchTextField.text componentsSeparatedByString:@","];
        
        NSDictionary *parameters = @{@"country":array[0],
                                     @"city":array[1],
                                     @"street":array[2],
                                     @"houseNumber":@"11"};
        
        [Communication getCoordinates:parameters successBlock:^(NSDictionary *response) {
            
            if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
                
                NSArray *locations = [response objectForKey:@"locations"];
                NSDictionary *loc = locations[0];
                
                CLLocationCoordinate2D location ;
                location.latitude = [[loc objectForKey:@"lat"] doubleValue];
                location.longitude = [[loc objectForKey:@"lon"] doubleValue];
                self.myDestination = CLLocationCoordinate2DMake(location.latitude, location.longitude);
                
                CustomMGLPointAnnotation *hello = [[CustomMGLPointAnnotation alloc] init];
                hello.coordinate = CLLocationCoordinate2DMake(location.latitude, location.longitude);
                hello.title = string;
                
                [self.mapView addAnnotation:hello];
                
                [self.mapView selectAnnotation:hello animated:YES];
            }
        } errorBlock:^(NSDictionary *error) {
            NSLog(@"%@", error);
        }];
        
        __unused BOOL valid = [string isEqualToString:@""];
    }
    
    
    if (textField.tag == 121) {
        [self removeSearchTable];
        
    }
    
}
- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    
    if (self.searchAddressTableView) {
        [self removeSearchTable];
    }
    [textField resignFirstResponder];
    return NO;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)search{
    
    
    
}
-(BOOL)isValidLatLong:(NSString *)checkString
{
    NSString *stricterFilterString = @"^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$";
    
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", stricterFilterString];
    return [emailTest evaluateWithObject:checkString];
}

-(void)saveAction{
    [self.view endEditing:YES];
    if (self.nameTextField.text.length > 25) {
        UIAlertController *alert = [UIHelper showAlertControllerWithTitle:@"Name is too long" andText:@"Max character count is 25."];
        [self presentViewController:alert animated:YES completion:nil];
        return;
    }
    
    
    
    
    if(self.isEdit){
        
        [self editAction];
        
    }else{
        [self createAction];
        
        
    }
    
    
}
-(void)createAction {
    
    
    if ( ![self inputIsValid] ) { return; }
    
    
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    hud.removeFromSuperViewOnHide = YES;
    
    if ([[AFNetworkReachabilityManager sharedManager] isReachable]) {
        
        NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
        NSString *userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
        
        [parameters setObject:userId forKey:@"userId"];
        [parameters setObject:self.nameTextField.text forKey:@"name"];
        [parameters setObject:[NSString stringWithFormat:@"%li", (long)self.typeIndex ] forKey:@"type"];
        
        [parameters setObject:[NSString stringWithFormat:@"%ld", (long)self.colorIndex] forKey:@"colorType"];
        NSMutableDictionary *location=[NSMutableDictionary dictionaryWithObjects:@[[NSNumber numberWithDouble:self.myDestination.latitude],[NSNumber numberWithDouble:self.myDestination.longitude]] forKeys:@[@"lat",@"lon"]];
        
        
        [parameters setObject:location forKey:@"location"];
        
        [Communication addDestinationWithParameters:parameters successfulBlock:^(NSDictionary *response) {
            
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    
                    if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
                        
                        [[NSNotificationCenter defaultCenter] postNotificationName:@"placeAdded" object:self];
                        hud.mode = MBProgressHUDModeText;
                        [hud setCenter:self.view.center];
                        if (self.isEdit == YES) {
                            hud.labelText = @"Place edited.";
                        }
                        else {
                            hud.labelText = @"Place added.";
                        }
                        
                        
                        [hud hide:YES afterDelay:1];
                        [self performSelector:@selector(removeScreen) withObject:nil afterDelay:1];
                        
                    }
                    else {
                        [hud hide:YES];
                        UIAlertController *alert = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"Error", nil) message:@"Destination not in zone!" preferredStyle:UIAlertControllerStyleAlert];
                        UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                            
                        }];
                        [alert addAction:okAction];
                        [self presentViewController:alert animated:YES completion:nil];
                    }
                    
                    
                    
                });
            });
            
            
            NSLog(@"%@", response);
            
        } errorBlock:^(NSDictionary *error) {
            
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    [hud hide:YES];
                });
            });
            
            
        }];
        
    }
    else { //There is no internet connection
        dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [hud hide:YES];
            });
        });
        UIAlertController *alert = [UIHelper showAlertControllerWithTitle:@"Error occurred" andText:@"There is no internet connection."];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
}
-(void)editAction{
    
    if ( ![self inputIsValid] ) { return; }
    
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    hud.removeFromSuperViewOnHide = YES;
    if ([[AFNetworkReachabilityManager sharedManager] isReachable]) {
        
        NSString *userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
        NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];
        [parameters setObject:userId forKey:@"userId"];
        [parameters setObject:self.destination.destinationId forKey:@"destId"];
        
        [parameters setObject:self.nameTextField.text forKey:@"name"];
        [parameters setObject:[NSString stringWithFormat:@"%li", (long)self.typeIndex ] forKey:@"type"];
        
        NSMutableDictionary *location=[NSMutableDictionary dictionaryWithObjects:@[[NSNumber numberWithDouble:self.myDestination.latitude],[NSNumber numberWithDouble:self.myDestination.longitude]] forKeys:@[@"lat",@"lon"]];
        
        
        [parameters setObject:location forKey:@"location"];
        [parameters setObject:[NSString stringWithFormat:@"%ld", (long)self.colorIndex] forKey:@"colorType"];
        [Communication editDestinationWithParameters:parameters successfulBlock:^(NSDictionary *response) {
            
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    
                    if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
                        
                        [[NSNotificationCenter defaultCenter] postNotificationName:@"placeAdded" object:self];
                        
                        hud.mode = MBProgressHUDModeText;
                        [hud setCenter:self.view.center];
                        if (self.isEdit == YES) {
                            hud.labelText = @"Place edited.";
                        }
                        else {
                            hud.labelText = @"Place added.";
                        }
                        [self performSelector:@selector(removeScreen) withObject:nil afterDelay:1];
                    }
                    [hud hide:YES afterDelay:1];
                    
                });
            });
            
            
            NSLog(@"%@", response);
            
        } errorBlock:^(NSDictionary *error) {
            
            
            dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    [hud hide:YES];
                });
            });
            
            
        }];
        
    }
    else { //There is no internet connection
        dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [hud hide:YES];
            });
        });
        
        UIAlertController *alert = [UIHelper showAlertControllerWithTitle:@"Error occurred" andText:@"There is no internet connection."];
        [self presentViewController:alert animated:YES completion:nil];
    }
    
    
    
    
    
}
-(BOOL) inputIsValid {
    // trim the ends of our email + username
    self.nameTextField.text = [LogicHelper trimmedString:self.nameTextField.text];
    NSLog(@"%@", self.nameTextField.text);
    NSString *errorTitle = @"Error";
    if (self.nameTextField.text.length < 1) {
        [self presentViewController:[UIHelper showAlertControllerWithTitle:@"Error" andText:@"Please enter name for destination"] animated:NO completion:nil];
        return NO;
    }
    
    if ( self.myDestination.latitude==0 || self.myDestination.longitude==0 ) {
        
        CLLocationCoordinate2D location = self.myLocation;
        
        self.myDestination = location;
        
        return YES;
        
        
    }
    
    if ( IS_EMPTY_STRING(self.nameTextField.text ) ) {
        
        UIAlertController *alert = [UIHelper showAlertControllerWithTitle:errorTitle andText:@"Please enter a name."];
        [self presentViewController:alert animated:YES completion:nil];
        return NO;
        
    }
    
    return YES;
}


-(void)searchButtonTapped:(UIButton *)sender
{
    
    [self.mapView removeAnnotations:self.mapView.annotations];
    [self.view endEditing:YES];
    
    
    NSString *string=self.destinationTextField.text;
    NSCharacterSet *numSet = [NSCharacterSet decimalDigitCharacterSet];
    
    
    string =[string stringByTrimmingCharactersInSet:numSet];
    NSArray *array = [self.destinationTextField.text componentsSeparatedByString:@","];
    
    if (array.count != 4) {// Address not valid
        UIAlertController *alert = [UIHelper showAlertControllerWithTitle:NSLocalizedString(@"Error", nil) andText:@"Address is not valid. Format: Country, City, Address, Number"];
        [self presentViewController:alert animated:YES completion:nil];
    }
    else {
        NSDictionary *parameters = @{@"country":array[0],
                                     @"city":array[1],
                                     @"street":array[2],
                                     @"houseNumber":array[3]};
        
        [Communication getCoordinates:parameters successBlock:^(NSDictionary *response) {
            
            if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
                
                NSArray *locations = [response objectForKey:@"locations"];
                if (locations != (id)[NSNull null] && locations.count) {
                    NSDictionary *loc = locations[0];
                    CLLocationCoordinate2D location ;
                    location.latitude = [[loc objectForKey:@"lat"] doubleValue];
                    location.longitude = [[loc objectForKey:@"lon"] doubleValue];
                    self.myDestination = CLLocationCoordinate2DMake(location.latitude, location.longitude);
                    
                    CustomMGLPointAnnotation *hello = [[CustomMGLPointAnnotation alloc] init];
                    hello.coordinate = CLLocationCoordinate2DMake(location.latitude, location.longitude);
                    hello.title = self.streetTextField.text;
                    
                    // Add marker `hello` to the map
                    [self.mapView addAnnotation:hello];
                    [self.mapView setCenterCoordinate:hello.coordinate animated:YES];
                    
                    [self.mapView selectAnnotation:hello animated:YES];
                }
            }
            
        } errorBlock:^(NSDictionary *error) {
            NSLog(@"%@", error);
        }];
    }
    
}

-(void)predefineAddressByCoordinates
{
    [self showSpinner:YES];
    
    if(self.isEdit) {
        NSString *lat = [NSString stringWithFormat:@"%f", self.myDestination.latitude];
        NSString *lon = [NSString stringWithFormat:@"%f", self.myDestination.longitude];
        NSDictionary *location = @{@"lat":lat,
                                   @"lon":lon};
        NSDictionary *dict = @{@"location":location};
        
        [self getAddress:dict];
    }
    else {
        NSString *lat = [NSString stringWithFormat:@"%f", self.myLocation.latitude];
        NSString *lon = [NSString stringWithFormat:@"%f", self.myLocation.longitude];
        NSDictionary *location = @{@"lat":lat,
                                   @"lon":lon};
        NSDictionary *dict = @{@"location":location};
        
        [self getAddress:dict];
        
    }
}

- (void)getAddress:(NSDictionary *)dict
{
    [Communication getAddressByCoordinates:dict successBlock:^(NSDictionary *response) {
        [self showSpinner:NO];
        
        NSString *city = @"";
        NSString *street = @"";
        if ([response objectForKey:@"city"] != (id)[NSNull null]) {
            city = [response objectForKey:@"city"];
        }
        if ([response objectForKey:@"street"] != (id)[NSNull null]) {
            street = [response objectForKey:@"street"];
        }
        
        self.destinationTextField.text = [NSString stringWithFormat:@"%@, %@",  city, street];
        
        if ([response objectForKey:@"houseNumber"] != (id)[NSNull null]) {
            self.numberTextField.text = [response objectForKey:@"houseNumber"];
        }
        
    } errorBlock:^(NSDictionary *error) {
        [self showSpinner:NO];
    }];
}


-(void)selectedAddressFromServer:(NSDictionary *)address
{
    [self.mapView removeAnnotations:self.mapView.annotations];
    float latitudeRet = [[address objectForKey:@"lat"] floatValue];
    float longitudeRet = [[address objectForKey:@"lon"] floatValue];
    
    CustomMGLPointAnnotation *hello = [[CustomMGLPointAnnotation alloc] init];
    hello.coordinate = CLLocationCoordinate2DMake(latitudeRet, longitudeRet);
    [self divideAddress:address forTextField:self.destinationTextField];
    hello.title = [address objectForKey:@"addressLong"];
    
    // Add marker `hello` to the map
    [self.mapView addAnnotation:hello];
    
    [self.mapView setZoomLevel:14 animated:YES];
    [self.mapView setCenterCoordinate:hello.coordinate animated:YES];
    self.myDestination = CLLocationCoordinate2DMake(latitudeRet, longitudeRet);
}

-(void)removeScreen
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)setupKeyboardNotifications {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidShow:)
                                                 name:UIKeyboardDidShowNotification
                                               object:nil];
}

- (void)keyboardDidShow:(NSNotification *) notif{
    CGSize keyboardSize = [[[notif userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    //Given size may not account for screen rotation
    int height = MIN(keyboardSize.height,keyboardSize.width);
    //    int width = MAX(keyboardSize.height,keyboardSize.width);
    self.searchAddressTableView.contentInset = UIEdgeInsetsMake(0, 0, height+50, 0);
}


-(void)textFieldChanged:(UITextField *)tf
{
    
    if (tf.tag == 121) {
        if (self.numberTextField.text.length > 0) {
            [self updateSearchArray:tf.text andIsNumber:YES andIsRecursive:NO];
        }
        
    }
    else {
        if (self.selectedAddressWithoutNumber == YES) {
            return;
        }
        if (tf.text.length > 2) {
            [self updateSearchArray:tf.text andIsNumber:NO andIsRecursive:NO];
        }
        else {
            self.addressesArray = [NSArray new];
            [self.searchAddressTableView reloadData];
        }
    }
}

-(void)updateSearchArray:(NSString*)searchTextString andIsNumber:(BOOL)isNumber andIsRecursive:(BOOL)isRecursive
{
    
    if (self.calledWebservice == NO) {
        self.calledWebservice = YES;
        NSMutableDictionary *parameters = [NSMutableDictionary new];
        if (isNumber == YES) {
            parameters = [NSMutableDictionary dictionaryWithDictionary:@{@"address":self.destinationTextField.text}];
            if ([self.selectedAddress objectForKey:@"streetId"] != (id)[NSNull null] && [[self.selectedAddress objectForKey:@"streetId"] stringValue].length > 0) {
                [parameters setObject:[self.selectedAddress objectForKey:@"streetId"] forKey:@"streetId"];
            }
        }
        else {
            parameters = [NSMutableDictionary dictionaryWithDictionary:@{@"address":searchTextString}];
        }
        
        if (self.numberTextField.text.length > 0) {
            [parameters setObject:self.numberTextField.text forKey:@"number"];
        }
        self.lastTypedString = searchTextString; //We will check if we're presenting the last entered string
        
        [Communication getAddressesForAutocompleteWithParameters:parameters successBlock:^(NSDictionary *response) {
            
            self.calledWebservice = NO;
            if (isNumber == YES) {
                if (![self.lastTypedString isEqualToString:self.numberTextField.text] && self.destinationTextField.text.length > 0) {
                    [self updateSearchArray:self.numberTextField.text andIsNumber:YES andIsRecursive:YES];
                }
                else {
                    NSArray *address = [response objectForKey:@"AddressBook"];
                    if (address.count > 0) {
                        NSDictionary *selectedAddress = address[0];
                        [self selectedAddressFromServer:selectedAddress];
                    }
                }
            }
            else {
                self.addressesArray = [response objectForKey:@"AddressBook"];
                [self.searchAddressTableView reloadData];
                if (![self.lastTypedString isEqualToString:self.destinationTextField.text] && self.destinationTextField.text.length > 2) {
                    [self updateSearchArray:self.destinationTextField.text andIsNumber:NO andIsRecursive:YES];
                }
            }
        } errorBlock:^(NSDictionary *error) {
            
        }];
    }
}



#pragma mark - UITableViewDelegate and DataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (self.addressesArray.count > 0) {
        return self.addressesArray.count;
    }
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"addressCell"];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"addressCell"];
        cell.backgroundColor = [UIColor clearColor];
        cell.textLabel.textColor = [UIColor whiteColor];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    
    if (self.addressesArray.count > 0) {
        cell.textLabel.textAlignment = NSTextAlignmentLeft;
        NSDictionary *dict = self.addressesArray[indexPath.row];
        
        NSArray *addressDivided = [[dict objectForKey:@"addressLong"] componentsSeparatedByString:@","];
        
        if (addressDivided.count > 1) {
            cell.textLabel.text = [NSString stringWithFormat:@"%@, %@", addressDivided[0], addressDivided[1]];
        }
        
        else {
            cell.textLabel.text = [dict objectForKey:@"addressLong"];
        }
        
    }
    else {
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
        if (self.destinationTextField.text.length > 2) {
            cell.textLabel.text = NSLocalizedString(@"NoSearchResults", nil);
        }
        else {
            cell.textLabel.text = @"";
        }
        
    }
    
    if (self.selectedAddressWithoutNumber == YES) {
        cell.textLabel.text = @"";
    }
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.selectedAddressWithoutNumber != YES) {
        self.selectedAddress = self.addressesArray[indexPath.row];
        self.addressesArray = [NSArray new];
        self.selectedAddressWithoutNumber = YES;
        self.numberTextField.enabled = YES;
        [self.numberTextField becomeFirstResponder];
        [self.searchAddressTableView reloadData];
        self.calledWebservice = NO;
        
        [self selectedAddressFromServer:self.selectedAddress];
        
    }
    
    
}

- (void)removeSearchTable {
    [UIView animateWithDuration:0.2f
                     animations:^{self.searchAddressTableView.alpha = 0.0;}
                     completion:^(BOOL finished){
                         [self.searchAddressTableView removeFromSuperview];
                         self.searchAddressTableView = nil;}];
    self.selectedAddressWithoutNumber = NO;
    self.calledWebservice = NO;
}

- (void)doneClicked:(UIBarButtonItem *)sender {
    if (self.searchAddressTableView) {
        [self removeSearchTable];
    }
    [self.nameTextField becomeFirstResponder];
    [self.view endEditing:YES];
}

- (void)divideAddress:(NSDictionary *)dict forTextField:(UITextField *)textField {
    NSArray *addressDivided = [[dict objectForKey:@"addressLong"] componentsSeparatedByString:@","];
    
    if (addressDivided.count > 1) {
        textField.text = [NSString stringWithFormat:@"%@, %@", addressDivided[1], addressDivided[0]];
    }
    else {
        textField.text = [dict objectForKey:@"addressLong"];
    }
}

@end
