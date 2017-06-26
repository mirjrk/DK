//
//  TutorialView.h
//  OneTraffic
//
//  Created by Stefan Andric on 1/5/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TutorialView : UIView
@property (nonatomic, strong) NSString *type;
- (id)initWithFrame:(CGRect)frame andType:(NSString *)type;
@end
