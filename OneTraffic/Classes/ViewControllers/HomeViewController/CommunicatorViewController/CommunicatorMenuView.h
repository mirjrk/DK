//
//  CommunicatorMenuView.h
//  OneTraffic
//
//  Created by Stefan Andric on 3/1/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol CommunicatorMenuDelegate <NSObject>
- (void)selectedMenuIndexCommunicatorMenu:(NSInteger)index;
@end

@interface CommunicatorMenuView : UIView <UICollectionViewDelegate, UICollectionViewDataSource>

- (instancetype)initWithFrame:(CGRect)frame;
@property (nonatomic, weak) id <CommunicatorMenuDelegate> delegate;
@property (nonatomic, strong) UICollectionView *communicatorMenuCollectionView;
@end
