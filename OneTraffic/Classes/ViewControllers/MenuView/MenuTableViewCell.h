//
//  MenuTableViewCell.h
//  OneTraffic
//
//  Created by Stefan Andric on 4/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MenuTableViewCell : UITableViewCell
@property (nonatomic, strong) UIImageView *firstImage; //This image will be used two times in different purposes, so that is reason for generic name
@property (nonatomic, strong) UIImageView *secondImage;
@property (nonatomic, strong) UIImageView *thirdImage;

/* Generic names in purpose of constant name changing */
@property (nonatomic, strong) UIButton *firstButton;
@property (nonatomic, strong) UIButton *secondButton;
@property (nonatomic, strong) UIButton *thirdButton;
@property (nonatomic, strong) UIButton *forthButton;
@end
