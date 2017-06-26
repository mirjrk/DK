//
//  EventCollectionViewCell.h
//  OneTraffic
//
//  Created by Stefan Andric on 2/8/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EventCollectionViewCell : UICollectionViewCell
@property (nonatomic,retain) UIImageView *eventImage;
@property (nonatomic,retain) UILabel *eventName;
@property (nonatomic,retain) UIView *highlight;
@end
