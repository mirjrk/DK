//
//  SponsorMechanismView.h
//  OneTraffic
//
//  Created by Stefan Andric on 7/6/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SponsorMechanismView : UIView
@property (nonatomic, strong) UIImageView *sponsorImage;
//@property (nonatomic, strong) UILabel *sponsorText;
@property (nonatomic, assign) BOOL isSplash;
- (id)initWithFrame:(CGRect)frame isSplash:(BOOL)isSplash;
@end
