//
//  ReplyView.m
//  OneTraffic
//
//  Created by Stefan Andric on 4/27/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "ReplyView.h"
#import "Constant.h"
#import "UIHelper.h"
#import "QuickMessageCollectionViewCell.h"
#import "LogicHelper.h"

@implementation ReplyView

/*
 // Only override drawRect: if you perform custom drawing.
 // An empty implementation adversely affects performance during animation.
 - (void)drawRect:(CGRect)rect {
 // Drawing code
 }
 */


- (id)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = DARK_BLUE_TRANSPARENT;
        [self createInterface];
    }
    
    return self;
}


- (void)createInterface {
    
    UIButton *closeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    closeButton.frame = CGRectMake(SCREEN_WIDTH/2-17.5, SCREEN_HEIGHT-50, 35, 35);
    [closeButton setImage:[UIImage imageNamed:@"error"] forState:UIControlStateNormal];
    [closeButton addTarget:self action:@selector(closeTapped:) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:closeButton];
    
    self.quickReplyMessages = [LogicHelper makeLocalizableArrayOfKeys:@[@"Ok", @"No", @"Driving", @"Delayed", @"Locator", @"Started", @"Soon", @"When?", @"Where?"]];
    
    UILabel *headerLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, self.frame.size.width, 82)];
    headerLabel.font = [UIFont systemFontOfSize:20.0f];
    headerLabel.textColor = [UIColor whiteColor];
    headerLabel.textAlignment = NSTextAlignmentCenter;
    [self addSubview:headerLabel];
    
    UICollectionView *collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 90, self.frame.size.width, self.frame.size.height-140) collectionViewLayout:[self setUpCollectionViewFlow]];
    [collectionView registerNib:[UINib nibWithNibName:@"QuickMessageCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"quickMessageCell"];
    collectionView.delegate = self;
    collectionView.dataSource = self;
    collectionView.backgroundColor = DARK_BLUE;
    [self addSubview:collectionView];
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.quickReplyMessages.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    QuickMessageCollectionViewCell *cell = (QuickMessageCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:@"quickMessageCell" forIndexPath:indexPath];
    
    cell.messageLabel.text = self.quickReplyMessages[indexPath.row];
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    CGSize size = CGSizeMake(SCREEN_WIDTH/3-20, SCREEN_WIDTH/3-20);
    
    return size;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    [self removeFromSuperview];
    [self.delegate stringToSend:self.quickReplyMessages[indexPath.row]];
}

- (void)closeTapped:(UIButton *)sender {
    [self removeFromSuperview];
}

-(UICollectionViewFlowLayout*) setUpCollectionViewFlow
{
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [flowLayout setSectionInset:UIEdgeInsetsMake(0, 0, 0, 0)];
    flowLayout.minimumInteritemSpacing = 0;
    flowLayout.minimumLineSpacing = 10;
    
    return flowLayout;
}

@end
