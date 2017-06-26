//
//  EventCollectionViewCell.m
//  OneTraffic
//
//  Created by Stefan Andric on 2/8/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "EventCollectionViewCell.h"
#import "Constant.h"

@implementation EventCollectionViewCell
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        
        self.eventImage = [[UIImageView alloc]initWithFrame:CGRectMake(self.frame.size.width/2-35, self.frame.size.height/2-35, 70, 70)];
        [self addSubview:self.eventImage];
        self.eventName = [[UILabel alloc]initWithFrame:CGRectMake(2, self.frame.size.height-20, self.frame.size.width, 40)];
        self.eventName.textAlignment = NSTextAlignmentCenter;
        [self.eventName setTextColor:NEON_GREEN];
        [self addSubview:self.eventName];
        self.highlight = [[UIView alloc]initWithFrame:self.bounds];
        self.highlight.backgroundColor = [UIColor grayColor];
        [self addSubview:self.highlight];
    }
    return self;
}
@end
