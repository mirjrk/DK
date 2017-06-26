//
//  CommunicatorViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 2/28/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "CommunicatorViewController.h"
#import "Constant.h"
#import "Communication.h"
#import "CommunicatorTableViewCell.h"
#import "UIHelper.h"
#import "UIImageView+AFNetworking.h"

@interface CommunicatorViewController ()
@property (nonatomic, strong) NSMutableArray *communicatorArray;
@property BOOL settedUpTableView;
@property BOOL isLongPressActive;
@end

@implementation CommunicatorViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = DARK_BLUE;
    self.isLongPressActive = NO;
    [self setupKeyboardNotifications];
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:YES];
    self.navigationController.navigationBar.hidden = YES;
    [self getCommunicatorList];
    
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:YES];
    if (self.settedUpTableView != YES) {
        [self setupTableView];
        self.settedUpTableView = YES;
    }
}

- (void)viewWillDisappear:(BOOL)animated {
    [self.view endEditing:YES];
    [super viewWillDisappear:YES];
}


- (void)setupTableView {
    
    self.communicatorTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 20, SCREEN_WIDTH, self.view.frame.size.height-40)];
    
    self.communicatorTableView.backgroundColor = [UIColor clearColor];
    self.communicatorTableView.delegate = self;
    self.communicatorTableView.dataSource = self;
    
    //Register cell from nib
    [self.communicatorTableView registerNib:[UINib nibWithNibName:@"CommunicatorTableViewCell" bundle:nil] forCellReuseIdentifier:@"communicatorRequestCell"];
    
    //Deleting unneccessary empty lines with empty footer
    self.communicatorTableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
    self.communicatorTableView.separatorColor = DARK_BLUE_TRANSPARENT;
    [self.view addSubview:self.communicatorTableView];
    
    /*
     //Adding settings button to upper right corner
     self.settingsButton = [UIButton buttonWithType:UIButtonTypeCustom];
     [self.settingsButton setImage:[UIImage imageNamed:@"Friends"] forState:UIControlStateNormal];
     self.settingsButton.frame = CGRectMake(self.headerView.frame.size.width-65, self.headerView.frame.size.height/2-10, 35, 35);
     self.settingsButton.contentMode = UIViewContentModeScaleAspectFit;
     [self.settingsButton addTarget:self action:@selector(addFriendsButtonPressed) forControlEvents:UIControlEventTouchUpInside];
     [self.headerView addSubview:self.settingsButton];
     */
    
    UILongPressGestureRecognizer *longPressRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPress:)];
    longPressRecognizer.minimumPressDuration = 1.1;
    longPressRecognizer.delegate = self;
    [self.communicatorTableView addGestureRecognizer:longPressRecognizer];
}

- (void)longPress:(UILongPressGestureRecognizer *)recognizer {
    CGPoint p = [recognizer locationInView:self.communicatorTableView];
    
    NSIndexPath *indexPath = [self.communicatorTableView indexPathForRowAtPoint:p];
    
    QuickMessageViewController *quickMessageVc = [[QuickMessageViewController alloc] initWithNibName:@"QuickMessageViewController" bundle:nil];
    quickMessageVc.communicatorModel = self.communicatorArray[indexPath.row];
    quickMessageVc.delegate = self;
    quickMessageVc.indexPathRemembered = indexPath;
    [self presentViewController:quickMessageVc animated:YES completion:nil];
    
    
    self.isLongPressActive = YES;
    
    if (recognizer.state == UIGestureRecognizerStateCancelled
        || recognizer.state == UIGestureRecognizerStateFailed
        || recognizer.state == UIGestureRecognizerStateEnded) {
        
        [self performSelector:@selector(deactivateLongPress)
                   withObject:nil
                   afterDelay:0.2];
    }
    
}

- (void)deactivateLongPress {
    self.isLongPressActive = NO;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    return YES;
}


/*!
 * @discussion Getting all friends with last interaction and parsing to a model.
 */
- (void)getCommunicatorList {
    [Communication getCommunicatorList:@{} successBlock:^(NSDictionary *response) {
        
        if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
            NSArray *communicatorMessages = [response objectForKey:@"friends"];
            
            if (communicatorMessages.count > 0) {
                self.communicatorArray = [NSMutableArray new];
                for (NSDictionary *user in communicatorMessages) {
                    CommunicatorModel *model = [[CommunicatorModel alloc] initWithDictionary:user];
                    [self.communicatorArray addObject:model];
                }
                //Because of searching option to hold all users in one array
                self.allFriendsHolder = [self.communicatorArray copy];
                
                [self.communicatorTableView reloadData];
            }
            
            else {
                self.communicatorTableView.backgroundView = [UIHelper createTableBackgroundView:self.communicatorTableView andText:@"No messages and friends."];
            }
        }
        else {
            [self presentViewController:[UIHelper logoutAlert] animated:YES completion:nil];
        }
        
    } errorBlock:^(NSDictionary *error) {
        
    }];
}

#pragma mark - UITableViewDelegate
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.communicatorArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CommunicatorTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"communicatorRequestCell"];
    CommunicatorModel *model = self.communicatorArray[indexPath.row];
    cell.delegate = self;
    if (model.deletable) {
        cell.leftUtilityButtons = [self leftButtons];
    }
    else {
        cell.leftUtilityButtons = nil;
        
    }
    
    
    
    if (model.groupPictureUrl.length > 0 && model.groupPictureUrl != (id)[NSNull null]) {
        [cell.userImageView setImageWithURL:[NSURL URLWithString:model.groupPictureUrl]];
    }
    else {
        cell.userImageView.image = [UIHelper drawImageWithSnapshotWithFirstName:model.fromName andLastName:model.fromSurname];
    }
    if ([model.unreadMessages integerValue] > 0) {
        cell.redDotImageView.hidden = NO;
    }
    else {
        cell.redDotImageView.hidden = YES;
    }
    cell.nameLabel.text = model.groupName;
    cell.dateLabel.text = model.timestamp;
    cell.typeOfRequestLabel.text = model.finalTypeOfRequest;
    cell.statusLabel.text = model.typeOfResponseToRequest;
    
    return cell;
}

- (BOOL)swipeableTableViewCellShouldHideUtilityButtonsOnSwipe:(SWTableViewCell *)cell {
    return YES;
}

- (void)swipeableTableViewCell:(SWTableViewCell *)cell didTriggerLeftUtilityButtonWithIndex:(NSInteger)index {
    NSIndexPath *indexPath = [self.communicatorTableView indexPathForCell:cell];
    
    UIAlertController *confirmDelete = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"Delete", nil) message:NSLocalizedString(@"DeleteSure", nil)  preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        CommunicatorModel *model = self.communicatorArray[indexPath.row];
        
        NSDictionary *parameters = @{@"groupId":model.groupId,
                                     @"acceptFriendship":[NSNumber numberWithInt:-1]};
        
        [Communication friendRequestResponse:parameters successBlock:^(NSDictionary *response) {
            NSLog(@"SUCCESS ACCEPT: %@", parameters);
            [self getCommunicatorList];
        } errorBlock:^(NSDictionary *error) {
            NSLog(@"FAILURE ACCEPT: %@", parameters);
            
        }];
        
    }];
    
    UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Cancel", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    [confirmDelete addAction:okAction];
    [confirmDelete addAction:cancelAction];
    
    [self presentViewController:confirmDelete animated:YES completion:nil];
    
    
}

- (NSArray *)leftButtons {
    NSMutableArray *leftButtons = [NSMutableArray new];
    [leftButtons sw_addUtilityButtonWithColor:[UIColor redColor] title:@"Stop"];
    
    return leftButtons;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.isLongPressActive != YES) {
        CommunicatorModel *model = self.communicatorArray[indexPath.row];
        [self.delegate selectFriendPressed:model];
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 100;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *header = [[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 40)];
    header.backgroundColor = DARK_BLUE;
    self.searchTextField = [UIHelper createTextFieldWithFrame:CGRectMake(SCREEN_WIDTH/2-100, 0, 200, 40) placeHolder:NSLocalizedString(@"Search", nil) delegate:nil];
    [UIHelper customizeTextField:self.searchTextField withPlaceholder:NSLocalizedString(@"Search", nil)];
    self.searchTextField.delegate = self;
    [self.searchTextField addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventAllEvents];
    [header addSubview:self.searchTextField];
    
    return header;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 40;
}


#pragma mark - Custom methods
/*!
 * @discussion Delegating to Home, so it can push to it's navigation stack.
 * @param -
 * @return -
 */
- (void)addFriendsButtonPressed {
    [self.delegate addFriendsButtonPressedCommunicator];
    //    [self.delegate settingsButtonPressedCommunicator];
}

/*!
 * @discussion Overriding parent method. This is neccessary because this is not classic pop action, we need to dismiss Communicator and remove it from memory manually.
 */



#pragma mark - TextField Delegates and Keyboard

- (void)setupKeyboardNotifications {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidShow:)
                                                 name:UIKeyboardDidShowNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardDidHide:)
                                                 name:UIKeyboardDidHideNotification
                                               object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(getCommunicatorList)
                                                 name:@"refreshCommunicator"
                                               object:nil];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return NO;
}

- (void)keyboardDidShow:(NSNotification *) notif{
    CGSize keyboardSize = [[[notif userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
    
    int height = MIN(keyboardSize.height,keyboardSize.width);
    self.communicatorTableView.contentInset = UIEdgeInsetsMake(0, 0, height-80, 0);
}

- (void)keyboardDidHide:(NSNotification *) notif{
    self.communicatorTableView.contentInset = UIEdgeInsetsZero;
}


-(void)textFieldDidChange:(UITextField*)textField
{
    if (textField.text.length > 0) {
        textField.rightViewMode = UITextFieldViewModeAlways;
        
        UIButton *clear = [UIButton buttonWithType:UIButtonTypeCustom];
        [clear setImage:[UIImage imageNamed:@"error"] forState:UIControlStateNormal];
        [clear setFrame:CGRectMake(0, 0, 30, 30)];
        [clear addTarget:self action:@selector(clearTapped:) forControlEvents:UIControlEventTouchUpInside];
        
        textField.rightView = clear;
    }
    else {
        textField.rightView = nil;
    }
    [self updateSearchArray:textField.text];
}

- (void)clearTapped:(UIButton *)sender {
    [self.searchTextField resignFirstResponder];
    self.searchTextField.text = @"";
    [self updateSearchArray:@""];
}

-(void)updateSearchArray:(NSString*)searchTextString
{
    if (searchTextString.length != 0) {
        self.communicatorArray = [NSMutableArray array];
        
        for (CommunicatorModel *model in self.allFriendsHolder) {
            if ([[model.groupName  lowercaseString] rangeOfString:[searchTextString lowercaseString]].location != NSNotFound) {
                [self.communicatorArray addObject:model];
            }
        }
    }
    else {
        self.communicatorArray = [self.allFriendsHolder mutableCopy];
    }
    [self.communicatorTableView reloadData];
}


#pragma mark - QuickMessageDelegate
- (void)tappedMessage:(NSString *)message atIndexPath:(NSIndexPath *)indexPath {
    [self.communicatorTableView selectRowAtIndexPath:indexPath animated:YES scrollPosition:UITableViewScrollPositionNone];
    [self tableView: self.communicatorTableView didSelectRowAtIndexPath:indexPath];
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
