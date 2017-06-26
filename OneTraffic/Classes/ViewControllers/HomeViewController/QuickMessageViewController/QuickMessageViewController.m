//
//  QuickMessageViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 6/14/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "QuickMessageViewController.h"
#import "QuickMessageCollectionViewCell.h"
#import "Constant.h"
#import "LogicHelper.h"
#import "Communication.h"
#import "UIHelper.h"
#import "BasicViewController.h"

@interface QuickMessageViewController ()
@property (nonatomic, strong) NSArray *messagesArray;
@end

@implementation QuickMessageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = DARK_BLUE;
    [self.quickMessageCollectionView registerNib:[UINib nibWithNibName:@"QuickMessageCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"quickMessageCell"];
    self.messagesArray = [LogicHelper makeLocalizableArrayOfKeys:@[@"Started", @"Delayed", @"Locator", @"Soon", @"Arrived", @"Pickups?"]];
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.messagesArray.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    QuickMessageCollectionViewCell *cell = (QuickMessageCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"quickMessageCell" forIndexPath:indexPath];
    
    cell.messageLabel.text = self.messagesArray[indexPath.row];
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    [self sendMessage:self.messagesArray[indexPath.row] toModel:self.communicatorModel];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    CGSize size = CGSizeMake(SCREEN_WIDTH/3-20, SCREEN_WIDTH/3-20);
    
    return size;
}

- (void)sendMessage:(NSString *)message toModel:(CommunicatorModel *)model {
    
    [self showSpinner:NO];
    
    NSData *data = [message dataUsingEncoding:NSNonLossyASCIIStringEncoding];
    NSString *unicode = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    
    
    NSDictionary *params = @{@"groupId":self.communicatorModel.groupId,
               @"message": unicode,
               @"usersInGroup":@[]};
    
    [Communication sendMessage:params withSuccessBlock:^(NSDictionary *response) {
        [self showSpinner:NO];
        if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
            [self.delegate tappedMessage:unicode atIndexPath:self.indexPathRemembered];
            [self dismissViewControllerAnimated:YES completion:nil];
        }
    } errorBlock:^(NSDictionary *error) {
        [self showSpinner:NO];
    }];
    
}
/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */
- (IBAction)closeButtonTapped:(UIButton *)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
