//
//  QuickMessageViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 6/14/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CommunicatorModel.h"

@protocol QuickMessageDelegate <NSObject>
- (void)tappedMessage:(NSString *)message atIndexPath:(NSIndexPath *)indexPath;
@end

@interface QuickMessageViewController : UIViewController <UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>
@property (weak, nonatomic) IBOutlet UICollectionView *quickMessageCollectionView;
@property (nonatomic, strong) NSIndexPath *indexPathRemembered;
@property (nonatomic, weak) id <QuickMessageDelegate> delegate;
@property (nonatomic, strong) CommunicatorModel *communicatorModel;
@end
