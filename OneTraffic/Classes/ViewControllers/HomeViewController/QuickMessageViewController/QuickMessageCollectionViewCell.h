//
//  QuickMessageCollectionViewCell.h
//  OneTraffic
//
//  Created by Stefan Andric on 6/14/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QuickMessageCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *messageImage;
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;

@end
