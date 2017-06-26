//
//  UIViewController+Extended.m
//  OneTraffic
//
//  Created by Stefan Andric on 6/15/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "UIViewController+Extended.h"
#import "MBProgressHUD.h"
#import "Constant.h"

@implementation UIViewController (Extended)


- (void)showSpinner:(BOOL)show {
    if (show == YES) {
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    }
    else {
        [MBProgressHUD hideHUDForView:self.view animated:YES];
    }
}

@end
