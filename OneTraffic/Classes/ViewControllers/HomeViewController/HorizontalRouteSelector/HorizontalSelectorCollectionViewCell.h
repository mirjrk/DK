//
//  HorisontalSelectorCollectionViewCell.h
//  OneTraffic
//
//  Created by Stefan Andric on 12/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HorizontalSelectorCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UILabel *routeLabel;
@property (weak, nonatomic) IBOutlet UIView *backroundView;
@property (weak, nonatomic) IBOutlet UIView *selectedIndicatorView;

@end
