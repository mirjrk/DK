//
//  CommunicatorMenuView.m
//  OneTraffic
//
//  Created by Stefan Andric on 3/1/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "CommunicatorMenuView.h"
#import "Constant.h"
#import "CommunicatorMenuCollectionViewCell.h"

@implementation CommunicatorMenuView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        
        [self setupView];
    }
    return self;
}

- (void)setupView {
    self.backgroundColor = DARK_BLUE;
    [self setupCollectionView];
}

- (void)setupCollectionView {
    self.communicatorMenuCollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, self.frame.size.height-380, SCREEN_WIDTH, 360) collectionViewLayout:[self setUpCollectionViewFlow]];
    self.communicatorMenuCollectionView.delegate = self;
    self.communicatorMenuCollectionView.dataSource = self;
    [self.communicatorMenuCollectionView registerNib:[UINib nibWithNibName:@"CommunicatorMenuCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"communicatorMenuCell"];
    self.communicatorMenuCollectionView.backgroundColor = [UIColor clearColor];
    [self addSubview:self.communicatorMenuCollectionView];
}

#pragma mark - UICollectionView delegates, datasource and setup
-(UICollectionViewFlowLayout*) setUpCollectionViewFlow {
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    [flowLayout setSectionInset:UIEdgeInsetsMake(0, 0, 0, 0)];
    flowLayout.minimumInteritemSpacing = 0;
    flowLayout.minimumLineSpacing = 0;
    
    return flowLayout;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return 6;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CommunicatorMenuCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"communicatorMenuCell" forIndexPath:indexPath];
    
    if (indexPath.row == 0) {
        cell.communicatorMenuItemLabel.text = NSLocalizedString(@"AddPlace", nil);
        cell.communicatorMenuItemImage.image = [UIImage imageNamed:@"setDestination"];
    }
    else if (indexPath.row == 1) {
        cell.communicatorMenuItemLabel.text = NSLocalizedString(@"AddFriend", nil);
    }
    else if (indexPath.row == 2) {
        cell.communicatorMenuItemLabel.text = NSLocalizedString(@"Channels", nil);
        cell.communicatorMenuItemImage.image = [UIImage imageNamed:@"Channels"];
    }
    else if (indexPath.row == 3) {
        cell.communicatorMenuItemLabel.text = NSLocalizedString(@"Locator", nil);
        cell.communicatorMenuItemImage.image = [UIImage imageNamed:@"locationSharing"];
    }
    
    else if (indexPath.row == 4) {
        cell.communicatorMenuItemLabel.text = NSLocalizedString(@"ReportEvent", nil);
        cell.communicatorMenuItemImage.image = [UIImage imageNamed:@"addEvent"];
    }
    
    else if (indexPath.row == 5) {
        cell.communicatorMenuItemLabel.text = NSLocalizedString(@"Settings", nil);
        cell.communicatorMenuItemImage.image = [UIImage imageNamed:@"SettingsIcon"];
    }
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(SCREEN_WIDTH/2, 120);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    [self.delegate selectedMenuIndexCommunicatorMenu:indexPath.row];
}

@end
