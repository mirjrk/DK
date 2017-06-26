//
//  EventTableViewCell.h
//  OneTraffic
//
//  Created by Stefan Andric on 4/1/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EventTableViewCell : UITableViewCell
@property (nonatomic, strong) UIImageView *commentatorImage;
@property (nonatomic, strong) UILabel *commentatorLabel;
@property (nonatomic, strong) UITextView *commentText;
@property (nonatomic, strong) UILabel *commentTextLabel;
@property (nonatomic, strong) UIButton *appropriateButton;
@property (nonatomic, strong) UILabel *approriateLabel;
@property (nonatomic, strong) UIButton *inappropriateButton;
@property (nonatomic, strong) UILabel *inappropriateLabel;
@property (nonatomic, strong) UIView *contentViewComment;
@end
