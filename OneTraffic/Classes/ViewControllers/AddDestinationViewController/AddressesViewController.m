//
//  AddressesViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 6/27/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "AddressesViewController.h"
#import "Constant.h"
#import "UIHelper.h"
#import "LogicHelper.h"
#import "Communication.h"
#import "UIHelper.h"


@interface AddressesViewController ()
@property (nonatomic, assign) BOOL calledWebservice;
@property BOOL selectedAddressWithoutNumber;
@property NSString *lastTypedString;
@property NSDictionary *selectedAddress;
@property UITextField *numberTextField;
@end

@implementation AddressesViewController

-(void)loadView
{
    UIView *view= [[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view = view;
    self.view.backgroundColor = DARK_BLUE;
    self.automaticallyAdjustsScrollViewInsets = NO;
}


-(void)viewWillDisappear:(BOOL)animated
{
    [self.addressesFilterTextField resignFirstResponder];
    
    [super viewWillDisappear:YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.calledWebservice = NO; //Just for the first time
    self.headerLabel.text = NSLocalizedString(@"Address", nil);
    [self createInterface];
    [self setupKeyboardNotifications];
    
    // Do any additional setup after loading the view.
}

- (void)setupKeyboardNotifications {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidShow:)
                                                 name:UIKeyboardDidShowNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidHide:)
                                                 name:UIKeyboardDidHideNotification
                                               object:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)createInterface
{
    self.addressesFilterTextField = [UIHelper createTextFieldWithFrame:CGRectMake(SCREEN_WIDTH/2-(SCREEN_WIDTH-80)/2, CGRectGetMaxY(self.headerView.frame)+10, SCREEN_WIDTH-80, 40) placeHolder:@"Search" delegate:self];
    [self.addressesFilterTextField addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventAllEvents];
    self.addressesFilterTextField.autocorrectionType = UITextAutocorrectionTypeNo;
    [UIHelper customizeTextField:self.addressesFilterTextField withPlaceholder:@"Address"];
    
    self.numberTextField = [[UITextField alloc] initWithFrame:CGRectMake(0, 0, 60, 40)];
    [self.addressesFilterTextField setRightViewMode:UITextFieldViewModeAlways];
    self.numberTextField.tag = 121;
    [self.numberTextField addTarget:self action:@selector(textFieldChanged:) forControlEvents:UIControlEventAllEvents];
    [UIHelper customizeTextField:self.numberTextField withPlaceholder:@"#"];
    self.numberTextField.textAlignment = NSTextAlignmentCenter;
    self.numberTextField.keyboardType = UIKeyboardTypePhonePad;
    [self.addressesFilterTextField setRightView:self.numberTextField];
    
    
    self.addressesFilterTextField.autocapitalizationType = UITextAutocapitalizationTypeSentences;
    
    [self.view addSubview:self.addressesFilterTextField];
    
    
    self.addressesTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(self.addressesFilterTextField.frame), SCREEN_WIDTH, SCREEN_HEIGHT-CGRectGetMaxY(self.addressesFilterTextField.frame))];
    self.addressesTableView.backgroundColor = [UIColor clearColor];
    self.addressesTableView.delegate = self;
    self.addressesTableView.dataSource = self;
    self.addressesTableView.tableFooterView = [UIView new];
    self.addressesTableView.contentInset = UIEdgeInsetsMake(0, 0, 20, 0);
    [self.view addSubview:self.addressesTableView];
    
    self.numberTextField.enabled = NO;
    
}

#pragma mark - TableViewDelegate Methods

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    
    if (self.addressesArray.count > 0) { //Idea here is to present user text please enter your query, or no results, so this is checking for that
        return self.addressesArray.count;
    }
    
    return 1;
    
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
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
        NSString *number = [dict objectForKey:@"number"];
        if (number != (id)[NSNull null]) {
            if (number.length > 0 ) {
                cell.textLabel.text = [NSString stringWithFormat:@"%@, %@", [dict objectForKey:@"number"], [dict objectForKey:@"addressLong"]];
            }
            else {
                cell.textLabel.text = [dict objectForKey:@"addressLong"];
            }
        }
        
        else {
            cell.textLabel.text = [dict objectForKey:@"addressLong"];
        }
        
    }
    else {
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
        if (self.addressesFilterTextField.text.length > 2) {
            cell.textLabel.text = NSLocalizedString(@"NoSearchResults", nil);
        }
        else {
            //            cell.textLabel.text = @"Please enter your search query.";
            cell.textLabel.text = @"";
        }
        
    }
    
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.selectedAddressWithoutNumber != YES) {
        self.selectedAddress = self.addressesArray[indexPath.row];
        self.addressesArray = @[self.selectedAddress];
        self.selectedAddressWithoutNumber = YES;
        self.numberTextField.enabled = YES;
        self.addressesFilterTextField.text = [self.selectedAddress objectForKey:@"addressLong"];
        [self.numberTextField becomeFirstResponder];
        [self.addressesTableView reloadData];
    }
    else {
        NSDictionary *address = self.addressesArray[indexPath.row];
        [self.delegate selectedAddressFromServer:address];
        [self backAction];
    }
    
}

-(void)textFieldChanged:(UITextField *)tf
{
    
    if (tf.tag == 121) {
        if (self.numberTextField.text.length > 0) {
            [self updateSearchArray:tf.text];
        }
    }
    else {
        
        if (tf.text.length > 2) {
            [self updateSearchArray:tf.text];
        }
        else {
            self.addressesArray = [NSArray new];
            [self.addressesTableView reloadData];
        }
    }
}


-(void)updateSearchArray:(NSString*)searchTextString {
    
    if (self.calledWebservice == NO) {
        self.calledWebservice = YES;
        NSMutableDictionary *parameters = [NSMutableDictionary dictionaryWithDictionary:@{@"address":searchTextString}];
        if (self.numberTextField.text.length > 0) {
            [parameters setObject:self.numberTextField.text forKey:@"number"];
        }
        self.lastTypedString = searchTextString; //We will check if we're presenting the last entered string
        
        [Communication getAddressesForAutocompleteWithParameters:parameters successBlock:^(NSDictionary *response) {
            self.addressesArray = [response objectForKey:@"AddressBook"];
            [self.addressesTableView reloadData];
            self.calledWebservice = NO;
            if (![self.lastTypedString isEqualToString:self.addressesFilterTextField.text] && self.addressesFilterTextField.text.length > 2) {
                [self updateSearchArray:self.addressesFilterTextField.text];
            }
        } errorBlock:^(NSDictionary *error) {
            
        }];
    }
}



- (void)keyboardDidShow:(NSNotification *) notif {
    CGSize keyboardSize = [[[notif userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    //Given size may not account for screen rotation
    int height = MIN(keyboardSize.height,keyboardSize.width);
    //    int width = MAX(keyboardSize.height,keyboardSize.width);
    self.addressesTableView.contentInset = UIEdgeInsetsMake(0, 0, height, 0);
}

- (void)keyboardDidHide:(NSNotification *) notif{
    self.addressesTableView.contentInset = UIEdgeInsetsZero;
}

-(void)backAction {
    
    [self.navigationController popViewControllerAnimated:NO];
}


- (void)clearButtonTapped:(UIButton *)sender {
    self.addressesFilterTextField.text = @"";
    self.addressesFilterTextField.rightView = nil;
    [self.addressesTableView reloadData];
    
}

@end
